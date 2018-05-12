
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

	// TODO write javadoc comment
	/**
	 * 
	 */
	public ArmSimulator() {
		this.cpu = new Cpu();
		this.program = new Program();
		this.interpretor = new Interpretor(this.cpu, this.program);
	}
	

}
