package projetarm_v2.simulator.core.routines;

import projetarm_v2.simulator.core.Cpu;

import java.io.*;

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
			String fileName = this.longToString(address);
			File file = new File(fileName);
			FileWriter fileWriter = new FileWriter(file);

			System.out.println("[INFO] Writing " + fileName + " from 0x" + Long.toHexString(dest));

			String str = this.longToString(dest);
			fileWriter.write(str);
			fileWriter.close();
			
		} catch(Exception e) {
			System.out.println("[ERROR] Writing" + e.getMessage());
		}
	}
}

