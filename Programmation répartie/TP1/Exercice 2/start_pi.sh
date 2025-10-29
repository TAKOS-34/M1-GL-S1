#!/bin/bash

if [ "$#" -ne 3 ]; then
    echo "Usage: $0 <num_instances> <ip_pconfig> <port>"
    exit 1
fi

NUM=$1
IP=$2
PORT=$3

for (( i=1; i<=$NUM; i++ ))
do
   ./p_i "$IP" "$PORT" &
done

wait
