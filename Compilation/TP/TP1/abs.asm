# abs.asm

.text
main:
	li $v0, 5
	syscall
	move $a0, $v0
	blt $a0, $0 neg
	li $v0 1
	syscall
	j end
	
neg:
	neg $a0 $a0
	li $v0 1
	syscall
	
end: