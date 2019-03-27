ldr r0,=kek @ File path
mov r1,#0x3000 @ Where to write File bytes
ldr r2,=0x1F0020 @ Open file routine's address
blx r2 @ Calls routine
kek: .asciz "C:\\kek.txt"