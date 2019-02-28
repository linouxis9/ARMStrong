package projetarm_v2.simulator.core.routines;

import projetarm_v2.simulator.core.Cpu;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CpuPutFile extends CpuRoutine
{
	public static final long ROUTINE_ADDRESS = 0xFF0CL;
	
	public CpuPutFile(Cpu cpu) { super(cpu); }
	
	public long getRoutineAddress() { return ROUTINE_ADDRESS; }
	
	@Override
	protected void primitive() {
		long address = (long) this.getRegister(0).getValue();
		long dest = (long) this.getRegister(1).getValue();
		
		try {
			File file = new File(this.longToString(dest));
			FileWriter fileWriter = new FileWriter(file);
			String str;
			
			str = this.longToString(address);
			fileWriter.write(str);
			fileWriter.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}

