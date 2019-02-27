package projetarm_v2.simulator.ui.javafx.ramview;

public class LineRamHWord implements LineRam {
	private short a;
	private short b;
	private short c;
	private short d;
	
	private OutputType type;
	
	public LineRamHWord(short byte1, short byte2, short byte3, short byte4, OutputType type) {
		this.a = byte1;
		this.b = byte2;
		this.c = byte3;
		this.d = byte4;
		this.type = type;
	}
	
	public String getA() {
		return asString(a);
	}
	private String asString(short a2) {
		switch (this.type) {
			default:
			case NORMAL:
				return Integer.toString(((int)a2) & 0xFFFF);
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
