#!/bin/sh

apt install nfs-kernel-server
mkdir -p /srv/nfs/commons
chown nobody:nogroup /srv/nfs/commons

echo "/srv/nfs/commons *(rw,sync,no_subtree_check,no_root_squash)" | tee -a /etc/exports
exportfs -a
systemctl restart nfs-kernel-server
