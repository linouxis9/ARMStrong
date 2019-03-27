mov r0,0x3000 @ Address to save / print string
ldr r1,=0x1F000C @ Routine to get String out of the GUI console
blx r1 @ Calls routine
ldr r1,=0x1F0008 @ Routine to print string in RAM
blx r1 @ Calls routine