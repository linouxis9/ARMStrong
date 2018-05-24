package simulator.core;

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
		int value = this.register.getValue();
		switch(this.shift) {
			case LSL:
				value = value << this.controlRegister.getValue();
				break;
			case LSR:
				value = value >> this.controlRegister.getValue();
				break;
		}
		return value;
	}

	public boolean getCarry() {
		return true;
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
