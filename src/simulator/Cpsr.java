package simulator;

/**
 * Current Program Status Register (CPSR)
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

	// TODO write javadoc comment
	/**
	 * 
	 */
	private boolean eq() {
		return z;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	private boolean ne() {
		return !z;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	private boolean cs() {
		return c;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	private boolean cc() {
		return !c;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	private boolean mi() {
		return n;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	private boolean pl() {
		return !n;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	private boolean vs() {
		return v;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	private boolean vc() {
		return !v;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	private boolean hi() {
		return (c == true) && (z == false);
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	private boolean ls() {
		return (c == false) || z;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	private boolean ge() {
		return v == n;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	private boolean lt() {
		return v != n;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	private boolean gt() {
		return (z == false) && (n == v);
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	private boolean le() {
		return z || (n != v);
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	private boolean al() {
		return true;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public boolean isN() {
		return n;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public void setN(boolean n) {
		this.n = n;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public boolean isZ() {
		return z;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public void setZ(boolean z) {
		this.z = z;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public boolean isC() {
		return c;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public void setC(boolean c) {
		this.c = c;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public boolean isV() {
		return v;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public void setV(boolean v) {
		this.v = v;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public boolean isQ() {
		return q;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public void setQ(boolean q) {
		this.q = q;
	}
}
