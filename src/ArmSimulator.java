
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
	private Program program;
	private final Interpretor interpretor;
	private final Cpu cpu;

	// TODO write javadoc comment
	/**
	 * 
	 */
	public ArmSimulator() {
		this.cpu = new Cpu();
		this.program = new Program();
		this.interpretor = new Interpretor(this.cpu, this.program);
	}

	public int getRegisterValue(int registerNumber) {
		return 0;
	}

	public byte getRamByte(int Address) {
		return 0;
	}

	public short getRamHWord(int Address) {
		return 0;
	}

	public int getRamWord(int Address) {
		return 0;
	}

	public void run() {
	}

	public void runStep(){

	}

	public void runToNextBreakPoint(){

	}

	public void resetRun(){

	}

	public int getCurrentLine(){

	}

	public void changeBreakPointStatus(int line){

	}

	public boolean getBreakPointStatus(int line){

	}

	public void userConsoleInput(String command){

	}

	public void setProgramString(String programAsString){
		
	}
}
