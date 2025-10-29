#ifndef GRAPH_PARSER_H
#define GRAPH_PARSER_H

typedef struct Node {
    int dest;
    struct Node* next;
} Node;

typedef struct Graph {
    int num_vertices;
    Node** adj_lists;
} Graph;

Graph* parse_dimacs_graph(const char* filename);

void free_graph(Graph* graph);

#endif