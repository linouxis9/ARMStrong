package simulator;

public abstract class AssemblyException extends Exception {

	private static final long serialVersionUID = -2485352858328151954L;
	protected final int line;
	
	public AssemblyException(int line) {
		this.line = line;
	}
	
	public int getLine() {
		return this.line;
	}
}
