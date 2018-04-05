package simulateurARM;

public class ArmSimulator {
	private final Program program;
	private final Interpretor interpretor;
	private final IHMEditor editor;
	private final IHMSimulator simulator;
	private final Cpu cpu;
	public ArmSimulator() {
		this.program = new Program(new String());
		this.cpu = new Cpu();
		this.interpretor = new Interpretor(this.cpu,this.program);
		this.editor = new IHMEditor();
		this.simulator = new IHMSimulator();
	}

}
