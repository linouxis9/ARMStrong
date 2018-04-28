package simulator;

public class InvalidSyntaxException extends AssemblyException {

	private static final long serialVersionUID = -3576420336871736366L;

	public InvalidSyntaxException(int line) {
		super(line);
	}

	public String toString() {
		return "INVALID SYNTAX: Syntax @ line " + line + " is not understood.";
	}
	
}