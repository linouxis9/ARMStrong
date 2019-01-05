package projetarm_v2.simulator.core;

import unicorn.Unicorn;

/**
 * The Current Program Status Register is a 32-bit wide register used in the ARM
 * architecture to record various pieces of information regarding the state of
 * the program being executed by the processor and the state of the processor.
 * This information is recorded by setting or clearing specific bits in the
 * register.
 */
public class Cpsr extends UnicornRegister {

	public Cpsr(Unicorn u, int register) {
		super(u, register);
	}

	/**
	 * Equal.
	 */
	public boolean eq() {
		return this.z();
	}

	/**
	 * Not equal.
	 */
	public boolean ne() {
		return !this.z();
	}

	/**
	 * Unsigned higher or same (or carry set).
	 */
	public boolean cs() {
		return this.c();
	}

	/**
	 * Unsigned lower (or carry clear).
	 */
	public boolean cc() {
		return !this.c();
	}

	/**
	 * Negative.
	 */
	public boolean mi() {
		return this.n();
	}

	/**
	 * Positive or zero.
	 */
	public boolean pl() {
		return !this.n();
	}

	/**
	 * Signed overflow.
	 */
	public boolean vs() {
		return this.v();
	}

	/**
	 * No signed overflow.
	 */
	public boolean vc() {
		return !this.v();
	}

	/**
	 * Unsigned higher.
	 */
	public boolean hi() {
		return (this.c()) && (!this.z());
	}

	/**
	 * Unsigned lower or same.
	 */
	public boolean ls() {
		return (!this.c()) || this.z();
	}

	/**
	 * Signed greater than or equal.
	 */
	public boolean ge() {
		return this.v() == this.n();
	}

	/**
	 * Signed less than.
	 */
	public boolean lt() {
		return this.v() != this.n();
	}

	/**
	 * Signed greater than.
	 */
	public boolean gt() {
		return (this.z() == false) && (this.n() == this.v());
	}

	/**
	 * Signed less than or equal.
	 */
	public boolean le() {
		return this.z() || (this.n() != this.v());
	}

	/**
	 * Always executed.
	 */
	public boolean al() {
		return true;
	}

	/**
	 * Return the N flag from the CPSR
	 */
	public boolean n() {
		int res = ((this.getValue()>>31) & 0x1);
		return (res == 1);
	}

	/**
	 * Set the N flag in the CPSR
	 *
	 * @param n The value (1 or 0)
	 */
	public void setN(boolean n) {
		int res = booleanToInt(n);
		res = ((res<<31) & 0x80000000);
		int value = this.getValue() & 0x7fffffff;
		this.setValue(res | value);
		
	}

	/**
	 * Return the Z flag from the CPSR
	 */
	public boolean z() {
		int res = ((this.getValue()>>30) & 0x1);
		return res == 1;
	}

	/**
	 * Set the Z flag in the CPSR
	 *
	 * @param z The value (1 or 0)
	 */
	public void setZ(boolean z) {
		int res = booleanToInt(z);
		res = ((res<<30) & 0x40000000);
		int value = this.getValue() & 0xbfffffff;
		this.setValue(res | value);
	}

	/**
	 * Return the C flag from the CPSR
	 */
	public boolean c() {
		int res = ((this.getValue()>>29) & 0x1);
		return (res == 1);
	}

	/**
	 * Set the C flag in the CPSR
	 *
	 * @param c The value (1 or 0)
	 */
	public void setC(boolean c) {
		int res = booleanToInt(c);
		res = ((res<<29) & 0x20000000);
		int value = this.getValue() & 0xdfffffff;
		this.setValue(res | value);
	}

	/**
	 * Return the V flag from the CPSR
	 */
	public boolean v() {
		int res = ((this.getValue()>>28) & 0x1);
		return (res == 1);
	}

	/**
	 * Set the V flag in the CPSR
	 *
	 * @param v The value (1 or 0)
	 */
	public void setV(boolean v) {
		int res = booleanToInt(v);
		res = ((res<<28) & 0x10000000);
		int value = this.getValue() & 0xefffffff;
		this.setValue(res | value);
	}

	/**
	 * Return the Q flag from the CPSR
	 */
	public boolean q() {
		int res = ((this.getValue()>>27) & 0x1);
		return (res == 1);
	}

	/**
	 * Set the Q flag in the CPSR
	 *
	 * @param q The value (1 or 0)
	 */
	public void setQ(boolean q) {
		int res = booleanToInt(q);
		res = ((res<<27) & 0x8000000);
		int value = this.getValue() & 0x7ffffff;
		this.setValue(res | value);
	}

	private static int booleanToInt(boolean bool) {
		return bool ? 1 : 0;
	}
}