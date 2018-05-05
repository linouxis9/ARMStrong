package simulator;

/**
 * An Address class used to interact with the Memory classes.
 */
public class Address {

	// TODO write javadoc comment
	/**
	 * 
	 */
	public Address(int address) {
		this.addr = address;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	private final int addr;

	// TODO write javadoc comment
	/**
	 * 
	 */
	public int getAddress() {
		return addr;
	}

	@Override
	public String toString() {
		return "Address [address=" + addr + "]";
	}
	
	
}
