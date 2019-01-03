package projetarm_v2.simulator.boilerplate;

import projetarm_v2.simulator.core.Assembler;
import projetarm_v2.simulator.core.Cpu;
import projetarm_v2.simulator.core.Program;
import projetarm_v2.simulator.core.Ram;

/**
 * ArmSimulator is class responsible for handling the creation of the main ARM Simulator classes.
 */
public class ArmSimulator {
    /**
     * The current loaded program
     */
	private final Program program;

	private final Assembler assembler;
    /**
     * The cpu to execute the prorgam
     */
	private Cpu cpu;

	private Ram ram;

    /**
     * Creates a arm simulator ready to use, with all the needed components (cpu, program, linesMap, interpretor)
     */
	public ArmSimulator() {	
		this.assembler = Assembler.getInstance();
		this.program = new Program();
		this.ram = new Ram();
		this.cpu = new Cpu(ram);
	}

	public void setProgram(String assembly) {
		byte[] binary = this.assembler.assemble(assembly);
		for (int i = 0; i < binary.length; i++) {
			this.ram.setByte(this.cpu.getStartingAddress()+i, binary[i]);
		}
	}
	
    /**
     * Returns the register value corresponding to the given number
     */
	public int getRegisterValue(int registerNumber) {
		return this.cpu.getRegister(registerNumber).getValue();
	}

	public Ram getRam() {
		return this.ram;
	}
	
	public Cpu getCpu() {
		return this.cpu;
	}
	
    /**
     * Returns a byte(8bits) from the ram corresponding to the given address
     */
	public byte getRamByte(long address) {
		return this.ram.getByte(address);
	}

    /**
     * Returns a half-word(16bits) from the ram corresponding to the given address
     */
	public short getRamHWord(long address) {
		return this.ram.getHWord(address);
	}
	
    /**
     * Returns a word(32bits) from the ram corresponding to the given address
     */
	public int getRamWord(long address) {
		return this.ram.getValue(address);
	}

    /**
     * Starting the processor to the next break or to the end
     */
	public void run() {
		this.cpu.runAllAtOnce();
	}

    /**
     * Staring the processor to execute a single instruction
     */
	public void runStep(){
		this.cpu.runStep();
	}

    /**
     * Resets the execution (clears the current execution point)
     */
	public void resetRun(){
		this.cpu.getRegister(15).setValue(this.cpu.getStartingAddress());
	}

	public void resetState() {
		this.ram = new Ram();
		this.cpu = new Cpu(ram);
	}
	
    /**
     * Returns the Negative Flag status
     */
	public boolean getN() {
		return this.cpu.getCPSR().n();
	}

    /**
     * Returns the Zero Flag status
     */
	public boolean getZ() {
		return this.cpu.getCPSR().z();
	}

    /**
     * Returns the Carry Flag status
     */
	public boolean getC() {
		return this.cpu.getCPSR().c();
	}

    /**
     * Returns the oVerflow Flag status
     */
	public boolean getV() {
		return this.cpu.getCPSR().v();
	}
	
	public boolean getQ() {
		return this.cpu.getCPSR().q();
	}

    /**
     * Returns true if the cpu is halted
     */
	public boolean isRunning() {
		return this.cpu.isRunning() && !this.cpu.hasFinished();
	}

	public boolean hasFinished() {
		return this.cpu.hasFinished();
	}
	
	/**
     * Stops the execution
     */
	public void interruptExecutionFlow() {
		this.cpu.interruptMe();
	}
}
