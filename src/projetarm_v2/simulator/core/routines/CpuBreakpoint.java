package projetarm_v2.simulator.core.routines;


import projetarm_v2.simulator.core.Cpu;

public class CpuBreakpoint extends CpuRoutine {

	public static final long ROUTINE_ADDRESS = 0xFF18L;
	
	public CpuBreakpoint(Cpu cpu) {
		super(cpu);
	}
	
	public long getRoutineAddress() {
		return ROUTINE_ADDRESS;
	}
	
	@Override
	protected void primitive() {
		this.getCpu().interruptMe();
	}

}
