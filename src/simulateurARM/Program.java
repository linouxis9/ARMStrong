package simulateurARM;

import java.util.Iterator;

public class Program implements Iterator<String> {
	private String assembly;

	public Program(String assembly) {
		this.assembly = assembly;
	}
	
	public boolean hasNext() {
		return true;
	}
	
	public String next() {
		return null;
	}
	
	public void remove() {
		
	}
}
