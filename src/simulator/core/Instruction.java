package simulator.core;

import java.util.Set;

/**
 * A fully-fledged representation of an ARM Instruction. Ready to be decoded and
 * executed by the Cpu!
 */
public class Instruction {

	/**
	 * The operation 
	 */
	private final Operation op;

	/**
	 * The fist register parameter
	 */
	private final Register r1;

	/**
	 * The second register parameter
	 */
	private final Register r2;

	/**
	 * The third register parameter
	 */
	private final Register r3;

	/**
	 * The operand2 parameter
	 */
	private final Operand2 ope2;

	/**
	 * The condition code
	 */
	private final ConditionCode cc;

	/**
	 * The flags (see flag class)
	 */
	private final Set<Flag> flags;

	/**
	 * Build a usable instruction of type: opreationConditionCode register, register, register, operand2
	 * @param op
	 * 		The operation (ex: ADD)
	 * @param r1
	 * 		A reference on the the fist register of the instruction
	 * @param r2
	 * 		A reference on the the second register of the instruction
	 * @param r3
	 * 		A reference on the the third register of the instruction
	 * @param ope2
	 * 		An operand 2 value for the instruction
	 * @param flags
	 * 		The flags (see flag class)
	 * @param cc
	 * 		The condition for the execution of the instruction
	 */
	public Instruction(Operation op, Register r1, Register r2, Register r3, Operand2 ope2, Set<Flag> flags, ConditionCode cc) {
		this.op = op;
		this.r1 = r1;
		this.r2 = r2;
		this.r3 = r3;
		this.ope2 = ope2;
		this.cc = cc;
		this.flags = flags;
	}

	/**
	 * Build a usable instruction of type: opreationConditionCode register, register, operand2
	 * @param op
	 * 		The operation (ex: ADD)
	 * @param r1
	 * 		A reference on the the fist register of the instruction
	 * @param r2
	 * 		A reference on the the second register of the instruction
	 * @param ope2
	 * 		An operand 2 value for the instruction
	 * @param flags
	 * 		The flags (see flag class) 
	 * @param cc
	 * 		The condition for the execution of the instruction
	 */
	public Instruction(Operation op, Register r1, Register r2, Operand2 ope2, Set<Flag> flags, ConditionCode cc) {
		this(op, r1, r2, null, ope2, flags, cc);
	}

	/**
	 * Build a usable instruction of type: opreationConditionCode register, operand2
	 * @param op
	 * 		The operation (ex: ADD)
	 * @param r1
	 * 		A reference on the the register of the instruction
	 * @param ope2
	 * 		An operand 2 value for the instruction
	 * @param flags
	 * 		The flags (see flag class) 
	 * @param cc
	 * 		The condition for the execution of the instruction
	 */
	public Instruction(Operation op, Register r1, Operand2 ope2, Set<Flag> flags, ConditionCode cc) {
		this(op, r1, null, null, ope2, flags, cc);
	}

	/**
	 * Build a usable instruction of type: opreationConditionCode operand2
	 * @param op
	 * 		The operation (ex: ADD)
	 * @param ope2
	 * 		An operand 2 value for the instruction
	 * @param flags
	 * 		The flags (see flag class)
	 * @param cc
	 * 		The condition for the execution of the instruction
	 */
	public Instruction(Operation op, Operand2 ope2, Set<Flag> flags, ConditionCode cc) {
		this(op, null, null, null, ope2, flags, cc);
	}

	/**
	 * Build a usable instruction of type: opreationConditionCode register, operand2
	 * @param op
	 * 		The operation (ex: ADD)
	 * @param r1
	 * 		A reference on the the register of the instruction
	 * @param flags
	 * 		The flags (see flag class)
	 * @param cc
	 * 		The condition for the execution of the instruction
	 */
	public Instruction(Operation op, Register r1, Set<Flag> flags, ConditionCode cc) {
		this(op, r1, null, null, null, flags, cc);
	}

	
	/**
	 * 	Returns the type of the operation
	 */
	public Operation getOp() {
		return op;
	}

	/**
	 * Returns the first register
	 */
	public Register getR1() {
		return r1;
	}

	/**
	 * Returns the second register
	 */
	public Register getR2() {
		return r2;
	}

	/**
	 * Returns the third register
	 */
	public Register getR3() {
		return r3;
	}

	/**
	 * Returns the operation2 parameter
	 */
	public Operand2 getOpe2() {
		return ope2;
	}

	/**
	 * Returns the condition code
	 */
	public ConditionCode getCc() {
		return cc;
	}

	/**
	 * Returns the set of flags of the operation
	 */
	public Set<Flag> getFlags() {
		return flags;
	}
	
	// We can't do much better without using Reflection
	@Override
	public String toString() {
	/*	return "Instruction [op=" + op + ", r1=" + r1 + ", r2=" + r2 + ", r3=" + r3 + ", ope2=" + ope2 + ", cc=" + cc
				+ ", flags=" + flags + "]";*/
		StringBuilder string = new StringBuilder();
		
		string.append(this.op);
		if (!this.flags.isEmpty()) {
			string.append(this.flags);
		}
		if (this.cc != null && this.cc != ConditionCode.AL) {
			string.append(this.cc);
		}
		string.append(" ");
		if (this.r1 != null) {
			string.append(this.r1);
			string.append(", ");
		}
		if (this.r2 != null) {
			string.append(this.r2);
			string.append(", ");
		}
		if (this.r3 != null) {
			string.append(this.r3);
			string.append(", ");
		}
		if (this.ope2 != null) {
			string.append(this.ope2);
			string.append(", ");
		}
		return string.substring(0,string.length()-2);
	}

}
