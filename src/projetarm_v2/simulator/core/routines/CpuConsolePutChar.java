package projetarm_v2.simulator.core.routines;


import projetarm_v2.simulator.core.Cpu;

public class CpuConsolePutChar extends CpuRoutine {

	public static final long ROUTINE_ADDRESS = 0xFF00L;
	
	public CpuConsolePutChar(Cpu cpu) {
		super(cpu);
	}
	
	public long getRoutineAddress() {
		return ROUTINE_ADDRESS;
	}
	
	@Override
	protected void primitive() {
		long address = (long)this.getRegister(0).getValue();
		byte c = this.getRam().getByte(address);

		System.out.format("[OUTPUT] %s%n",Character.toString((char) c));
	}

}
