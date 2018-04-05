package simulateurARM;


public class Cpsr {

	private boolean n;
	private boolean z;
	private boolean c;
	private boolean v;
	private boolean q;
	private boolean e;
	private boolean endianness;
	
	public Cpsr() {
		this.reset();
		this.endianness = false;
	}
	
	public int getCpsr() {
		int cpsr = booleanToInt(n)*(2 << 30) + booleanToInt(z)*(2 << 29) + booleanToInt(c)*(2 << 28) + booleanToInt(v)*(2 << 27) + booleanToInt(q)*(2 << 26) + booleanToInt(e)*(2 << 25) + booleanToInt(endianness)*(2 << 8);
		return cpsr;
		
	}
	
	public int booleanToInt(boolean bool) {
		return (bool) ? 1 : 0;
	}

	public void reset() {
		this.n = false;
		this.z = false;
		this.c = false;
		this.v = false;
		this.q = false;
		this.e = false;
	}
	
	public boolean getConditonCodeStatus(ConditionCode cc) {
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
	private boolean eq() {
		return z==true;
	}
	private boolean ne() {
		return z==false;
	}
	private boolean cs() {
		return c==true;
	}
	private boolean cc() {
		return c==false;
	}	
	private boolean mi() {
		return n==true;
	}	
	private boolean pl() {
		return z==false;
	}	
	private boolean vs() {
		return v==true;
	}	
	private boolean vc() {
		return v==false;
	}	
	private boolean hi() {
		return (c==true) && (z==false);
	}	
	private boolean ls() {
		return (c==false) || (z==true);
	}
	private boolean ge() {
		return v==n;
	}	
	private boolean lt() {
		return v!=n;
	}
	private boolean gt() {
		return (z==false) && (n==v);
	}	
	private boolean le() {
		return (z==true) || (n!=v);
	}
	private boolean al() {
		return true;
	}

	public boolean getEndianness() {
		return endianness;
	}

	public void setEndianness(boolean endianness) {
		this.endianness = endianness;
	}

	public boolean isN() {
		return n;
	}

	public void setN(boolean n) {
		this.n = n;
	}

	public boolean isZ() {
		return z;
	}

	public void setZ(boolean z) {
		this.z = z;
	}

	public boolean isC() {
		return c;
	}

	public void setC(boolean c) {
		this.c = c;
	}

	public boolean isV() {
		return v;
	}

	public void setV(boolean v) {
		this.v = v;
	}

	public boolean isQ() {
		return q;
	}

	public void setQ(boolean q) {
		this.q = q;
	}

	public boolean isE() {
		return e;
	}

	public void setE(boolean e) {
		this.e = e;
	}
	
	
}
