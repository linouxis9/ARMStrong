package projetarm_v2.simulator.core;

import unicorn.Unicorn;

public class UnicornRegister extends Register {
	private final Unicorn u;
	private int register;
	
	public UnicornRegister(Unicorn u, int register) {
		this.u = u;
		this.register = register;
	}
	
	/* (non-Javadoc)
	 * @see projetarm_v2.Registers#getValue()
	 */
	@Override
	public int getValue() {
		return ((Long)u.reg_read(this.register)).intValue();
	}
	
	/* (non-Javadoc)
	 * @see projetarm_v2.Registers#setValue(long)
	 */
	@Override
	public void setValue(int value) {
		u.reg_write(this.register, value);
	}
}
