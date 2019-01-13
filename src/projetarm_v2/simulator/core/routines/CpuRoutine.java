package projetarm_v2.simulator.core.routines;

import projetarm_v2.simulator.core.Cpu;
import projetarm_v2.simulator.core.Ram;
import projetarm_v2.simulator.core.Register;
import unicorn.CodeHook;
import unicorn.Unicorn;

import java.io.IOException;

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

	protected abstract void primitive() throws IOException;

	public abstract long getRoutineAddress();

	public CodeHook getNewHook() {
		return new RoutineHook(this);
	}

	public static boolean shouldBeManuallyAdded() {
		return false;
	}
	
	private class RoutineHook implements CodeHook {
		private final CpuRoutine cpuRoutine;

		public RoutineHook(CpuRoutine cpuRoutine) {
			this.cpuRoutine = cpuRoutine;
		}

		public void hook(Unicorn u, long address, int size, Object user_data) {
			if (this.cpuRoutine.getRoutineAddress() != address) {
				return;
			}
			
			try
			{
				this.cpuRoutine.primitive();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}

	}
}
