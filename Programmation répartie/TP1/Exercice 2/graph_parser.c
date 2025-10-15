#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "graph_parser.h" // On inclut notre propre header

// Fonction interne (static) pour ajouter une arête
static void add_edge(Graph* graph, int src, int dest) {
    // Les sommets sont 1-indexés dans le fichier, on les convertit en 0-indexé
    src--; 
    dest--;

    // Vérification des limites
    if (src < 0 || src >= graph->num_vertices || dest < 0 || dest >= graph->num_vertices) {
        fprintf(stderr, "Erreur : Sommet invalide lors de l'ajout de l'arête (%d, %d)\n", src + 1, dest + 1);
        return;
    }

    // Ajoute l'arête de src -> dest
    Node* new_node_dest = (Node*)malloc(sizeof(Node));
    new_node_dest->dest = dest;
    new_node_dest->next = graph->adj_lists[src];
    graph->adj_lists[src] = new_node_dest;

    // Ajoute l'arête de dest -> src (car le graphe est non-orienté)
    Node* new_node_src = (Node*)malloc(sizeof(Node));
    new_node_src->dest = src;
    new_node_src->next = graph->adj_lists[dest];
    graph->adj_lists[dest] = new_node_src;
}

// Implémentation de la fonction de parsing déclarée dans le .h
Graph* parse_dimacs_graph(const char* filename) {
    FILE* file = fopen(filename, "r");
    if (!file) {
        perror("Erreur lors de l'ouverture du fichier graphe");
        return NULL;
    }

    char line_buffer[256];
    Graph* graph = NULL;
    int num_vertices = 0;
    int num_edges = 0;

    while (fgets(line_buffer, sizeof(line_buffer), file)) {
        if (line_buffer[0] == 'p') {
            if (sscanf(line_buffer, "p edge %d %d", &num_vertices, &num_edges) == 2) {
                graph = (Graph*)malloc(sizeof(Graph));
                if (!graph) {
                    perror("Erreur d'allocation pour le graphe");
                    fclose(file);
                    return NULL;
                }
                graph->num_vertices = num_vertices;
                graph->adj_lists = (Node**)calloc(num_vertices, sizeof(Node*)); // calloc initialise à NULL
                if (!graph->adj_lists) {
                    perror("Erreur d'allocation pour les listes d'adjacence");
                    free(graph);
                    fclose(file);
                    return NULL;
                }
            }
        } else if (line_buffer[0] == 'e') {
            if (!graph) {
                fprintf(stderr, "Erreur de format : ligne 'e' trouvée avant la ligne 'p'.\n");
                fclose(file);
                return NULL;
            }
            int u, v;
            if (sscanf(line_buffer, "e %d %d", &u, &v) == 2) {
                add_edge(graph, u, v);
            }
        }
        // Les lignes 'c' et autres sont ignorées
    }

    fclose(file);
    return graph;
}

// Implémentation de la fonction pour libérer la mémoire
void free_graph(Graph* graph) {
    if (!graph) return;

    for (int i = 0; i < graph->num_vertices; i++) {
        Node* current = graph->adj_lists[i];
        while (current) {
            Node* tmp = current;
            current = current->next;
            free(tmp);
        }
    }
    free(graph->adj_lists);
    free(graph);
}
