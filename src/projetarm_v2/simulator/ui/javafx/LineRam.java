package projetarm_v2.simulator.ui.javafx;

public class LineRam {
	private String a;
	private String b;
	private String c;
	private String d;
	
	public LineRam(byte byte1, byte byte2, byte byte3, byte byte4) {
		this.a = Byte.toString(byte1);
		this.b = Byte.toString(byte2);
		this.c = Byte.toString(byte3);
		this.d = Byte.toString(byte4);
	}
	
	public String getA() {
		return a;
	}
	public void setA(String a) {
		this.a = a;
	}
	public String getB() {
		return b;
	}
	public void setB(String b) {
		this.b = b;
	}
	public String getC() {
		return c;
	}
	public void setC(String c) {
		this.c = c;
	}
	public String getD() {
		return this.d;
	}
	public void setD(String d) {
		this.d = d;
	}
	
}
