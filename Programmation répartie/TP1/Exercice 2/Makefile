CC = gcc
CFLAGS = -Wall -Wextra -g
LDFLAGS =

TARGETS = p_config p_i

all: $(TARGETS)

p_config: p_config.c graph_parser.c graph_parser.h
	$(CC) $(CFLAGS) p_config.c graph_parser.c -o p_config $(LDFLAGS)

p_i: p_i.c
	$(CC) $(CFLAGS) p_i.c -o p_i $(LDFLAGS)

clean:
	rm -f $(TARGETS)

.PHONY: all clean