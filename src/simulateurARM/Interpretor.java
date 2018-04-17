package simulateurARM;
import java.util.Iterator;

public class Interpretor implements Iterator<Instruction> {
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	private final Program program;
	private final Cpu cpu;

	// TODO write javadoc comment
	/**
	 * 
	 */
	public Interpretor(Cpu cpu, Program program) {
		this.program = program;
		this.cpu = cpu;
	}
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	public boolean hasNext() {
		return true;
	}
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	public Instruction next() {
		return null;
	}
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	public void remove () {
		
	}
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	public static void checkSyntax(String line) throws InvalidSyntaxException, InvalidOperationException, InvalidRegisterException {
		
	}
}
