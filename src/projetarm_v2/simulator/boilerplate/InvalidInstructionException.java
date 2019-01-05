package projetarm_v2.simulator.boilerplate;

public class InvalidInstructionException extends RuntimeException {
	private final int line;

	public InvalidInstructionException(String message, int line) {
		super(message);
		this.line = line;
	}

	public int getLine() {
		return line;
	}

}
