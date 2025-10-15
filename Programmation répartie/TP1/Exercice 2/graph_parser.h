#ifndef GRAPH_PARSER_H
#define GRAPH_PARSER_H

// Structure pour un nœud de la liste d'adjacence
typedef struct Node {
    int dest;           // Le sommet de destination de l'arête
    struct Node* next;  // Le voisin suivant dans la liste
} Node;

// Structure principale pour le graphe
typedef struct Graph {
    int num_vertices;   // Nombre total de sommets
    Node** adj_lists;   // Tableau de listes chaînées (listes d'adjacence)
} Graph;

/**
 * @brief Parse un fichier graphe au format DIMACS (.col).
 * * @param filename Le chemin vers le fichier .col.
 * @return Un pointeur vers la structure Graph allouée et remplie, ou NULL en cas d'erreur.
 */
Graph* parse_dimacs_graph(const char* filename);

/**
 * @brief Libère toute la mémoire allouée pour le graphe.
 * * @param graph Le graphe à libérer.
 */
void free_graph(Graph* graph);

#endif // GRAPH_PARSER_H
