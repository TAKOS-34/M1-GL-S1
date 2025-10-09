# pair.asm

.data
ask: .asciiz "Saisir un entier :\n"
_pair: .asciiz "Nombre pair\n"
_impair: .asciiz "Nombre impair\n"

.text
main:
	li $v0, 4
	la $a0, ask
	syscall
	
	li $v0, 5
	syscall
	move $t0, $v0
	li $t1, 1
	li $t2, 2
	
boucle:
	beqz $t0, pair
	beq $t0, $t1, impair
	sub $t0, $t0, $t2
	j boucle

pair:
	li $v0, 4
	la $a0, _pair
	syscall
	j end

impair:
	li $v0, 4
	la $a0, _impair
	syscall
	
end:
