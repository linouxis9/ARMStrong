@ Hmm... Ne trouvez-vous pas qu'il y a quelque chose de bizarre lors de l'exécution de ce code ;)?
ldr r0,=kek
ldrb r0,[r0]
kek:.asciz "test"

@ Solution
b start
kek: .asciz "test"
.align
start: ldr r0,=kek
ldrb r0,[r0]