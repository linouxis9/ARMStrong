package simulator.core.exceptions;

import simulator.core.AssemblyException;

public class InvalidRegisterException extends AssemblyException {

	private static final long serialVersionUID = -1990042547606009095L;
	private final int register;
	
	public InvalidRegisterException(int line, int register) {
		super(line);
		this.register = register;
	}

	@Override
	public String toString() {
		return "INVALID REGISTER: Register r" + register + " @ line " + line + " should be between r[0-15].";
	}
}
