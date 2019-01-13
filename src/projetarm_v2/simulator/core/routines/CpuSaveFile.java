package projetarm_v2.simulator.core.routines;

import projetarm_v2.simulator.core.Cpu;

public class CpuSaveFile extends CpuRoutine
{
	public static final long ROUTINE_ADDRESS = 0xFF10L;
	
	public CpuSaveFile(Cpu cpu) {
		super(cpu);
	}
	
	@Override
	public long getRoutineAddress() {
		return ROUTINE_ADDRESS;
	}
	
	@Override
	protected void primitive()
	{
	
	}
}
