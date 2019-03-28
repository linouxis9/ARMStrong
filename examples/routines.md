CpuConsolePutChar 0x1F0000
	- r0 @ ASCII
	- r0 -> stdout
	
CpuConsoleGetChar 0x1F0004
	- r0 @ ASCII
	- 1 char consoleBuffer -> r0
	
CpuConsolePutString 0x1F0008
	- r0 -> Pointer to a null-terminated string
	- *r0 -> stdout
	
CpuConsoleGetString 0x1F000C
	- r0 @ Pointer to a buffer
	- whole consoleBuffer -> *r0

CpuSleep 0x1F0010
	- r0 @ Wait in ms
	- sleep(r0)
	
CpuRandom 0x1F0014
	- Random 32 bits integer -> r0
	
CpuBreakpoint 0x1F0018
	- Halt execution

CpuPutFile 0x10001C
	- r0 @ File path
	- r1 @ Where to read content

CpuGetFile 0x1F0020
	- r0 @ File path
	- r1 @ Where to save content