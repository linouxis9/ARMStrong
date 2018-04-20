package simulateurARM;


public class Cpsr {

	// TODO write javadoc comment
	/**
	 * 
	 */
	private boolean n;
	private boolean z;
	private boolean c;
	private boolean v;
	private boolean q;
	private boolean e;
	private boolean endianness;
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	public Cpsr() {
		this.reset();
		this.endianness = false;
	}
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	public int getCpsr() {
		int cpsr = booleanToInt(n)*(2 << 30) + booleanToInt(z)*(2 << 29) + booleanToInt(c)*(2 << 28) + booleanToInt(v)*(2 << 27) + booleanToInt(q)*(2 << 26) + booleanToInt(e)*(2 << 25) + booleanToInt(endianness)*(2 << 8);
		return cpsr;
		
	}
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	public int booleanToInt(boolean bool) {
		return (bool) ? 1 : 0;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public void reset() {
		this.n = false;
		this.z = false;
		this.c = false;
		this.v = false;
		this.q = false;
		this.e = false;
	}
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	public boolean getConditionCodeStatus(ConditionCode cc) {
		switch (cc) {
			case EQ: return eq();
			case NE: return ne();
			case CS: return cs();
			case CC: return cc();
			case MI: return mi();
			case PL: return pl();
			case VS: return vs();
			case VC: return vc();
			case HI: return hi();
			case LS: return ls();
			case GE: return ge();
			case GT: return gt();
			case LT: return lt();
			case LE: return le();
			default: return al();
		}
		
	}
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	private boolean eq() {
		return z==true;
	}
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	private boolean ne() {
		return z==false;
	}
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	private boolean cs() {
		return c==true;
	}
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	private boolean cc() {
		return c==false;
	}	
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	private boolean mi() {
		return n==true;
	}	
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	private boolean pl() {
		return z==false;
	}	
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	private boolean vs() {
		return v==true;
	}	
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	private boolean vc() {
		return v==false;
	}	
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	private boolean hi() {
		return (c==true) && (z==false);
	}	
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	private boolean ls() {
		return (c==false) || (z==true);
	}
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	private boolean ge() {
		return v==n;
	}	
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	private boolean lt() {
		return v!=n;
	}
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	private boolean gt() {
		return (z==false) && (n==v);
	}	
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	private boolean le() {
		return (z==true) || (n!=v);
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
	public boolean getEndianness() {
		return endianness;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public void setEndianness(boolean endianness) {
		this.endianness = endianness;
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

	// TODO write javadoc comment
	/**
	 * 
	 */
	public boolean isE() {
		return e;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public void setE(boolean e) {
		this.e = e;
	}
}
