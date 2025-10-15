#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

typedef struct {
    int id;
    struct sockaddr_in pi_addr;
    int tcp_port;
} PiInfo;

typedef struct {
    int tcp_port;
} RegisterRequestMsg;

typedef struct {
    int assigned_id;
    struct in_addr successor_ip;
    int successor_tcp_port;
} RegistrationResponseMsg;

int main(int argc, char *argv[]) {
    // --- Création de la socket UDP & désignation ---
    if (argc != 3) {
        fprintf(stderr, "Usage: %s <pi_count> <udp_port>\n", argv[0]);
        exit(1);
    }

    int total_pi = atoi(argv[1]);
    if (total_pi <= 1) {
        fprintf(stderr, "Error: pi_count must be > 1\n");
        exit(1);
    }

    int sock_fd = socket(AF_INET, SOCK_DGRAM, 0);
    if (sock_fd < 0) {
        perror("socket creation failed");
        exit(1);
    }

    struct sockaddr_in serv_addr;
    memset(&serv_addr, 0, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_port = htons(atoi(argv[2]));
    serv_addr.sin_addr.s_addr = INADDR_ANY;

    if (bind(sock_fd, (struct sockaddr *)&serv_addr, sizeof(serv_addr)) < 0) {
        perror("bind failed");
        close(sock_fd);
        exit(1);
    }

    printf("Pconfig listening for %d processes...\n", total_pi);

    PiInfo pi_registry[total_pi + 1];
    memset(pi_registry, 0, sizeof(pi_registry));

    // --- Attente et stockage des informations des Pi ---
    for (int id = 1; id <= total_pi; id++) {
        RegisterRequestMsg req_msg;
        struct sockaddr_in client_addr;
        socklen_t client_len = sizeof(client_addr);

        if (recvfrom(sock_fd, &req_msg, sizeof(req_msg), 0, (struct sockaddr *)&client_addr, &client_len) < 0) {
            perror("recvfrom failed");
            id--;
            continue;
        }

        pi_registry[id].id = id;
        pi_registry[id].pi_addr = client_addr;
        pi_registry[id].tcp_port = ntohl(req_msg.tcp_port);

        char client_ip[INET_ADDRSTRLEN];
        inet_ntop(AF_INET, &client_addr.sin_addr, client_ip, sizeof(client_ip));
        printf("Registered Pi %d/%d from %s | TCP port: %d\n", id, total_pi, client_ip, pi_registry[id].tcp_port);
    }

    printf("\nAll processes registered. Sending successor info...\n");

    // --- Envoie des informations des successeurs aux Pi ---
    for (int i = 1; i <= total_pi; i++) {
        int successor_id = (i % total_pi) + 1;
        RegistrationResponseMsg resp_msg;
        resp_msg.assigned_id = htonl(i);
        resp_msg.successor_ip = pi_registry[successor_id].pi_addr.sin_addr;
        resp_msg.successor_tcp_port = htonl(pi_registry[successor_id].tcp_port);

        sendto(sock_fd, &resp_msg, sizeof(resp_msg), 0, (struct sockaddr *)&pi_registry[i].pi_addr, sizeof(pi_registry[i].pi_addr));
    }

    printf("Configuration sent. Shutting down\n");
    close(sock_fd);
    return 0;
}