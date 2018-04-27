package simulator;

public class InvalidRegisterException extends AssemblyException {
	private final int register;
	
	public InvalidRegisterException(int line, int register) {
		super(line);
		this.register = register;
	}

	public String toString() {
		return "INVALID REGISTER: Register r" + register + " @ line " + line + " should be between r[0-15].";
	}
}
