package projetarm_v2.simulator.core.routines;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import projetarm_v2.simulator.core.Cpu;

public class CpuConsolePutString extends CpuRoutine {

	public static final long ROUTINE_ADDRESS = 0xFF04L;
	
	public CpuConsolePutString(Cpu cpu) { super(cpu); }
	
	public long getRoutineAddress() { return ROUTINE_ADDRESS; }

	@Override
	protected void primitive() {
		int i = 0;
		long address = (long) this.getRegister(0).getValue();
		
		try
		{
			System.out.println("[OUTPUT] " + this.longToString(address));
		}
		catch(UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		
		System.out.flush();
	}

}
