ldr r1,=struct
ldr r2,=string
str r2,[r1]
svc #0x12

.stop

struct:.word 0
length:.word 13
string:.asciz "mkdir /tmp/sa"
