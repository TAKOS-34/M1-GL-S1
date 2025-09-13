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
	
	li $t1, 2
	div $t0, $t1
	mfhi $t2
	beq $t2, 0, pair

	li $v0, 4
	la $a0, _impair
	syscall
	j end

pair:
	li $v0, 4
	la $a0, _pair
	syscall
	
end: