
import simulator.*;

public class ArmSimulator {

	// TODO write javadoc comment
	/**
	 * 
	 */
	private final Program program;
	private final Interpretor interpretor;
	private final GUI gui;
	private final Cpu cpu;

	// TODO write javadoc comment
	/**
	 * 
	 */
	public ArmSimulator() {
		this.program = new Program(new String());
		this.cpu = new Cpu();
		this.interpretor = new Interpretor(this.cpu, this.program);
		this.gui = new GUI();
	}

}
