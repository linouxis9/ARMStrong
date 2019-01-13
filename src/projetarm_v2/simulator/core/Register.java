package projetarm_v2.simulator.core;

public interface Register {

	public int getValue();

	public void setValue(int value);

	public default boolean getBit(int shift) {
		return intToBool((this.getValue() >> shift) & 0x1);
	}
	
	public default void setBit(int shift, boolean bit) {
		int value = this.getValue() & ~(1 << shift);
		this.setValue(value | (boolToInt(bit) << shift));
	}
	
	private static  boolean intToBool(int value) {
		return value == 1;
	}
	
	private static int boolToInt(boolean bool) {
		return bool ? 1 : 0;
	}
}