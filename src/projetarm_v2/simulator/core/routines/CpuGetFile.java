package projetarm_v2.simulator.core.routines;

import projetarm_v2.simulator.core.Cpu;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CpuGetFile extends CpuRoutine
{
	public static final long ROUTINE_ADDRESS = 0xFF0CL;
	
	public CpuGetFile(Cpu cpu) { super(cpu); }
	
	public long getRoutineAddress() { return ROUTINE_ADDRESS; }
	
	@Override
	protected void primitive() {
		int i = 0;
		long address = (long) this.getRegister(0).getValue();
		long dest = (long) this.getRegister(1).getValue();
		
		try
		{
			System.out.println("Bonjour " + this.longToString(dest));
		}
		catch(UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		
		System.out.flush();
	}
}

