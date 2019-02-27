package projetarm_v2.simulator.ui.javafx.ramview;

public class LineRamByte implements LineRam {
	private byte a;
	private byte b;
	private byte c;
	private byte d;
	
	private OutputType type;
	
	public LineRamByte(byte byte1, byte byte2, byte byte3, byte byte4, OutputType type) {
		this.a = byte1;
		this.b = byte2;
		this.c = byte3;
		this.d = byte4;
		this.type = type;
	}
	
	public String getA() {
		return asString(a);
	}
	private String asString(byte a2) {
		switch (this.type) {
			default:
			case NORMAL:
				return Integer.toString(((int)a2) & 0xFF);
			case HEX:
				return String.format("%x", a2);
		}
	}

	public void setA(String a) {
	}
	public String getB() {
		return asString(b);
	}
	public void setB(String b) {
	}
	public String getC() {
		return asString(c);
	}
	public void setC(String c) {
	}
	public String getD() {
		return asString(d);
	}
	public void setD(String d) {
	}
	
}
