# Makefile pour le TP1 - Programmation répartie
# Calcul de la taille d'un anneau de processus

# Compilateur et options
CC = gcc
CFLAGS = -Wall -Wextra -g
LDFLAGS = 

# Cibles exécutables
TARGETS = p_config p_i

# Règle par défaut : compiler tous les exécutables
all: $(TARGETS)

# Compilation directe de p_config (sans fichiers .o)
p_config: p_config.c send_recv.h
	$(CC) $(CFLAGS) -o $@ $< $(LDFLAGS)

# Compilation directe de p_i (sans fichiers .o)
p_i: p_i.c send_recv.h
	$(CC) $(CFLAGS) -o $@ $< $(LDFLAGS)

# Nettoyage des exécutables
clean:
	rm -f $(TARGETS)

# Nettoyage complet (alias de clean)
mrproper: clean

# Reconstruire complètement
rebuild: mrproper all

# Cibles factices (ne correspondent pas à des fichiers)
.PHONY: all clean mrproper rebuild
