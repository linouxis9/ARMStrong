ldr r1,=0x1F0010
mov r0,#1000
mov r2,#0

kek: add r2,r2,#1
blx r1
b kek