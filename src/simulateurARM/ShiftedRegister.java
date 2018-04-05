package simulateurARM;

public class ShiftedRegister implements Operand2 {
	private Register register;
	private Operand2 controlRegister;
	private Shift shift;
	
	public int getValue() {
		return register.getValue(); /* TODO SHIFTING */
	}

	public ShiftedRegister(Register register, Shift shift, Operand2 controlRegister) {
		this.register = register;
		this.shift = shift;
		this.controlRegister = controlRegister;
	}
	
}
