mov r0,#100
mov r1,#0xFF14

deb:mov r4,#0xe000
mov r5,0b101
str r5,[r4]

blx r1

mov r4,#0xe000
mov r5,0b010
str r5,[r4]

blx r1

b deb
