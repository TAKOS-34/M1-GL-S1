#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>

// Message envoyé à Pconfig pour l'enregistrement
typedef struct {
    int tcp_port; // Network byte order
} RegisterRequestMsg;

// Message initial reçu de Pconfig
typedef struct {
    int assigned_id;  // Network byte order
    int neighbor_count; // Network byte order
} ConfigStartMsg;

// Message reçu pour chaque voisin
typedef struct {
    int neighbor_id;          // Network byte order
    struct in_addr neighbor_ip; // Déjà en network byte order
    int neighbor_tcp_port;  // Network byte order
} NeighborInfoMsg;

// Structure locale pour stocker les informations d'un voisin
typedef struct {
    int id;
    struct sockaddr_in addr;
    int socket_fd; // Pourra stocker le FD entrant ou sortant
} NeighborInfo;

void die(char *s) {
    perror(s);
    exit(1);
}

int main(int argc, char *argv[]) {
    // --- Validation des arguments (sans le nom de fichier graphe) ---
    if (argc != 3) {
        fprintf(stderr, "Usage: %s <pconfig_ip> <pconfig_port>\n", argv[0]);
        exit(1);
    }
    const char* pconfig_ip = argv[1];
    int pconfig_port = atoi(argv[2]);

    // --- Création et configuration de la socket d'écoute TCP ---
    int listen_fd = socket(AF_INET, SOCK_STREAM, 0);
    if (listen_fd < 0) die("socket() listen TCP");

    struct sockaddr_in my_tcp_addr;
    memset(&my_tcp_addr, 0, sizeof(my_tcp_addr));
    my_tcp_addr.sin_family = AF_INET;
    my_tcp_addr.sin_port = htons(0);
    my_tcp_addr.sin_addr.s_addr = INADDR_ANY;

    if (bind(listen_fd, (struct sockaddr *)&my_tcp_addr, sizeof(my_tcp_addr)) < 0) {
        close(listen_fd);
        die("bind() TCP");
    }

    // Récupérer le port TCP attribué par le système
    socklen_t addr_len = sizeof(my_tcp_addr);
    if (getsockname(listen_fd, (struct sockaddr *)&my_tcp_addr, &addr_len) < 0) {
        close(listen_fd);
        die("getsockname() TCP");
    }
    int my_tcp_port = ntohs(my_tcp_addr.sin_port);
    printf("[Pi] Écoute TCP sur le port %d\n", my_tcp_port);

    int backlog = 15;
    if (listen(listen_fd, backlog) < 0) {
        close(listen_fd);
        die("listen() TCP");
    }

    // --- Enregistrement auprès de Pconfig via UDP ---
    int udp_fd = socket(AF_INET, SOCK_DGRAM, 0);
    if (udp_fd < 0) die("socket() UDP");

    struct sockaddr_in pconfig_addr;
    memset(&pconfig_addr, 0, sizeof(pconfig_addr));
    pconfig_addr.sin_family = AF_INET;
    pconfig_addr.sin_port = htons(pconfig_port);
    if (inet_aton(pconfig_ip, &pconfig_addr.sin_addr) == 0) {
        fprintf(stderr, "Adresse IP invalide pour Pconfig\n");
        close(udp_fd);
        exit(1);
    }

    // Envoyer le port TCP choisi
    RegisterRequestMsg req_msg = { .tcp_port = htonl(my_tcp_port) };
    printf("[Pi] Enregistrement auprès de Pconfig %s:%d...\n", pconfig_ip, pconfig_port);
    if (sendto(udp_fd, &req_msg, sizeof(req_msg), 0, (struct sockaddr *)&pconfig_addr, sizeof(pconfig_addr)) < 0) {
        close(udp_fd);
        die("sendto() UDP registration");
    }

    // --- Réception de la configuration (ID et voisins) ---
    ConfigStartMsg start_msg;
    if (recvfrom(udp_fd, &start_msg, sizeof(start_msg), 0, NULL, NULL) < 0) {
        close(udp_fd);
        die("recvfrom() ConfigStartMsg");
    }
    int my_id = ntohl(start_msg.assigned_id);
    int neighbor_count = ntohl(start_msg.neighbor_count);

    printf("[Pi %d] ID assigné. Attente des informations pour %d voisins...\n", my_id, neighbor_count);

    NeighborInfo* neighbors = NULL;
    if (neighbor_count > 0) {
        neighbors = (NeighborInfo*)malloc(neighbor_count * sizeof(NeighborInfo));
        if (!neighbors) die("malloc pour neighbors");
    }

    // Recevoir les détails de chaque voisin
    for (int i = 0; i < neighbor_count; i++) {
        NeighborInfoMsg neighbor_msg;
        if (recvfrom(udp_fd, &neighbor_msg, sizeof(neighbor_msg), 0, NULL, NULL) < 0) {
            perror("recvfrom() NeighborInfoMsg");
            if(neighbors) free(neighbors);
            close(udp_fd);
            close(listen_fd);
            exit(1);
        }
        neighbors[i].id = ntohl(neighbor_msg.neighbor_id);
        neighbors[i].addr.sin_family = AF_INET;
        neighbors[i].addr.sin_port = htons(ntohl(neighbor_msg.neighbor_tcp_port));
        neighbors[i].addr.sin_addr = neighbor_msg.neighbor_ip;
        neighbors[i].socket_fd = -1;

        char neighbor_ip_str[INET_ADDRSTRLEN];
        inet_ntop(AF_INET, &neighbors[i].addr.sin_addr, neighbor_ip_str, sizeof(neighbor_ip_str));
        printf("[Pi %d] Voisin %d/%d reçu : ID %d @ %s:%d\n", my_id, i + 1, neighbor_count, neighbors[i].id, neighbor_ip_str, ntohs(neighbors[i].addr.sin_port));
    }
    close(udp_fd);

    // --- Établissement des connexions TCP ---
    int connections_to_make = 0;
    int connections_to_accept = 0;
    if (neighbors) { // Vérifier si neighbors a été alloué
        for (int i = 0; i < neighbor_count; i++) {
            if (neighbors[i].id > my_id) {
                connections_to_make++;
            } else {
                connections_to_accept++;
            }
        }
    }
    printf("[Pi %d] Doit initier %d connexions et accepter %d connexions.\n", my_id, connections_to_make, connections_to_accept);

    // 1. Initier les connexions sortantes (vers les voisins avec ID > my_id)
    if (neighbors) {
        for (int i = 0; i < neighbor_count; i++) {
            if (neighbors[i].id > my_id) {
                printf("[Pi %d] Connexion vers Pi %d...\n", my_id, neighbors[i].id);
                int sock_fd_out = socket(AF_INET, SOCK_STREAM, 0);
                if (sock_fd_out < 0) die("socket() pour connexion sortante");

                while (connect(sock_fd_out, (struct sockaddr *)&neighbors[i].addr, sizeof(neighbors[i].addr)) < 0) {
                    perror("connect() - tentative échouée, nouvelle tentative dans 1s");
                    sleep(1);
                }
                neighbors[i].socket_fd = sock_fd_out;
                printf("[Pi %d] Connecté à Pi %d\n", my_id, neighbors[i].id);

                if (send(sock_fd_out, &my_id, sizeof(my_id), 0) < 0) perror("send_id() - échec de l'envoie de l'id");
                printf("[Pi %d] Id envoyé à Pi %d\n", my_id, neighbors[i].id);
            }
        }
    }

    // 2. Accepter les connexions entrantes (des voisins avec ID < my_id)
    for (int i = 0; i < connections_to_accept; i++) {
        printf("[Pi %d] Attente de la connexion %d/%d...\n", my_id, i + 1, connections_to_accept);
        struct sockaddr_in peer_addr;
        socklen_t peer_len = sizeof(peer_addr);
        int incoming_fd = accept(listen_fd, (struct sockaddr*)&peer_addr, &peer_len);
        if (incoming_fd < 0) die("accept()");

        int incoming_id;
        int incoming_id_recv = recv(incoming_fd, &incoming_id, sizeof(incoming_id), 0);
        if (incoming_id_recv < 0 || incoming_id_recv != sizeof(incoming_id) ) die("incoming_id()");

        int found_neighbor_idx = -1;
        if (neighbors) { // Vérifier si neighbors existe
            for(int j = 0; j < neighbor_count; ++j){
                if(neighbors[j].id < my_id && neighbors[j].addr.sin_addr.s_addr == peer_addr.sin_addr.s_addr && neighbors[j].id == incoming_id) {
                    neighbors[j].socket_fd = incoming_fd;
                    found_neighbor_idx = j;
                    break;
                }
            }
        }

        if (found_neighbor_idx != -1) {
            char peer_ip[INET_ADDRSTRLEN];
            inet_ntop(AF_INET, &peer_addr.sin_addr, peer_ip, sizeof(peer_ip));
            printf("[Pi %d] Connexion acceptée de Pi %d (%s:%d).\n", my_id, neighbors[found_neighbor_idx].id, peer_ip, ntohs(peer_addr.sin_port));
        } else {
            char peer_ip[INET_ADDRSTRLEN];
            inet_ntop(AF_INET, &peer_addr.sin_addr, peer_ip, sizeof(peer_ip));
            fprintf(stderr, "[Pi %d] Connexion inconnue reçue de %s:%d. Fermeture.\n", my_id, peer_ip, ntohs(peer_addr.sin_port));
            close(incoming_fd);
            i--; // Réessayer d'accepter la bonne connexion
        }
    }

    close(listen_fd);
    printf("[Pi %d] Toutes les connexions TCP sont établies.\n\n", my_id);

    // --- Attente de l'entrée clavier sur le Pi 1 ---
    if (my_id == 1) {
        printf("--- [Pi %d] Processus Leader --- \n", my_id);
        printf("Appuyez sur Entrée pour terminer...\n");
        getchar();
        printf("--- [Pi %d] Terminaison initiée... ---\n", my_id);
    } else {
        printf("[Pi %d] En attente (le leader doit appuyer sur Entrée pour terminer).\n", my_id);
        // Attente passive : lire depuis une socket connectée.
        // La lecture se débloquera si le pair ferme la connexion (ou en cas d'erreur).
        char dummy_buffer[1];
        int read_fd = -1;
        // Trouver une socket connectée pour l'attente
        if (neighbors) {
            for(int i=0; i < neighbor_count; ++i) {
                if (neighbors[i].socket_fd != -1) {
                    read_fd = neighbors[i].socket_fd;
                    break;
                }
            }
        }
        if (read_fd != -1) {
            recv(read_fd, dummy_buffer, sizeof(dummy_buffer), 0); // Attente bloquante
        } else {
            printf("[Pi %d] Avertissement: Aucun voisin connecté pour l'attente passive.\n", my_id);
            sleep(10); // Attendre un peu avant de terminer
        }
    }

    // --- Fermeture des connexions et libération ---
    printf("[Pi %d] Fermeture des connexions...\n", my_id);
    if (neighbors) {
        for (int i = 0; i < neighbor_count; i++) {
            if (neighbors[i].socket_fd != -1) {
                close(neighbors[i].socket_fd);
            }
        }
        free(neighbors);
    }

    printf("[Pi %d] Terminé.\n", my_id);
    return 0;
}