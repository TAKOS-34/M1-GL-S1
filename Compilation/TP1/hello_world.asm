# hello_world.asm

.data
hello: .asciiz "hello world\n"

.text
main:
    li $v0, 4       # appel système : print_string
    la $a0, hello   # adresse de la chaîne
    syscall
