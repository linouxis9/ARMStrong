ldr r0,=0x1f1000
mov r2,#0x7D
mov r1,#0

kek: str r2,[r0,r1]

add r1,r1,#1
cmp r1,#15

bne kek