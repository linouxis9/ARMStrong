mov r0,#100
ldr r1,=0x1F0010
ldr r4,=0x1F1000


deb:
mov r5,0b101
str r5,[r4]

blx r1

mov r5,0b010
str r5,[r4]

blx r1

b deb
