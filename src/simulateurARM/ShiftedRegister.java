package simulateurARM;

public class ShiftedRegister implements Operand2 {
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	private Register register;
	private Operand2 controlRegister;
	private Shift shift;
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	public int getValue() {
		return register.getValue(); /* TODO SHIFTING */
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public ShiftedRegister(Register register, Shift shift, Operand2 controlRegister) {
		this.register = register;
		this.shift = shift;
		this.controlRegister = controlRegister;
	}
	
}
