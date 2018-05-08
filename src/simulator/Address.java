package simulator;

/**
 * An Address class used to interact with the Memory classes.
 */
public class Address {


	/**
	 *	A valid address on 32bits
	 */
	private final int addr;


	/**
	 * Initializes the address
	 * @param address
	 * 		A valid address on 32bit
	 */
	public Address(int address) {
		this.addr = address;
	}


	/**
	 * Returns the address
	 */
	public int getAddress() {
		return addr;
	}

	@Override
	public String toString() {
		return "Address [address=" + addr + "]";
	}
	
	
}
