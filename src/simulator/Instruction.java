package simulator;

import java.util.Set;

/**
 * A fully-fledged representation of an ARM Instruction. Ready to be decoded and
 * executed by the Cpu!
 */
public class Instruction {

	// TODO write javadoc comment
	/**
	 * 
	 */
	private final Operation op;
	private final Register r1;
	private final Register r2;
	private final Register r3;
	private final Operand2 ope2;
	private final ConditionCode cc;
	private final Set<Flag> flags;

	// TODO write javadoc comment
	/**
	 * 
	 */
	public Instruction(Operation op, Register r1, Register r2, Register r3, Operand2 ope2, Set<Flag> flags,
			ConditionCode cc) {
		this.op = op;
		this.r1 = r1;
		this.r2 = r2;
		this.r3 = r3;
		this.ope2 = ope2;
		this.cc = cc;
		this.flags = flags;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public Instruction(Operation op, Register r1, Register r2, Operand2 ope2, Set<Flag> flags, ConditionCode cc) {
		this(op, r1, r2, null, ope2, flags, cc);
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public Instruction(Operation op, Register r1, Operand2 ope2, Set<Flag> flags, ConditionCode cc) {
		this(op, r1, null, null, ope2, flags, cc);
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public Instruction(Operation op, Operand2 ope2, Set<Flag> flags, ConditionCode cc) {
		this(op, null, null, null, ope2, flags, cc);
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public Instruction(Operation op, Register r1, Set<Flag> flags, ConditionCode cc) {
		this(op, r1, null, null, null, flags, cc);
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public Operation getOp() {
		return op;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public Register getR1() {
		return r1;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public Register getR2() {
		return r2;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public Register getR3() {
		return r3;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public Operand2 getOpe2() {
		return ope2;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public ConditionCode getCc() {
		return cc;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public Set<Flag> getFlags() {
		return flags;
	}

	@Override
	public String toString() {
		return "Instruction [op=" + op + ", r1=" + r1 + ", r2=" + r2 + ", r3=" + r3 + ", ope2=" + ope2 + ", cc=" + cc
				+ ", flags=" + flags + "]";
	}

}
