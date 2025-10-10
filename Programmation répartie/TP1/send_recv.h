int sendTCP(int sock, void* msg, int sizeMsg) {
    int total_send = 0;
    int sent = 0;

    while (total_send < sizeMsg) {
        sent = send(sock, (char *)msg + total_send, sizeMsg - total_send, 0);
        if (sent < 0) {
            return -1;
        }
        if (sent == 0) {
            return 0;
        }
        total_send += sent;
    }

    if (total_send == sizeMsg) {
        return 1;
    } else {
        return -1;
    }
}

int recvTCP(int sock, void* msg, int sizeMsg) {
    int total_recv = 0;
    int res = 0;

    while (total_recv < sizeMsg) {
        res = recv(sock, (char *)msg + total_recv, sizeMsg - total_recv, 0);
        if (res < 0) {
            return -1;
        }
        if (res == 0) {
            return 0;
        }
        total_recv += res;
    }

    if (total_recv == sizeMsg) {
        return 1;
    } else {
        return -1;
    }
}