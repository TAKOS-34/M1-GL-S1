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
	move $t3, $v0
	li $t0, 0
	li $t1, 1
	li $t2, 2
	j boucle
	
boucle:
	beq $t0, $t3, pair
	beq $t1, $t3, impair
	sub $t3, $t3, $t2
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
	j end
	
end: