ldr r0,=kek @ File path (\0 terminated)
ldr r1,=content @ Address of content to write (\0 terminated)
ldr r2,=0x1F001C @ Write file routine's address
blx r2 @ Jump to routine
kek: .asciz "C:\\tmp\\kek.txt"
content: .asciz "salut toi!"