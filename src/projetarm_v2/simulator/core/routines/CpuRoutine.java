package projetarm_v2.simulator.core.routines;

import projetarm_v2.simulator.core.Assembler;
import projetarm_v2.simulator.core.Cpu;
import projetarm_v2.simulator.core.Ram;
import projetarm_v2.simulator.core.Register;
import unicorn.CodeHook;
import unicorn.Unicorn;

public abstract class CpuRoutine {

	private Cpu cpu;
	private final long routineAddress;

	public CpuRoutine(Cpu cpu, long routineAddress) {
		this.cpu = cpu;
		this.routineAddress = routineAddress;
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

	private void runPrimitive() {
		if (this.routineAddress != this.getRegister(15).getValue()) {
			return;
		}
		
		primitive();
	}

	public CodeHook getNewHook() {
		return new RoutineHook(this);
	}

	private class RoutineHook implements CodeHook {
		private final CpuRoutine cpuRoutine;

		public RoutineHook(CpuRoutine cpuRoutine) {
			this.cpuRoutine = cpuRoutine;
		}

		public void hook(Unicorn u, long address, int size, Object user_data) {
			this.cpuRoutine.runPrimitive();
		}

	}
}
