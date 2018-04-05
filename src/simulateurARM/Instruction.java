package simulateurARM;

import java.util.Set;

public class Instruction {
	private final Operation op;
	private final Register r1;
	private final Register r2;
	private final Register r3;
	private final Operand2 ope2;
	private final ConditionCode cc;
	private final Set<Flag> flags;
	public Instruction(Operation op, Register r1, Register r2, Register r3, Operand2 ope2, Set<Flag> flags, ConditionCode cc) {
		this.op = op;
		this.r1 = r1;
		this.r2 = r2;
		this.r3 = r3;
		this.ope2 = ope2;
		this.cc = cc;
		this.flags = flags;
	}
	public Instruction(Operation op, Register r1, Register r2, Operand2 ope2, Set<Flag> flags, ConditionCode cc) {
		this(op,r1,r2,null,ope2,flags,cc);
	}
	
	public Instruction(Operation op, Register r1, Operand2 ope2, Set<Flag> flags, ConditionCode cc) {
		this(op,r1,null,null,ope2,flags,cc);
	}
	
	public Instruction(Operation op, Operand2 ope2, Set<Flag> flags, ConditionCode cc) {
		this(op,null,null,null,ope2,flags,cc);
	}

	public Instruction(Operation op, Register r1, Set<Flag> flags, ConditionCode cc) {
		this(op,r1,null,null,null,flags,cc);
	}
	
	public Operation getOp() {
		return op;
	}

	public Register getR1() {
		return r1;
	}

	public Register getR2() {
		return r2;
	}

	public Register getR3() {
		return r3;
	}

	public Operand2 getOpe2() {
		return ope2;
	}

	public ConditionCode getCc() {
		return cc;
	}

	public Set<Flag> getFlags() {
		return flags;
	}
	
	
}
