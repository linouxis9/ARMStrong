mov r0,0x3000 @ Address to save / print string
mov r1,#0xFF08 @ Routine to get String out of the GUI console
blx r1 @ Calls routine
mov r1,#0xFF04 @ Routine to print string in RAM
blx r1 @ Calls routine