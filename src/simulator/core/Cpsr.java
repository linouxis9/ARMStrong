package simulator.core;

/**
 * The Current Program Status Register is a 32-bit wide register used in the ARM architecture to record
 * various pieces of information regarding the state of the program being executed by the processor and the
 * state of the processor. This information is recorded by setting or clearing specific bits in the register.
 */
public class Cpsr {

	/**
	 * Negative condition code flag
	 */
	private boolean n;

	/**
	 * Zero condition code flag
	 */
	private boolean z;

	/**
	 * Carry condition code flag
	 */
	private boolean c;

	/**
	 * Overflow condition code flag
	 */
	private boolean v;

	/**
	 * Cumulative saturation bit
	 */
	private boolean q;

	/**
	 * Returns a Cpsr intialized and zeroed.
	 */
	public Cpsr() {
		this.reset();
	}

	/**
	 * Converts a Cpsr instance into its proper Integer representation.
	 */
	public int getCpsr() {
		return booleanToInt(n) * (2 << 30) + booleanToInt(z) * (2 << 29) + booleanToInt(c) * (2 << 28)
				+ booleanToInt(v) * (2 << 27) + booleanToInt(q) * (2 << 26);

	}

	// TODO To move
	/**
	 * Converts a bool into an int.
	 * 
	 * @param bool The boolean to convert.
	 */
	int booleanToInt(boolean bool) {
		return (bool) ? 1 : 0;
	}

	/**
	 * Zeroes the Cpsr.
	 */
	public void reset() {
		this.n = false;
		this.z = false;
		this.c = false;
		this.v = false;
		this.q = false;
	}

	/**
	 * Returns if according to the current Cpsr's statut the cc ConditionCode
	 * evaluates to true or not.
	 * 
	 * @param cc The ConditionCode to evaluate.
	 */
	public boolean getConditionCodeStatus(ConditionCode cc) {
		switch (cc) {
		case EQ:
			return eq();
		case NE:
			return ne();
		case CS:
			return cs();
		case CC:
			return cc();
		case MI:
			return mi();
		case PL:
			return pl();
		case VS:
			return vs();
		case VC:
			return vc();
		case HI:
			return hi();
		case LS:
			return ls();
		case GE:
			return ge();
		case GT:
			return gt();
		case LT:
			return lt();
		case LE:
			return le();
		default:
			return al();
		}

	}

	/**
	 * Equal.
	 */
	private boolean eq() {
		return z;
	}

	/**
	 * Not equal.
	 */
	private boolean ne() {
		return !z;
	}

	/**
	 * Unsigned higher or same (or carry set).
	 */
	private boolean cs() {
		return c;
	}

	/**
	 * Unsigned lower (or carry clear).
	 */
	private boolean cc() {
		return !c;
	}

	/**
	 * Negative.
	 */
	private boolean mi() {
		return n;
	}

	/**
	 * Positive or zero.
	 */
	private boolean pl() {
		return !n;
	}

	/**
	 * Signed overflow.
	 */
	private boolean vs() {
		return v;
	}

	/**
	 * No signed overflow.
	 */
	private boolean vc() {
		return !v;
	}

	/**
	 * Unsigned higher.
	 */
	private boolean hi() {
		return (c == true) && (z == false);
	}

	/**
	 * Unsigned lower or same.
	 */
	private boolean ls() {
		return (c == false) || z;
	}

	/**
	 * Signed greater than or equal.
	 */
	private boolean ge() {
		return v == n;
	}

	/**
	 * Signed less than.
	 */
	private boolean lt() {
		return v != n;
	}

	/**
	 * Signed greater than.
	 */
	private boolean gt() {
		return (z == false) && (n == v);
	}

	/**
	 * Signed less than or equal.
	 */
	private boolean le() {
		return z || (n != v);
	}

	/**
	 * Always executed.
	 */
	private boolean al() {
		return true;
	}
	

	/**
	 * Return the N flag from the CPSR
	 */
	public boolean isN() {
		return n;
	}

	/**
	 * Set the N flag in the CPSR
	 *
	 * @param n The value (1 or 0)
	 */
	public void setN(boolean n) {
		this.n = n;
	}

	/**
	 * Return the Z flag from the CPSR
	 */
	public boolean isZ() {
		return z;
	}

	/**
	 * Set the Z flag in the CPSR
	 *
	 * @param z The value (1 or 0)
	 */
	public void setZ(boolean z) {
		this.z = z;
	}

	/**
	 * Return the C flag from the CPSR
	 */
	public boolean isC() {
		return c;
	}

	/**
	 * Set the C flag in the CPSR
	 *
	 * @param c The value (1 or 0)
	 */
	public void setC(boolean c) {
		this.c = c;
	}

	/**
	 * Return the V flag from the CPSR
	 */
	public boolean isV() {
		return v;
	}

	/**
	 * Set the V flag in the CPSR
	 *
	 * @param v The value (1 or 0)
	 */
	public void setV(boolean v) {
		this.v = v;
	}

	/**
	 * Return the Q flag from the CPSR
	 */
	public boolean isQ() {
		return q;
	}

	/**
	 * Set the Q flag in the CPSR
	 *
	 * @param q The value (1 or 0)
	 */
	public void setQ(boolean q) {
		this.q = q;
	}
}
