package simulator;

public class InvalidOperationException extends AssemblyException {

	private static final long serialVersionUID = 8364730117849795976L;
	private final String op;
	
	public InvalidOperationException(int line, String op) {
		super(line);
		this.op = op;
	}

	@Override
	public String toString() {
		return "INVALID OPERATION: "+ op + " @ line " + line + " is unknown.";
	}
}
