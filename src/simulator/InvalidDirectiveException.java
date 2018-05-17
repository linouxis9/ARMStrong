package simulator;

public class InvalidDirectiveException extends AssemblyException {

	private final String directive;
	public InvalidDirectiveException(int line, String directive) {
		super(line);
		this.directive = directive;
	}

	public String toString() {
		return "INVALID DIRECTIVE: " + directive + " @ line " + line + " is unknown.";
	}
}
