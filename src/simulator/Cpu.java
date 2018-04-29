package simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Cpu {

	/**
	 * Register[] registers
	 * 
	 * Hold a reference to the different CPU registers.
	 * 
	 * Interior mutability FTW, the variable can be final
	 */
	private final Register[] registers;

	/**
	 * Ram ram
	 * 
	 * Hold a reference to the CPU's RAM.
	 */
	private final Ram ram;

	private final Cpsr cpsr;
	private final Register pc;
	private final Register lr;
	private final Register sp;
	private final ALU alu;
	private boolean interrupt;
	private final List<Instruction> instructions;

	/**
	 * Stores #@rmsim's provided-routines that can be called from the assembly that
	 * are *NOT* implemented in assembly.
	 * 
	 * This is to be used cojointly with the SWI or SVC ARM instruction.
	 * 
	 * #@rmsim provides several calls for the assembly to interact with the
	 * simulator. On a bare metal computer, these calls are usually provided by the
	 * OS kernel. Please read the User Manual for more informations about the
	 * available calls.
	 * 
	 */
	private final Map<Integer, Callable> interruptsVector;

	private Map<String, Integer> labelMap;

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

		this.sp = this.registers[13]; // SP should reference the same register as this.registers[13]
		this.lr = this.registers[14]; // LR should reference the same register as this.registers[14]
		this.pc = this.registers[15]; // PC should reference the same register as this.registers[15]
		this.alu = new ALU();
		this.instructions = instructions;
		this.interruptsVector = new HashMap<Integer, Callable>();
		this.fillInterruptsVector();
		this.interrupt = false;
		this.labelMap = new HashMap<String, Integer>();
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public void execute() {
		try {
		while (!this.interrupt) {
			this.executeStep();
		}
		} catch (IndexOutOfBoundsException e) {
			
		}
		this.interrupt = false;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public void fillInterruptsVector() {
		this.interruptsVector.put(80, () -> {
			this.interrupt = true;
			System.out.println("Stopping the execution of the program");
		});
		this.interruptsVector.put(81, () -> {
			System.out.println(About.info());
		});
		this.interruptsVector.put(0, () -> {
			char c = '\0';
			int i = 0;
			try {
				c = (char) (this.ram.getByte(new Address(this.registers[0].getValue())));
				while (c != '\0') {
					System.out.print(c);
					i++;
					c = (char) (this.ram.getByte(new Address(this.registers[0].getValue() + i)));
				}
			} catch (InvalidMemoryAddressException e) {

			}
		});

		this.interruptsVector.put(1, () -> {
			System.out.println((char) (this.registers[0].getValue()));
		});
		
		this.interruptsVector.put(2, () -> {
			System.out.println((int) (this.registers[1].getValue()));
		});
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public void executeStep() {
		int offset = this.getPc().getValue();
		offset = ((int) Math.ceil((double) this.getPc().getValue() / 4));
		Instruction i = instructions.get(offset);
		this.pc.setValue(this.pc.getValue() + 4);
		
		if (this.cpsr.getConditionCodeStatus(i.getCc())) {
			System.out.println(i);
			this.runInstruction(i);
		}
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public void runInstruction(Instruction i) {
		switch (i.getOp()) {
		case ADC:
			this.alu.adc(i.getR1(), i.getR2(), i.getOpe2());
			break;
		case ADD:
			this.alu.add(i.getR1(), i.getR2(), i.getOpe2(), i.getFlags());
			break;
		case AND:
			this.alu.and(i.getR1(), i.getR2(), i.getOpe2());
			break;
		case B:
			this.alu.b(i.getOpe2());
			break;
		case BIC:
			this.alu.bic(i.getR1(), i.getR2(), i.getOpe2());
			break;
		case BL:
			this.alu.bl(i.getOpe2());
			break;
		case CMN:
			this.alu.cmn(i.getR1(), i.getOpe2());
			break;
		case CMP:
			this.alu.cmp(i.getR1(), i.getOpe2());
			break;
		case EOR:
			this.alu.eor(i.getR1(), i.getR2(), i.getOpe2());
			break;
		case LDR:
			this.alu.ldr(i.getR1(), i.getOpe2(), i.getFlags());
			break;
		case MLA:
			this.alu.mla(i.getR1(), i.getR2(), i.getR3(), (Register) i.getOpe2(), i.getFlags());
			break;
		case MOV:
			this.alu.mov(i.getR1(), i.getOpe2(), i.getFlags());
			break;
		case MVN:
			this.alu.mvn(i.getR1(), i.getOpe2(), i.getFlags());
			break;
		case MUL:
			this.alu.mul(i.getR1(), i.getR2(), i.getR3());
			break;
		case ORR:
			this.alu.orr(i.getR1(), i.getR2(), i.getOpe2(), i.getFlags());
			break;
		case SDIV:
			this.alu.sdiv(i.getR1(), i.getR2(), i.getR3());
			break;
		case STR:
			this.alu.str(i.getR1(), i.getOpe2(), i.getFlags());
			break;
		case SVC: // SVC is the new name for SWI in the latest ARM chips
		case SWI:
			this.alu.swi((ImmediateValue) i.getOpe2());
			break;
		case SUB:
			this.alu.sub(i.getR1(), i.getR2(), i.getOpe2(), i.getFlags());
			break;
		case SWP:
			this.alu.swp(i.getR1(), i.getR1(), i.getOpe2(), i.getFlags());
			break;
		case TEQ:
			this.alu.teq(i.getR1(), i.getOpe2());
			break;
		case TST:
			this.alu.tst(i.getR1(), i.getOpe2());
			break;
		case UDIV:
			this.alu.udiv(i.getR1(), i.getR2(), i.getR3());
			break;
		}
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
		 * The ADC instruction adds the values in R2 and Operand2, together with the
		 * carry flag.
		 * r1 <- r2 + op2 + carry
		 * 
		 * @param r1
		 *            Destination Register
		 * @param r2
		 *            Source Register
		 * @param op
		 *            Operand2
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
		 * @param r1
		 *            Destination Register
		 * @param r2
		 *            Source Register
		 * @param op
		 *            Operand2
		 */
		public void add(Register r1, Register r2, Operand2 op, Set<Flag> flags) {
			r1.setValue(r2.getValue() + op.getValue());
		}

		/**
		 * AND - And
		 * 
		 * The AND instruction performs bitwise AND operations on the values in R2 and
		 * Operand2.
		 * r1 <- r2 AND op2
		 * 
		 * @param r1
		 *            Destination Register
		 * @param r2
		 *            Source Register
		 * @param op
		 *            Operand2
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
		 * @param op
		 *            Operand2
		 */
		public void b(Operand2 op) {
			Cpu.this.pc.setValue(Cpu.this.pc.getValue() - 4 +op.getValue());
		}

		/**
		 * AND - And
		 * 
		 * The BIC instruction performs an R2 AND NOT OP operation.
		 * r1 <- r2 AND NOT op2
		 * 
		 * @param r1
		 *            Destination Register
		 * @param r2
		 *            Source Register
		 * @param op
		 *            Operand2
		 */
		public void bic(Register r1, Register r2, Operand2 op) {
			r1.setValue(r2.getValue() & ~op.getValue());

		}

		/**
		 * B - Branch with link
		 * 
		 * The BL instruction copies the address of the next instruction into r14 (lr,
		 * the link register), and causes a branch to op.
		 * pc <- op2
		 * 
		 * @param op
		 *            Operand2
		 */
		public void bl(Operand2 op) {
			Cpu.this.lr.setValue(Cpu.this.pc.getValue());
			this.b(op);

		}

		/**
		 * CMN - Compare Negative
		 * 
		 * The CMN instruction adds the value of Operand2 to the value in Rn and update
		 * the flags.
		 * - DISCARD <- r1 - (-op)
		 * - Update the flags in CPSR
		 * 
		 * @param r1
		 *            Destination Register
		 * @param op
		 *            Operand2
		 */
		public void cmn(Register r1, Operand2 op) {
			int value = r1.getValue() + op.getValue();
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
		 * The CMP instruction subtracts the value of Operand2 to the value in Rn and
		 * update the flags.
		 * - DISCARD <- r1 - op
		 * - Update the flags in CPSR
		 * 
		 * @param r1
		 *            Destination Register
		 * @param op
		 *            Operand2
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
		 * @param r1
		 *            Destination Register
		 * @param r2
		 *            Source Register
		 * @param op
		 *            Operand2
		 */
		public void eor(Register r1, Register r2, Operand2 op) {
			r1.setValue(r2.getValue() ^ op.getValue());
		}

		/**
		 * LDR - Load
		 * 
		 * The LDR instruction loads the value stored in the memory at the address op.
		 * r1 <- mem[op]
		 * 
		 * @param r1
		 *            Destination Register
		 * @param op
		 *            Operand2
		 */
		public void ldr(Register r1, Operand2 op, Set<Flag> flags) {
			try {
				if (flags.contains(Flag.B)) { 
					r1.setValue(Cpu.this.ram.getByte(new Address(op.getValue())));
				} else if (flags.contains(Flag.H)) { 
					r1.setValue(Cpu.this.ram.getHWord(new Address(op.getValue())));
				} else {
					r1.setValue(Cpu.this.ram.getValue(new Address(op.getValue())));
				}
			} catch (InvalidMemoryAddressException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/**
		 * MLA - Multiply Accumulate
		 * 
		 * The MLA instruction performs a multiplication between r2 and r3 and adds the
		 * value from r4.
		 * r1 <- r2 * r3 + r4
		 * 
		 * @param r1
		 *            Destination Register
		 * @param r2
		 *            Source Register
		 * @param r3
		 *            Source Register
		 * @param r4
		 *            Source Register
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
		 * @param r1
		 *            Destination Register
		 * @param op
		 *            Operand2
		 */
		public void mov(Register r1, Operand2 op, Set<Flag> flags) {
			r1.setValue(op.getValue());
		}

		/**
		 * MUL - Multiply
		 * 
		 * The MUL instruction performs a multiplication between r2 and r3.
		 * r1 <- r2 * r3
		 * 
		 * @param r1
		 *            Destination Register
		 * @param r2
		 *            Source Register
		 * @param r3
		 *            Source Register
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
		 * @param r1
		 *            Destination Register
		 * @param op
		 *            Operand2
		 */
		public void mvn(Register r1, Operand2 op, Set<Flag> flags) {
			r1.setValue(~op.getValue());
		}

		/**
		 * ORR - OR
		 * 
		 * The OR instruction performs a logical OR operation.
		 * r1 <- r2 OR op2
		 * 
		 * @param r1
		 *            Destination Register
		 * @param r2
		 *            Source Register
		 * @param op
		 *            Operand2
		 */
		public void orr(Register r1, Register r2, Operand2 op, Set<Flag> flags) {
			r1.setValue(r2.getValue() | op.getValue());
		}

		/**
		 * SDIV - Signed division
		 * 
		 * The SDIV instruction performs a signed division between r2 and r3.
		 * r1 <- r2 / r3
		 * 
		 * @param r1
		 *            Destination Register
		 * @param r2
		 *            Source Register
		 * @param r3
		 *            Source Register
		 */
		public void sdiv(Register r1, Register r2, Register r3) {

		}

		/**
		 * STR - Store
		 * 
		 * The STR instruction stores r1 in the memory at the address op.
		 * mem[op] <- r1
		 * 
		 * @param r1
		 *            Source Register
		 * @param op
		 *            Operand2
		 */
		public void str(Register r1, Operand2 op, Set<Flag> flags) {
			try {
				if (flags.contains(Flag.B)) {
					Cpu.this.ram.setByte(new Address(op.getValue()),(byte)(r1.getValue()));
				} else if (flags.contains(Flag.H)){
					Cpu.this.ram.setHWord(new Address(op.getValue()),(short)(r1.getValue()));
				} else {
					Cpu.this.ram.setValue(new Address(op.getValue()),(r1.getValue()));
				}
			} catch (InvalidMemoryAddressException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/**
		 * SWI - Software Interrupt
		 * 
		 * The SWI instruction causes a SWI exception. The processor branches to the SWI
		 * vector.
		 * 
		 * #@rmsim provides several calls for the assembly to interact with the
		 * simulator. On a bare metal computer, these calls are usually provided by the
		 * OS kernel. Please read the User Manual for more informations about the
		 * available calls.
		 * 
		 * @param r1
		 *            Source Register
		 * @param op
		 *            Operand2
		 */
		public void swi(ImmediateValue value) {
			try {
				Cpu.this.interruptsVector.get(value.getValue()).run();
			} catch (Exception e) {

			}
		}

		/**
		 * SUB - Substract
		 * 
		 * The SUB instruction subtracts the value of Operand2 to the value in R2.
		 * r1 <- r2 - op2
		 * 
		 * @param r1
		 *            Destination Register
		 * @param r2
		 *            Source Register
		 * @param op
		 *            Operand2
		 */
		public void sub(Register r1, Register r2, Operand2 op, Set<Flag> flags) {
			r1.setValue(r2.getValue() - op.getValue());
		}

		/**
		 * SWP - Swap
		 * 
		 * The SWP instruction swaps data between registers and memory.
		 * - r1 <- mem[pointer]
		 * - mem[pointer] <- r2
		 * 
		 * @param r1
		 *            Destination Register
		 * @param r2
		 *            Source Register
		 * @param op
		 *            Address in memory
		 */
		public void swp(Register r1, Register r2, Operand2 op, Set<Flag> flags) {

		}

		/**
		 * TEQ - Test equivalence
		 * 
		 * The TEQ instruction performs a bitwise Exclusive OR on the value of Operand2
		 * and the value in r1 and update the flags.
		 * - DISCARD <- r1 XOR op 
		 * - Update the flags in CPSR
		 * 
		 * @param r1
		 *            Destination Register
		 * @param op
		 *            Operand2
		 */
		public void teq(Register r1, Operand2 op) {

		}

		/**
		 * TST - Test bits
		 * 
		 * The TST instruction performs a bitwise Exclusive AND on the value of Operand2
		 * and the value in r1 and update the flags.
		 * - DISCARD <- r1 AND op
		 * - Update the flags in CPSR
		 * 
		 * @param r1
		 *            Destination Register
		 * @param op
		 *            Operand2
		 */
		public void tst(Register r1, Operand2 op) {

		}

		/**
		 * UDIV - Signed division
		 * 
		 * The UDIV instruction performs an unsigned division between r2 and r3.
		 * r1 <- r2 / r3
		 * 
		 * @param r1
		 *            Destination Register
		 * @param r2
		 *            Source Register
		 * @param r3
		 *            Source Register
		 */
		public void udiv(Register r1, Register r2, Register r3) {

		}
	}
	
	
	
}