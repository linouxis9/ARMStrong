package projetarm_v2.simulator.core.routines;

import projetarm_v2.simulator.core.Cpu;
import projetarm_v2.simulator.core.Ram;
import projetarm_v2.simulator.core.Register;
import unicorn.CodeHook;
import unicorn.Unicorn;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public abstract class CpuRoutine {

	private Cpu cpu;

	public CpuRoutine(Cpu cpu) {
		this.cpu = cpu;
	}

	protected Cpu getCpu() {
		return this.cpu;
	}

	protected Ram getRam() {
		return this.cpu.getRam();
	}

	protected Register getRegister(int registerId) {
		return this.cpu.getRegister(registerId);
	}

	protected abstract void primitive();

	public abstract long getRoutineAddress();
	
	public CodeHook getNewHook() {
		return new RoutineHook(this);
	}

	public static boolean shouldBeManuallyAdded() {
		return false;
	}
	
	protected String longToString(long address) throws UnsupportedEncodingException
	{
		int i= 0;
		List<Byte> list = new ArrayList<>();
		byte c = this.getRam().getByte(address++);
		
		while (c != 0) {
			list.add(c);
			c = this.getRam().getByte(address++);
		}
		
		byte[] array = new byte[list.size()];
		
		for (Byte current : list) {
			array[i] = current;
			i++;
		}
	
		return new String(array, "UTF-8");
	
	}
	
	private class RoutineHook implements CodeHook {
		private final CpuRoutine cpuRoutine;

		public RoutineHook(CpuRoutine cpuRoutine) {
			this.cpuRoutine = cpuRoutine;
		}

		public void hook(Unicorn u, long address, int size, Object user_data) {
			if (address != this.cpuRoutine.getRoutineAddress()) {
				return;
			}
			
			System.out.println("[INFO] System call " + this.cpuRoutine.getClass().getSimpleName()
					+ " @ 0x" + Long.toHexString(this.cpuRoutine.getRoutineAddress()));
			
			this.cpuRoutine.primitive();
			
			this.cpuRoutine.getCpu().setCurrentAddress((long) this.cpuRoutine.getCpu().getRegister(14).getValue()-4);
		}

	}
}

