package projetarm_v2.simulator.core;

import java.io.IOException;

import cz.adamh.utils.NativeUtils;
import unicorn.*;

public class Cpu {
	public static final int DEFAULT_STARTING_ADDRESS = 0x1000;

	static {
		try {
			NativeUtils.loadLibraryFromJar("/natives/libunicorn_java.so");
		} catch (IOException e) {
			System.out.println("Impossible de charger Unicorn... Utilisez-vous bien le jar adapté à votre OS?");
			System.exit(-1);
		}
	}

	private final Ram ram;
	private final Unicorn u;
	private final Register[] registers;
	private boolean running = false;
	private Cpsr cpsr;
	private Register pc;
	private boolean hasFinished = false;
	private long startingAddress;

	public Cpu() {
		this(new Ram(), Cpu.DEFAULT_STARTING_ADDRESS, 2 * 1024 * 1024);
	}

	public Cpu(Ram ram, long startingAddress, int ramSize) {
		this.ram = ram;
		this.startingAddress = startingAddress;

		u = new Unicorn(Unicorn.UC_ARCH_ARM, Unicorn.UC_MODE_ARM);

		this.registers = new Register[16];

		for (int i = 0; i < 13; i++) {
			this.registers[i] = new UnicornRegister(u, ArmConst.UC_ARM_REG_R0 + i);
		}

		this.registers[13] = new UnicornRegister(u, ArmConst.UC_ARM_REG_SP);
		this.registers[14] = new UnicornRegister(u, ArmConst.UC_ARM_REG_LR);

		// The Unicorn library doesn't provide a way to reliably get the program counter
		// register as reg_read always returns the initial PC value
		// As such, we decided to implement an hook which is executed when an
		// instruction is being interpreted
		// We set our internal PC's value to the address of the instruction currently
		// being executed
		this.registers[15] = new SimpleRegister(startingAddress);
		this.pc = this.registers[15];

		u.mem_map(0, ramSize, Unicorn.UC_PROT_ALL);

		this.cpsr = new Cpsr(u, ArmConst.UC_ARM_REG_CPSR);

		this.synchronizeUnicornRam();

		u.hook_add(ram.getNewReadHook(), 1, 0, null);

		u.hook_add(ram.getNewWriteHook(), 1, 0, null);

		u.hook_add(new CPUInstructionHook(this), 1, 0, null);
	}

	private void synchronizeUnicornRam() {
		for (RamChunk chunk : this.ram.getRamChunks()) {
			u.mem_write(chunk.startingAddress, chunk.getChunk());
		}
	}

	public boolean isRunning() {
		return this.running;
	}

	// Ou tout d'un coup!
	public void runAllAtOnce() throws UnicornException {
		this.synchronizeUnicornRam();
		
		running = true;
		hasFinished = false;
		
		u.emu_start(this.pc.getValue(), 0, 0, 0);
		
		running = false;
		hasFinished = true;
	}

	public Register getRegister(int registerNumber) {
		return this.registers[registerNumber];
	}

	public Ram getRam() {
		return this.ram;
	}

	public boolean hasFinished() {
		return this.hasFinished;
	}

	public long getStartingAddress() {
		return this.startingAddress;
	}

	public void runStep() throws UnicornException {
		this.synchronizeUnicornRam();
		
		running = true;
		hasFinished = false;

		u.emu_start(this.pc.getValue(), this.pc.getValue() + 4, 0, 0);
		this.pc.setValue(this.pc.getValue() + 4);
		
		running = false;
	}

	private class CPUInstructionHook implements CodeHook {
		private final Cpu cpu;

		public CPUInstructionHook(Cpu cpu) {
			this.cpu = cpu;
		}

		public void hook(Unicorn u, long address, int size, Object user_data) {
			this.cpu.pc.setValue(address);

			int a = 0;
			byte instruction[] = u.mem_read(address, size);
			for (int i = 0; i < size; i++) {
				a += instruction[i];
			}
			if (a == 0) {
				u.emu_stop();
				this.cpu.hasFinished = true;
				this.cpu.running = false;
				return;
			}

			System.out.format(">>> Instruction @ 0x%x is being executed\n", this.cpu.pc.getValue());

			// TODO Un petit sleep pour avoir le temps de voir ce qu'il se passe pendant
			// l'exécution pour le débogage
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public void interruptMe() {
		this.u.emu_stop();
	}

	public Cpsr getCPSR() {
		return this.cpsr;
	}

}