package simulateurARM;

public class Register implements Operand2 {
	private int register;

	public int getValue() {
		return register;
	}

	public void setValue(int register) {
		this.register = register;
	}


}
