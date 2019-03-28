mov r0,#25
ldr r1,=0x1F0010
ldr r4,=0x1F1000
mov r6,#-1

if:mov r5,0x80
add r6,r6,#1
deb:strb r5,[r4,r6]

blx r1

mov r5,r5, LSR #1
cmp r5,#0x0
beq if

blx r1

cmp r6,#15

bne deb
