b start
kek: .asciz "test"
.align
start: ldr r0,=kek
mov r1,#0xFF04
blx r1