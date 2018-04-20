package simulateurARM;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Cpu {
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	private final Register[] registers;
	private final Ram ram;
	private final Cpsr cpsr;
	private final Register pc;
	private final Register lr;
	private final Register sp;
	private final ALU alu;
	private final List<Instruction> instructions;
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	public Cpu() {
		this(new ArrayList<Instruction>());
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public Cpu(List<Instruction> instructions) {
		this.ram = new Ram();
		this.cpsr = new Cpsr();
		this.registers = new Register[16];
		
		for (int i = 0; i < 16; i++) {
			this.registers[i] = new Register();
		}

		this.sp = this.registers[13];
		this.lr = this.registers[14];
		this.pc = this.registers[15];		
		this.alu = new ALU();
		this.instructions = instructions;
	}
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	public void execute() {
		this.alu.adc(null, null, null);
	}
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	public void executeStep() {
		this.pc.setValue(this.pc.getValue()+1);
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public void addInstruction(Instruction instruction) {
		this.instructions.add(instruction);
	}
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	public Register[] getRegisters() {
		return registers;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public Ram getRam() {
		return ram;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public Register getPc() {
		return pc;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public Register getLr() {
		return lr;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public Register getSp() {
		return sp;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	private class ALU {
		
		/**
		 * ADC - Add with Carry
		 * 
		 * The ADC instruction adds the values in R2 and Operand2, together with the carry flag. 		
		 * r1 <- r2 + op2 + carry
		 * 
		 * @param r1 Destination Register
		 * @param r2 Source Register
		 * @param op Operand2
		 */
		public void adc(Register r1, Register r2, Operand2 op) {
			r1.setValue(r2.getValue() + op.getValue() + Cpu.this.cpsr.booleanToInt(Cpu.this.cpsr.isC()));
		}
		
		/**
		 * ADD - Add
		 * 
		 * The ADD instruction adds the value of Operand2 to the value in R2.
		 * r1 <- r2 + op2
		 * 
		 * @param r1 Destination Register
		 * @param r2 Source Register
		 * @param op Operand2
		 */
		public void add(Register r1, Register r2, Operand2 op, Set<Flag> flags) {
			r1.setValue(r2.getValue() + op.getValue());
		}
		
		/**
		 * AND - And
		 * 
		 * The AND instruction performs bitwise AND operations on the values in R2 and Operand2.
		 * r1 <- r2 AND op2
		 * 
		 * @param r1 Destination Register
		 * @param r2 Source Register
		 * @param op Operand2
		 */
		public void and(Register r1, Register r2, Operand2 op) {
			r1.setValue(r2.getValue() & op.getValue());
		}
		
		/**
		 * B - Branch
		 * 
		 * The B instruction causes a branch to op.
		 * pc <- op2
		 * 
		 * @param op Operand2
		 */
		public void b(Operand2 op) {
			Cpu.this.pc.setValue(op.getValue());
		}
		
		/**
		 * AND - And
		 * 
		 * The BIC instruction performs an R2 AND NOT OP operation.
		 * r1 <- r2 AND NOT op2
		 * 
		 * @param r1 Destination Register
		 * @param r2 Source Register
		 * @param op Operand2
		 */
		public void bic(Register r1, Register r2, Operand2 op) {
			r1.setValue(r2.getValue() & ~op.getValue());

		}
		
		/**
		 * B - Branch with link
		 * 
		 * The BL instruction copies the address of the next instruction into r14 (lr, the link register), and causes a branch to op.
		 * pc <- op2
		 * 
		 * @param op Operand2
		 */
		public void bl(Operand2 op) {
			Cpu.this.lr.setValue(Cpu.this.pc.getValue());
			this.b(op);
			
		}
		
		/**
		 * CMN - Compare Negative
		 * 
		 * The CMN instruction adds the value of Operand2 to the value in Rn and update the flags.
		 * - DISCARD <- r1 - (-op)
		 * - Update the flags in CPSR
		 * 
		 * @param r1 Destination Register
		 * @param op Operand2
		 */
		public void cmn(Register r1, Operand2 op) {
			int value = r1.getValue() - op.getValue();
			Cpu.this.cpsr.reset();
			if (value < 0) {
				Cpu.this.cpsr.setN(true);
			} else if (value == 0) {
				Cpu.this.cpsr.setZ(true);
			} else if (value > 0) {
			}
		}
		
		/**
		 * CMP - Compare
		 * 
		 * The CMP instruction subtract the value of Operand2 to the value in Rn and update the flags.
		 * - DISCARD <- r1 - op
		 * - Update the flags in CPSR
		 * 
		 * @param r1 Destination Register
		 * @param op Operand2
		 */
		public void cmp(Register r1, Operand2 op) {
			int value = r1.getValue() - op.getValue();
			Cpu.this.cpsr.reset();
			if (value < 0) {
				Cpu.this.cpsr.setN(true);
			} else if (value == 0) {
				Cpu.this.cpsr.setZ(true);
			} else if (value > 0) {
			}
		}
		
		/**
		 * EOR - Exclusive OR
		 * 
		 * The EOR instruction performs a logical Exclusive OR operation.
		 * r1 <- r2 XOR op2
		 * 
		 * @param r1 Destination Register
		 * @param r2 Source Register
		 * @param op Operand2
		 */
		public void eor(Register r1, Register r2, Operand2 op) {
			r1.setValue(r2.getValue() ^ op.getValue());
		}
		
		/**
		 * LDR - Load
		 * 
		 * The LDR instruction load the value stored in the memory at the address op.
		 * r1 <- mem[op]
		 * 
		 * @param r1 Destination Register
		 * @param r2 Source Register
		 * @param op Operand2
		 */
		public void ldr(Register r1, Operand2 op, Set<Flag> flags) {
			r1.setValue(op.getValue());
		}
		
		/**
		 * MLA - Multiply Accumulate
		 * 
		 * The MLA instructions performs a multiplication between r2 and r3 and adds the value from r4.
		 * r1 <- r2 * r3 + r4
		 * 
		 * @param r1 Destination Register
		 * @param r2 Source Register
		 * @param r3 Source Register
		 * @param r4 Source Register
		 */
		public void mla(Register r1, Register r2, Register r3, Register r4, Set<Flag> flags) {
			r1.setValue((r2.getValue() * r3.getValue()) + r4.getValue());
		}	
		
		/**
		 * MOV - Move
		 * 
		 * The MOV instruction copies the value of Operand2 into r1.
		 * r1 <- op
		 * 
		 * @param r1 Destination Register
		 * @param op Operand2
		 */
		public void mov(Register r1, Operand2 op, Set<Flag> flags) {
			r1.setValue(op.getValue());
		}
		
		// TODO write javadoc comment
		/**
		 * 
		 */
		public void mul(Register r1, Register r2, Register r3) {
			r1.setValue(r2.getValue() * r3.getValue());
		}
		
		/**
		 * MVN - Move NOT
		 * 
		 * The MVN instruction copies the complement of Operand2 into r1.
		 * r1 <- NOT op
		 * 
		 * @param r1 Destination Register
		 * @param op Operand2
		 */
		public void mvn(Register r1, Operand2 op, Set<Flag> flags) {
			r1.setValue(~op.getValue());
		}
		
		// TODO write javadoc comment
		/**
		 * 
		 */
		public void orr(Register r1, Register r2, Operand2 op, Set<Flag> flags) {
			r1.setValue(r2.getValue() | op.getValue());
		}
		
		// TODO write javadoc comment
		/**
		 * 
		 */
		public void sdiv(Register r1, Register r2, Register r3) {

		}
		
		// TODO write javadoc comment
		/**
		 * 
		 */
		public void str(Register r1, Operand2 op, Set<Flag> flags) {

		}

		// TODO write javadoc comment
		/**
		 * 
		 */
		public void swi(ImmediateValue value) {

		}
		
		// TODO write javadoc comment
		/**
		 * 
		 */
		public void sub(Register r1, Register r2, Operand2 op, Set<Flag> flags) {
			r1.setValue(r2.getValue() - op.getValue());
		}
		
		// TODO write javadoc comment
		/**
		 * 
		 */
		public void svc(ImmediateValue value) {

		}
		
		// TODO write javadoc comment
		/**
		 * 
		 */
		public void swp(Register r1, Register r2, Set<Flag> flags) {

		}
		
		// TODO write javadoc comment
		/**
		 * 
		 */
		public void teq(Register r1, Operand2 op) {
			
		}
		
		// TODO write javadoc comment
		/**
		 * 
		 */
		public void tst(Register r1, Operand2 op) {

		}
		
		// TODO write javadoc comment
		/**
		 * 
		 */
		public void udiv(Register r1, Register r2, Register r3) {

		}
	}
}