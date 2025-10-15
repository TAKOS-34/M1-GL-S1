#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>

typedef struct {
    int tcp_port;
} RegisterRequestMsg;

typedef struct {
    int assigned_id;
    struct in_addr successor_ip;
    int successor_tcp_port;
} RegistrationResponseMsg;

typedef struct {
    int initiator_id;
    int hop_count;
} Token;

void die(char *s) {
    perror(s);
    exit(1);
}

int main(int argc, char *argv[]) {
    // --- Création de la socket UDP / TCP & désignation de Pconfig ---
    if (argc != 3) {
        fprintf(stderr, "Usage: %s <pconfig_ip> <pconfig_port>\n", argv[0]);
        exit(1);
    }

    int my_id;
    int listen_fd = socket(AF_INET, SOCK_STREAM, 0);
    if (listen_fd < 0) die("socket() listen");

    struct sockaddr_in my_tcp_addr;
    memset(&my_tcp_addr, 0, sizeof(my_tcp_addr));
    my_tcp_addr.sin_family = AF_INET;
    my_tcp_addr.sin_port = htons(0);
    my_tcp_addr.sin_addr.s_addr = INADDR_ANY;

    if (bind(listen_fd, (struct sockaddr *)&my_tcp_addr, sizeof(my_tcp_addr)) < 0) die("bind()");

    socklen_t addr_len = sizeof(my_tcp_addr);
    if (getsockname(listen_fd, (struct sockaddr *)&my_tcp_addr, &addr_len) < 0) die("getsockname()");

    int my_tcp_port = ntohs(my_tcp_addr.sin_port);
    printf("[Pi] Registering...\n");

    int udp_fd = socket(AF_INET, SOCK_DGRAM, 0);
    if (udp_fd < 0) die("socket() udp");

    struct sockaddr_in pconfig_addr;
    memset(&pconfig_addr, 0, sizeof(pconfig_addr));
    pconfig_addr.sin_family = AF_INET;
    pconfig_addr.sin_port = htons(atoi(argv[2]));
    if (inet_aton(argv[1], &pconfig_addr.sin_addr) == 0) die("inet_aton()");

    // --- Envoie de la configuration TCP à Pconfig ---
    RegisterRequestMsg req_msg = { .tcp_port = htonl(my_tcp_port) };
    if (sendto(udp_fd, &req_msg, sizeof(req_msg), 0, (struct sockaddr *)&pconfig_addr, sizeof(pconfig_addr)) < 0) die("sendto()");

    // --- Récupération de la configuration TCP du successeur ---
    RegistrationResponseMsg resp_msg;
    if (recvfrom(udp_fd, &resp_msg, sizeof(resp_msg), 0, NULL, NULL) < 0) die("recvfrom()");
    close(udp_fd);

    my_id = ntohl(resp_msg.assigned_id);
    int successor_port = ntohl(resp_msg.successor_tcp_port);
    printf("[Pi %d] Assigned ID. Successor port : %d\n", my_id, successor_port);
    
    int pred_fd = -1, succ_fd = -1;
    struct sockaddr_in succ_addr = {0};
    succ_addr.sin_family = AF_INET;
    succ_addr.sin_port = htons(successor_port);
    succ_addr.sin_addr = resp_msg.successor_ip;

    if (listen(listen_fd, 1) < 0) die("listen()");

    // --- Si leader alors on accepte pour débloquer sinon attente de connexion ---
    if (my_id == 1) {
        printf("[Pi %d] LEADER: Waiting for predecessor...\n", my_id);
        pred_fd = accept(listen_fd, NULL, NULL);
        if (pred_fd < 0) die("accept()");
        printf("[Pi %d] Predecessor connected. Connecting to successor...\n", my_id);
        succ_fd = socket(AF_INET, SOCK_STREAM, 0);
        if (succ_fd < 0) die("socket() successor");
        while (connect(succ_fd, (struct sockaddr *)&succ_addr, sizeof(succ_addr)) < 0) sleep(1);
    } else {
        printf("[Pi %d] Connecting to successor...\n", my_id);
        succ_fd = socket(AF_INET, SOCK_STREAM, 0);
        if (succ_fd < 0) die("socket() successor");
        while (connect(succ_fd, (struct sockaddr *)&succ_addr, sizeof(succ_addr)) < 0) sleep(1);
        printf("[Pi %d] Successor connected. Waiting for predecessor...\n", my_id);
        pred_fd = accept(listen_fd, NULL, NULL);
        if (pred_fd < 0) die("accept()");
    }
    close(listen_fd);
    printf("[Pi %d] Ring is now complete\n\n", my_id);

    // --- Si leader alors j'envoie directement le premier message au successeur ---
    Token token;
    if (my_id == 1) {
        token.initiator_id = my_id;
        token.hop_count = 1;
        printf("--- [Pi %d] Initiator sending token (count=%d) ---\n", my_id, token.hop_count);
        send(succ_fd, &token, sizeof(token), 0);
    }

    // --- Sinon attente de recevoir le token ---
    if (recv(pred_fd, &token, sizeof(token), 0) <= 0) {
        printf("[Pi %d] Predecessor connection lost. Exiting\n", my_id);
    }

    // --- Si leader alors le token est revenue j'affiche le resultat, sinon je passe le token ---
    if (token.initiator_id == my_id) {
        printf("--- [Pi %d] Token returned! ---\n", my_id);
        printf(">>> Ring size is: %d <<<\n", token.hop_count);
    } else {
        token.hop_count++;
        printf("[Pi %d] Token received and forwarded (count=%d)\n", my_id, token.hop_count);
        send(succ_fd, &token, sizeof(token), 0);
    }

    close(pred_fd);
    close(succ_fd);
    printf("[Pi %d] Terminating.\n", my_id);
    return 0;
}