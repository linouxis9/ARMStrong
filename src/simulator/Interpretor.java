package simulator;

import java.util.Iterator;
import java.util.List;

public class Interpretor implements Iterator<Instruction> {

	// TODO write javadoc comment
	/**
	 * 
	 */
	private final Program program;
	private final Cpu cpu;
	private int line;

	// TODO write javadoc comment
	/**
	 * 
	 */
	public Interpretor(Cpu cpu, Program program) {
		this.program = program;
		this.cpu = cpu;
		this.line = 0;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public boolean hasNext() {
		return this.program.hasNext();
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public Instruction next() {
		if (!this.hasNext()) {
			return null;
		}
		this.line++;
		try {
			List<Token> tokens = this.program.next();
			SyntaxChecker.checkSyntax(tokens);
			return this.parse(tokens);
		} catch (Exception e) {
			return null;
		}
	}


	// TODO write javadoc comment
	/**
	 * 
	 */
	public Instruction parse(List<Token> tokens)
			throws InvalidSyntaxException, InvalidOperationException, InvalidRegisterException {
		return null;
	}
}
