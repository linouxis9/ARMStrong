ldr r1,=newAddress
ldr r2,=newName
str r2,[r1]

ldr r1,=oldAddress
ldr r2,=oldName
str r2,[r1]

svc #0xf

.stop

struct:
oldAddress:.word 0
oldLength:.word 7
newAddress:.word 0
newLength:.word 7
oldName:.asciz "/tmp/sa"
newName:.asciz "/tmp/sb"


