package simulator;

public class InvalidSyntaxException extends AssemblyException {

	private static final long serialVersionUID = -3576420336871736366L;
	private String error;

	public InvalidSyntaxException(int line) {
		super(line);
		this.error = "Syntax";
	}

	public InvalidSyntaxException(String error, int line) {
		super(line);
		this.error = error;
	}
	
	public String toString() {
		return "INVALID SYNTAX: " + error + " @ line " + line + " is not understood.";
	}
	
}