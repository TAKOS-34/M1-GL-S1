#!/bin/bash

if [ "$#" -ne 3 ]; then
    echo "Usage: $0 <num_instances> <ip_pconfig> <port>"
    exit 1
fi

NUM=$1
IP=$2
PORT=$3

TERMINAL_CMD=(gnome-terminal --title=p_i_Instance)

for (( i=1; i<=$NUM; i++ ))
do
    "${TERMINAL_CMD[@]}" -- bash -c "./p_i \"$IP\" \"$PORT\" ; exec bash" &
done

wait
