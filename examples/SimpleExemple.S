@ Comparaison et branchement

	cmp r4, r5
	beq conditionvraie		@ tester les codes eq, ne, ge, gt, le et lt
						@ et essayer de déterminer leur signification
	b conditionfausse

conditionvraie:			@ la condition est vraie
	ldr r0, =chaine1
	ldr r1, =0x1F0008
	blx r1
	b fin
conditionfausse:		@ la condition est fausse
	ldr r0, =chaine2
	ldr r1, =0x1F0008
	blx r1

fin:
	.stop

@ Données
chaine1:	.asciz "La condition est vraie"
chaine2:	.asciz "La condition est fausse !"