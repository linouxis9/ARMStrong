package simulator.core;

/**
 * ARM, like many other architectures, implements conditional execution using a
 * set of flags which store the state information about a previous operation.
 * 
 * The condition is specified with a two-letter suffix, such as EQ or CC,
 * appended to the mnemonic.
 * 
 * ConditionCode lists the different two-letter suffixes representing the
 * different condition mode available on the ARM's processor.
 */
public enum ConditionCode {
	/**
	 * Equal
	 */
	EQ,
	
	/**
	 * Not Equal
	 */
	NE, 
	
	/**
	 * Carry set (Greater than or equal UNSIGNED)
	 */
	CS,
	
	/**
	 * Carry clear (Lesser than UNSIGNED)
	 */
	CC, 
	
	/**
	 * Negative
	 */
	MI, 
	
	/**
	 * Positive or zero
	 */
	PL, 
	
	/**
	 * Overflow
	 */
	VS, 
	
	/**
	 * No overflow
	 */
	VC, 
	
	/**
	 * Unsigned higher
	 */
	HI, 
	
	/**
	 * Unsigned lesser than or equal
	 */
	LS, 
	
	/**
	 * Signed greater than or equal
	 */
	GE, 
	
	/**
	 * Signed lesser than	
	 */
	LT, 
	
	/**
	 * Signed greater than
	 */
	GT,
	
	/**
	 * Signed less than or equal
	 */
	LE, 
	
	/**
	 * Always (This is the implicit ConditionCode unless explicitly stated for each instruction)
	 */
	AL;
}