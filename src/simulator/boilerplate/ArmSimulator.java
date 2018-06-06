package simulator.boilerplate;

import java.util.HashMap;
import java.util.Map;

import simulator.core.*;
import simulator.core.exceptions.InvalidDirectiveException;
import simulator.core.exceptions.InvalidLabelException;
import simulator.core.exceptions.InvalidOperationException;
import simulator.core.exceptions.InvalidRegisterException;
import simulator.core.exceptions.InvalidSyntaxException;
import simulator.core.exceptions.UnknownLabelException;

/**
 * ArmSimulator is class responsible for handling the creation of the main ARM Simulator classes.
 */
public class ArmSimulator {

	// TODO write javadoc comment
	/**
	 * 
	 */
	private final Program program;
	private final Interpretor interpretor;
	private final Cpu cpu;
	private final Map<Instruction, Integer> linesMap;

	// TODO write javadoc comment
	/**
	 * 
	 */
	public ArmSimulator() {	
		this.cpu = new Cpu();
		this.program = new Program();
		this.linesMap = new HashMap<>();
		this.interpretor = new Interpretor(this.cpu, this.program, this.linesMap);
	}

	/**
	 * 
	 * @param registerNumber
	 * @return
	 */
	public int getRegisterValue(int registerNumber) {
		return this.cpu.getRegister(registerNumber).getValue();
	}

	/**
	 * 
	 * @param address
	 * @return
	 */
	public byte getRamByte(int address) {
		return this.cpu.getRam().getByte(new Address(address));
	}

	/**
	 * 
	 * @param address
	 * @return
	 */
	public short getRamHWord(int address) {
		return this.cpu.getRam().getHWord(new Address(address));
	}

	/**
	 * 
	 * @param address
	 * @return
	 */
	public int getRamWord(int address) {
		return this.cpu.getRam().getValue(new Address(address));
	}

	/**
	 * 
	 */
	public boolean run() {
		this.cpu.execute();
		return this.isHalted();
	}

	/**
	 * 
	 */
	public void runStep(){
		this.cpu.executeStep();
	}

	/**
	 * 
	 */
	public void resetRun(){
		this.linesMap.clear();
		this.cpu.reset();
	}

	/**
	 * 
	 * @return
	 */
	public int getCurrentLine(){
		return linesMap.get(this.cpu.getInstructions().get(((int) Math.ceil((double) this.cpu.getPc().getValue() / 4))))-1	;
	}

	public boolean getN() {
		return this.cpu.getCPSR().isN();
	}

	public boolean getZ() {
		return this.cpu.getCPSR().isZ();
	}
	
	public boolean getC() {
		return this.cpu.getCPSR().isC();
	}
	
	public boolean getV() {
		return this.cpu.getCPSR().isV();
	}
	/**
	 * 
	 * @param programAsString
	 * @throws InvalidSyntaxException
	 * @throws InvalidOperationException
	 * @throws InvalidRegisterException
	 * @throws InvalidLabelException
	 * @throws UnknownLabelException
	 * @throws InvalidDirectiveException 
	 */
	public void setProgramString(String programAsString) throws InvalidSyntaxException, InvalidOperationException, InvalidRegisterException, InvalidLabelException, UnknownLabelException, InvalidDirectiveException{
		this.resetRun();
		this.program.setNewProgram(programAsString);
		this.interpretor.parseProgram();
	}
	
	public boolean hasFinished() {
		return this.cpu.hasFinished();
	}
	
	public boolean isInterrupted() {
		return this.cpu.isInterrupted();
	}
	
	public boolean isBreakpoint() {
		return this.cpu.isBreakpoint();
	}
	
	public boolean isHalted() {
		return this.hasFinished() || this.isBreakpoint() || this.isInterrupted();
	}
	
	public void interruptExecutionFlow(boolean interrupt) {
		this.cpu.interruptMe(interrupt);
	}
}
