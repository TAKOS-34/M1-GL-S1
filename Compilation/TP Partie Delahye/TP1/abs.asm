# abs.asm

.data
ask: .asciiz "Saisir un entier :\n"

.text
main:
	li $v0, 4
	la $a0, ask
	syscall

	li $v0, 5
	syscall

	move $a0, $v0
	blt $a0, $0 neg
	li $v0, 1
	syscall
	j end
	
neg:
	neg $a0, $a0
	li $v0, 1
	syscall
	
end:
