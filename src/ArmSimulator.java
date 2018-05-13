
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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

	public int getRegisterValue(int registerNumber) {

	}

	public byte getRamByte(int address) {

	}

	public short getRamHWord(int address) {

	}

	public int getRamWord(int address) {

	}

	public void run() {

	}

	public void runStep(){

	}

	public void resetRun(){

	}

	public int getCurrentLine(){

	}

	public void flipBreakPointStatus(int line){

	}

	public boolean getBreakPointStatus(int line){

	}

	public void setProgramString(String programAsString) throws InvalidSyntaxException, InvalidOperationException, InvalidRegisterException, InvalidLabelException, UnknownLabelException{

	}
}
