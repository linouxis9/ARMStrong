package projetarm_v2.simulator.core.routines;

import projetarm_v2.simulator.core.Cpu;

import java.util.ArrayList;
import java.util.List;

public class CpuConsoleGetString extends CpuRoutine
{
	public final static long ROUTINE_ADDRESS = 0xFF08L;
	
	public CpuConsoleGetString(Cpu cpu) {
		super(cpu);
	}
	
	public long getRoutineAddress() {
		return ROUTINE_ADDRESS;
	}
	
	@Override
	protected void primitive()
	{
		String searchString = "test";
		
		long address = (long) this.getRegister(0).getValue();
		
		byte[] c = searchString.getBytes();
		
		for(int i = 0; i < c.length ; i++)
		{
			this.getRam().setByte(address,c[i]);
			address++;
		}
	}
}
