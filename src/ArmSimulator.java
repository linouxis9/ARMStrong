
import java.util.HashMap;
import java.util.Map;

import simulator.*;

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
	private final Map<Integer, Boolean> breakpoints;
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	public ArmSimulator() {	
		this.cpu = new Cpu();
		this.program = new Program();
		this.interpretor = new Interpretor(this.cpu, this.program);
		this.breakpoints = new HashMap<>();
	}

	/**
	 * 
	 * @param registerNumber
	 * @return
	 */
	public int getRegisterValue(int registerNumber) {
		return this.cpu.getRegisters(registerNumber).getValue();
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
	public void run() {
		this.cpu.execute();
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
		this.cpu.reset();
	}

	/**
	 * 
	 * @return
	 */
	public int getCurrentLine(){
		return this.cpu.getInstructions().get(((int) Math.ceil((double) this.cpu.getPc().getValue() / 4))).getLine()-1;
	}

	/**
	 * 
	 * @param line
	 */
	public void flipBreakPointStatus(int line){
		this.breakpoints.put(line, !this.getBreakPointStatus(line));
	}

	/**
	 * 
	 * @param line
	 * @return
	 */
	public boolean getBreakPointStatus(int line){
		if(!this.breakpoints.containsKey(line)) {
			return false;
		}
		return this.breakpoints.get(line);
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
}
