package simulator;

public class InvalidSyntaxException extends AssemblyException {

	public InvalidSyntaxException(int line) {
		super(line);
	}

	public String toString() {
		return "INVALID SYNTAX: Syntax @ line " + line + " is not understood.";
	}
	
}