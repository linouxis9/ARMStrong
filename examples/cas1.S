b start @ Branch to start label
kek: .asciz "test"
.align @ Align memory as the string string "test\0" isn't a multiple of 4
start: ldr r0,=kek @ Program entrypoint, load the string address inside the r0 register
ldr r1,=0x1F0008 @ Load routine address to show a string in register r1
blx r1 @ Calls routine