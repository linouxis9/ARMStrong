ldr r1,=0x1F0018
mov r0,#0

kek: add r0,r0,#1
blx r1
b kek