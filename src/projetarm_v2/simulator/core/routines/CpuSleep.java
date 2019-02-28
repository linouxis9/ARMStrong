package projetarm_v2.simulator.core.routines;


import projetarm_v2.simulator.core.Cpu;

public class CpuSleep extends CpuRoutine {

	public static final long ROUTINE_ADDRESS = 0xFF14L;
	
	public CpuSleep(Cpu cpu) {
		super(cpu);
	}
	
	public long getRoutineAddress() {
		return ROUTINE_ADDRESS;
	}
	
	@Override
	protected void primitive() {
		try {
			Thread.sleep(this.getRegister(0).getValue());
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

}
