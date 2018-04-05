package simulateurARM;

import java.util.Iterator;

public class Interpretor implements Iterator<Instruction> {
	private final Program program;
	private final Cpu cpu;

	public Interpretor(Cpu cpu, Program program) {
		this.program = program;
		this.cpu = cpu;
	}
	
	public boolean hasNext() {
		return true;
	}
	
	public Instruction next() {
		return null;
	}
	
	public void remove () {
		
	}
	
	public static void checkSyntax(String line) throws InvalidSyntaxException, InvalidOperationException, InvalidRegisterException {
		
	}
}
