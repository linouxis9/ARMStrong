package projetarm_v2.simulator.boilerplate;

public class InvalidInstructionException extends RuntimeException {
	
	private static final long serialVersionUID = 5458289081653091341L;
	private final int line;

	public InvalidInstructionException(String message, int line) {
		super(message);
		this.line = line;
	}

	public int getLine() {
		return line;
	}

}
