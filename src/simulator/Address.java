package simulator;

public class Address {

	// TODO write javadoc comment
	/**
	 * 
	 */
	public Address(int address) {
		this.address = address;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	private final int address;

	// TODO write javadoc comment
	/**
	 * 
	 */
	public int getAddress() {
		return address;
	}

	@Override
	public String toString() {
		return "Address [address=" + address + "]";
	}
	
	
}
