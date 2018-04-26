import simulator.Program;

public class Main {
	public static void main(String[] args) {
		ArmSimulator jpp = new ArmSimulator();
		System.out.println(Program.lexer(args[0]));
	}
}
