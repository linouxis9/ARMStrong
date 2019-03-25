ldr r1,=struct
ldr r2,=string
str r2,[r1]
svc #0x01

.stop

struct:.word 0
.word 5
length:.word 7
string:.asciz "/tmp/sb"

