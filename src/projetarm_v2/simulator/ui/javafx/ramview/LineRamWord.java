package projetarm_v2.simulator.ui.javafx.ramview;

public class LineRamWord implements LineRam {
	private int a;
	private int b;
	private int c;
	private int d;
	
	private OutputType type;
	
	public LineRamWord(int byte1, int byte2, int byte3, int byte4, OutputType type) {
		this.a = byte1;
		this.b = byte2;
		this.c = byte3;
		this.d = byte4;
		this.type = type;
	}
	
	public String getA() {
		return asString(a);
	}
	private String asString(int a2) {
		switch (this.type) {
			default:
			case NORMAL:
				return Integer.toUnsignedString(a2);
			case HEX:
				return Integer.toHexString(a2);
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
