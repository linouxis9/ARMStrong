package simulator.core.exceptions;

public class Bug extends Error {
	public Bug(String string) {
		super(string);
	}
	
	@Override
	public String toString() {
		return "[BUG?] " + this.getMessage();
	}
}
