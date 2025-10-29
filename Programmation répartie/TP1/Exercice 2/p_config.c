#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include "graph_parser.h"

// Structure pour stocker les infos réseau d'un Pi enregistré
typedef struct {
    int id;
    struct sockaddr_in pi_addr; // Adresse UDP utilisée pour l'enregistrement
    int tcp_port;               // Port TCP sur lequel le Pi écoutera
} PiInfo;

// Message reçu d'un Pi lors de l'enregistrement
typedef struct {
    int tcp_port; // Port TCP sur lequel le Pi écoutera (envoyé en network byte order)
} RegisterRequestMsg;

// Message initial envoyé à chaque Pi après enregistrement
typedef struct {
    int assigned_id;  // ID attribué au Pi
    int neighbor_count; // Nombre de voisins dans le graphe
} ConfigStartMsg;

// Message envoyé pour chaque voisin
typedef struct {
    int neighbor_id;          // ID du voisin
    struct in_addr neighbor_ip; // IP du voisin
    int neighbor_tcp_port;  // Port TCP du voisin
} NeighborInfoMsg;

void die(char *s) {
    perror(s);
    exit(1);
}

int main(int argc, char *argv[]) {
    // --- Validation des arguments ---
    if (argc != 4) {
        fprintf(stderr, "Usage: %s <pi_count> <udp_port> <graph_file.col>\n", argv[0]);
        exit(1);
    }

    int total_pi = atoi(argv[1]);
    if (total_pi <= 0) { // Un seul nœud n'a pas de sens pour les connexions
        fprintf(stderr, "Erreur : pi_count doit être > 0\n");
        exit(1);
    }
    int udp_port = atoi(argv[2]);
    const char* graph_filename = argv[3];

    // --- Parsing du graphe ---
    printf("Parsing du graphe depuis %s...\n", graph_filename);
    Graph* graph = parse_dimacs_graph(graph_filename);
    if (!graph) {
        fprintf(stderr, "Erreur lors du parsing du graphe.\n");
        exit(1);
    }
    if (graph->num_vertices != total_pi) {
        fprintf(stderr, "Erreur : Le nombre de sommets dans le graphe (%d) ne correspond pas à pi_count (%d).\n",
                graph->num_vertices, total_pi);
        free_graph(graph);
        exit(1);
    }
    printf("Graphe chargé : %d sommets.\n", graph->num_vertices);

    // --- Création et configuration de la socket UDP ---
    int sock_fd = socket(AF_INET, SOCK_DGRAM, 0);
    if (sock_fd < 0) die("socket UDP creation failed");

    struct sockaddr_in serv_addr;
    memset(&serv_addr, 0, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_port = htons(udp_port);
    serv_addr.sin_addr.s_addr = INADDR_ANY;

    if (bind(sock_fd, (struct sockaddr *)&serv_addr, sizeof(serv_addr)) < 0) {
        close(sock_fd); // Fermer avant die() si bind échoue
        die("bind UDP failed");
    }

    printf("Pconfig écoute sur le port UDP %d pour %d processus...\n", udp_port, total_pi);

    // Tableau pour stocker les informations des processus enregistrés (index 1 à total_pi)
    PiInfo pi_registry[total_pi + 1];
    memset(pi_registry, 0, sizeof(pi_registry));
    int registered_count = 0;

    // --- Attente et stockage des informations des Pi ---
    while (registered_count < total_pi) {
        RegisterRequestMsg req_msg;
        struct sockaddr_in client_addr;
        socklen_t client_len = sizeof(client_addr);

        if (recvfrom(sock_fd, &req_msg, sizeof(req_msg), 0, (struct sockaddr *)&client_addr, &client_len) < 0) {
            perror("recvfrom failed");
            continue; // Réessayer en cas d'erreur
        }

        registered_count++;
        pi_registry[registered_count].id = registered_count; // Attribuer l'ID séquentiellement
        pi_registry[registered_count].pi_addr = client_addr; // Stocker l'adresse UDP
        pi_registry[registered_count].tcp_port = ntohl(req_msg.tcp_port); // Stocker le port TCP

        char client_ip[INET_ADDRSTRLEN];
        inet_ntop(AF_INET, &client_addr.sin_addr, client_ip, sizeof(client_ip));
        printf("Enregistré Pi %d/%d depuis %s | Port TCP: %d\n", registered_count, total_pi, client_ip, pi_registry[registered_count].tcp_port);
    }

    printf("\nTous les processus sont enregistrés. Envoi des informations de voisinage...\n");

    // --- Envoi des informations de voisinage à chaque Pi ---
    for (int i = 1; i <= total_pi; i++) {
        // Compter les voisins du nœud i (index i-1 dans le graphe)
        int neighbor_count = 0;
        Node* current_neighbor = graph->adj_lists[i - 1];
        while (current_neighbor) {
            neighbor_count++;
            current_neighbor = current_neighbor->next;
        }

        // Envoyer le message de démarrage de configuration
        ConfigStartMsg start_msg;
        start_msg.assigned_id = htonl(i);
        start_msg.neighbor_count = htonl(neighbor_count);

        if (sendto(sock_fd, &start_msg, sizeof(start_msg), 0, (struct sockaddr *)&pi_registry[i].pi_addr, sizeof(pi_registry[i].pi_addr)) < 0) {
            perror("sendto ConfigStartMsg failed");
            continue; // Passer au suivant si échec
        }

        // Envoyer les informations pour chaque voisin
        current_neighbor = graph->adj_lists[i - 1];
        while (current_neighbor) {
            int neighbor_graph_index = current_neighbor->dest; // Index 0-based
            int neighbor_id = neighbor_graph_index + 1;       // ID 1-based

            NeighborInfoMsg neighbor_msg;
            neighbor_msg.neighbor_id = htonl(neighbor_id);
            neighbor_msg.neighbor_ip = pi_registry[neighbor_id].pi_addr.sin_addr; // Utiliser l'IP enregistrée
            neighbor_msg.neighbor_tcp_port = htonl(pi_registry[neighbor_id].tcp_port); // Utiliser le port TCP enregistré

            if (sendto(sock_fd, &neighbor_msg, sizeof(neighbor_msg), 0, (struct sockaddr *)&pi_registry[i].pi_addr, sizeof(pi_registry[i].pi_addr)) < 0) {
                perror("sendto NeighborInfoMsg failed");
                // Gérer l'erreur si nécessaire, peut-être arrêter ou notifier
            }
            current_neighbor = current_neighbor->next;
        }
        printf("Configuration envoyée à Pi %d (%d voisins).\n", i, neighbor_count);
    }

    printf("\nConfiguration envoyée à tous les processus. Arrêt de Pconfig.\n");
    free_graph(graph); // Libérer la mémoire du graphe
    close(sock_fd);
    return 0;
}