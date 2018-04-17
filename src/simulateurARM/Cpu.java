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
		
		// TODO write javadoc comment
		/**
		 * 
		 */
		public void adc(Register r1, Register r2, Operand2 op) {
			r1.setValue(r2.getValue() + op.getValue() + Cpu.this.cpsr.booleanToInt(Cpu.this.cpsr.isC()));
		}
		
		// TODO write javadoc comment
		/**
		 * 
		 */
		public void add(Register r1, Register r2, Operand2 op, Set<Flag> flags) {
			r1.setValue(r2.getValue() + op.getValue());
		}
		
		// TODO write javadoc comment
		/**
		 * 
		 */
		public void and(Register r1, Register r2, Operand2 op) {
			r1.setValue(r2.getValue() & op.getValue());
		}
		
		// TODO write javadoc comment
		/**
		 * 
		 */
		public void b(Operand2 op) {
			Cpu.this.pc.setValue(op.getValue());
		}
		
		// TODO write javadoc comment
		/**
		 * 
		 */
		public void bic(Register r1, Register r2, Operand2 op) {
			r1.setValue(r2.getValue() & ~op.getValue());

		}
		
		// TODO write javadoc comment
		/**
		 * 
		 */
		public void bl(Operand2 op) {

		}
		
		// TODO write javadoc comment
		/**
		 * 
		 */
		public void bx(Register r1) {

		}
		
		// TODO write javadoc comment
		/**
		 * 
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
		
		// TODO write javadoc comment
		/**
		 * 
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
		
		// TODO write javadoc comment
		/**
		 * 
		 */
		public void eor(Register r1, Register r2, Operand2 op) {
			r1.setValue((int)Math.pow(r2.getValue(),op.getValue()));
		}
		
		// TODO write javadoc comment
		/**
		 * 
		 */
		public void ldr(Register r1, Operand2 op, Set<Flag> flags) {
			r1.setValue(op.getValue());
		}
		
		// TODO write javadoc comment
		/**
		 * 
		 */
		public void mla(Register r1, Register r2, Register r3, Register r4, Set<Flag> flags) {
			r1.setValue((r2.getValue() * r3.getValue()) + r3.getValue());
		}	
		
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
		
		// TODO write javadoc comment
		/**
		 * 
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