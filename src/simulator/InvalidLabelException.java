package simulator;

public class InvalidLabelException extends AssemblyException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1239647656703191214L;
	private final String label;
	
	public InvalidLabelException(int line, String label) {
		super(line);
		this.label = label;
	}

	public String toString() {
		return "INVALID LABEL: " + label + " @ line " + line + " already exists.";
	}
}
