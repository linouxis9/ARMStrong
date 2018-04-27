package simulator;

public abstract class AssemblyException extends Exception {
	protected final int line;
	
	public AssemblyException(int line) {
		this.line = line;
	}
	
	public int getLine() {
		return this.line;
	}
}
