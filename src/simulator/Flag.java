package simulator;

/**
 * Flags are cumulative suffixes that are at the very end of the mnemonics that can indicate to the instruction the way to operate.
 */
public enum Flag {
	/**
	 * Work on bytes.
	 */
	B,
	
	/**
	 * Work on 16-bits words.
	 */
	H, 
	
	/**
	 * Update the CPSR at the end of the instruction.
	 */
	S;
}
