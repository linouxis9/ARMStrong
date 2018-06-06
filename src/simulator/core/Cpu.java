package simulator.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import simulator.core.exceptions.Bug;
import simulator.core.exceptions.InvalidMemoryAddressException;

/*
- ________/\\\\\\\\\__/\\\\\\\\\\\\\____/\\\________/\\\_        
-  _____/\\\////////__\/\\\/////////\\\_\/\\\_______\/\\\_       
-   ___/\\\/___________\/\\\_______\/\\\_\/\\\_______\/\\\_      
-    __/\\\_____________\/\\\\\\\\\\\\\/__\/\\\_______\/\\\_     
-     _\/\\\_____________\/\\\/////////____\/\\\_______\/\\\_    
-      _\//\\\____________\/\\\_____________\/\\\_______\/\\\_   
-       __\///\\\__________\/\\\_____________\//\\\______/\\\__  
-        ____\////\\\\\\\\\_\/\\\______________\///\\\\\\\\\/___ 
-         _______\/////////__\///_________________\/////////_____
 */
public class Cpu {

	/**
	 * Hold a reference to the different CPU registers.
	 * 
	 * Interior mutability FTW, the variable can be final.
	 */
	private final Register[] registers;

	/**
	 * The default number of Register held by an ARM CPU.
	 */
	public static final int DEFAULT_ARM_REGISTERS = 16;
	
	/**
	 * Hold a reference to the CPU's RAM.
	 */
	private final Ram ram;

	/**
	 * Hold a reference to the CPU's Current Program Status Register.
	 */
	private final Cpsr cpsr;

	/**
	 * Hold a reference to the Program Counter (pc) found in this.registers.
	 */
	private Register pc;

	/**
	 * Hold a reference to the Link Register (lr) found in this.registers.
	 */
	private Register lr;

	/**
	 * Hold a reference to the Stack Pointer (sp) found in this.registers.
	 */
	private Register sp;

	/**
	 * Hold a *strong* reference to an instance of the ALU nested class.
	 */
	private final ALU alu;

	/**
	 * Boolean indicating if the normal flow of execution of the CPU has been
	 * stopped by an external object.
	 */
	private AtomicBoolean isInterrupted;

	/**
	 * Boolean indicating if the normal flow of execution of the CPU has been
	 * stopped by a breakpoint. It can be controlled from assembly using Software
	 * Interrupts (SWI) or Supervisor (SVC) calls.
	 */
	private boolean isBreakpoint;

	/**
	 * Boolean indicating if the CPU has finished to execute all the instructions available at hand.
	 */
	private boolean hasFinished;
	
	/**
	 * Poor's man stack pointer
	 */
	private int pmsp;
	
	private static final int DEFAULT_PMSP = 900000;
	
	/**
	 * The Collection containing the instructions to execute by the processor.
	 */
	private final List<Instruction> instructions;

	/**
	 * Stores #@rmsim's provided-routines that can be called from the assembly that
	 * are *NOT* implemented in assembly.
	 * 
	 * This is to be used cojointly with the SWI or SVC ARM instruction.
	 * 
	 * #@rmsim provides several calls for the assembly to interact with the
	 * simulator. On a bare metal computer, these calls are usually provided by the
	 * OS kernel. Please read the User Manual for more information about the
	 * available calls.
	 * 
	 */
	private final Map<Integer, Callable> interruptsVector;

	/**
	 * Stores the addresses pointed by the assembly labels.
	 */
	private final Map<String, Integer> labelMap;

	/**
	 * Returns a fully initialized Cpu with an already specified set of instructions
	 * to execute.
	 * 
	 *            An initial list of Instructions to add to the Cpu's executable
	 *            memory space.
	 */
	public Cpu() {
		this.ram = new Ram();
		this.cpsr = new Cpsr();
		this.registers = new Register[DEFAULT_ARM_REGISTERS];
		this.instructions = new ArrayList<Instruction>();
		this.alu = new ALU();
		this.interruptsVector = new HashMap<>();
		this.fillInterruptsVector();
		this.interruptsVector.get(100).run();
		this.isInterrupted = new AtomicBoolean(false);
		this.labelMap = new HashMap<>();
		this.reset();
	}

	/**
	 * Reset the Cpu to a clean state.
	 */
	public void reset() {
		this.ram.cleanMemory();
		this.cpsr.reset();
		for (int i = 0; i < DEFAULT_ARM_REGISTERS; i++) {
			this.registers[i] = new Register();
		}
		this.sp = this.registers[13]; // SP should reference to the same register as this.registers[13]
		this.lr = this.registers[14]; // LR should reference to the same register as this.registers[14]
		this.pc = this.registers[15]; // PC should reference to the same register as this.registers[15]
		this.instructions.clear();
		this.isInterrupted.set(false);
		this.isBreakpoint = false;
		this.hasFinished = false;
		this.pmsp = Cpu.DEFAULT_PMSP;
		this.labelMap.clear();
	}

	/**
	 * Execute every instruction available to the processor until a Software
	 * Interrupt is made or that the CPU has tried to execute Instructions at
	 * invalid addresses.
	 */
	public void execute() {
		if (!this.hasFinished && !this.isBreakpoint() && !this.isInterrupted()) {
			hasFinished = this.executeInstruction();
		}
	}

	public void executeStep() {
		hasFinished = this.executeInstruction();
	}
	
	/**
	 * This function is responsible of the filing of Map<Integer,Callable>
	 * interruptsVector. It provides several functions directly accessible from
	 * Assembly. As we don't want to bother making a ton of small classes for such
	 * small methods, we use lambdas to implements Callable anonymous subclasses.
	 */
	private void fillInterruptsVector() {
		this.interruptsVector.put(81, () -> {
			this.isBreakpoint = true;
			Instruction instruction = this.instructions.get(Math.abs(this.pc.getValue() / 4 - 2));
			System.out.println("[DEBUG] BREAKPOINT after " + instruction);
		});

		this.interruptsVector.put(100, () -> {
			System.out.println("[INFO] " + About.info());
		});
		
		this.interruptsVector.put(-1, () -> {
			this.isInterrupted.set(true);
		});
		
		this.interruptsVector.put(0, () -> {
			char c = '\0';
			int i = 0;
			try {
				c = (char) (this.ram.getByte(new Address(this.registers[0].getValue())));
				while (c != '\0') { // Route diagram n°1 ( ͡° ͜ʖ ͡°)
					System.out.print(c);
					i++;
					c = (char) (this.ram.getByte(new Address(this.registers[0].getValue() + i)));
				}
			} catch (InvalidMemoryAddressException e) {
				System.out.println("[/!\\] Invalid address in r0 for SVC call #0");
			}
			System.out.println("");
		});

		this.interruptsVector.put(1, () -> {
			System.out.println((char) (this.registers[0].getValue()));
		});

		this.interruptsVector.put(2, () -> {
			System.out.println((int) (this.registers[1].getValue()));
		});
	}
	
	/**
	 * Executes the next instruction pointed by the Program Counter (pc).
	 * 
	 * @return True if no others instructions are available at this Program Counter offset.
	 */
	public boolean executeInstruction() {
		boolean result = false;
		try {
			int offset = this.getPc().getValue();
			offset = ((int) Math.ceil((double) offset / 4));
			Instruction i = instructions.get(offset);
			this.pc.setValue(this.pc.getValue() + 4);
	
			if (this.cpsr.getConditionCodeStatus(i.getCc())) {
				this.runInstruction(i);
			} else {
				System.out.println("Skip.: " + i + " (Condition not meet)");
			}
		}
		catch (IndexOutOfBoundsException e) {
			result = true;
		}
		return result;
	}

	/**
	 * Decodes an instruction and pass the required parameters to the ALU.
	 * 
	 * @param i
	 *            The instruction to decode and execute.
	 */
	private void runInstruction(Instruction i) {
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
			this.alu.mul(i.getR1(), i.getR2(), (Register)i.getOpe2());
			break;
		case ORR:
			this.alu.orr(i.getR1(), i.getR2(), i.getOpe2(), i.getFlags());
			break;
		case SDIV:
			this.alu.sdiv(i.getR1(), i.getR2(), (Register)i.getOpe2());
			break;
		case STR:
			this.alu.str(i.getR1(), i.getOpe2(), i.getFlags());
			break;
		case SVC: // SVC is the new name for SWI in the latest ARM chips
		case SWI:
			this.alu.swi(i.getOpe2());
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
			this.alu.udiv(i.getR1(), i.getR2(), (Register)i.getOpe2());
			break;
		default:
			System.out.println("OOPSIE WOOPSIE!! A PROBLEM OCCURED" + System.lineSeparator()
					+ "Please report the problem @ " + About.EMAIL + System.lineSeparator()
					+ "The code monkeys at our headquarters will work VEWY HAWD to fix this!");
		}
	}

	/**
	 * Appends an instruction at the end of the instructions executable memory.
	 * 
	 * @param instruction
	 *            Appends the instruction at the end of the Cpu's executable memory
	 *            space.
	 */
	public void addInstruction(Instruction instruction) {
		this.instructions.add(instruction);
	}

	/**
	 * Returns a reference to a Cpu's register. Useful for debugging purposes by
	 * ArmSimulator and Interpretor.
	 * 
	 * @param i
	 *            Register id
	 */
	public Register getRegister(int i) {
		if (i < 0 || i >= DEFAULT_ARM_REGISTERS) {
			throw new Bug("Invalid Register ID provided");
		}
		return this.registers[i];
	}

	/**
	 * Returns a reference to the Program Counter.
	 */
	public Register getPc() {
		return pc;
	}

	/**
	 * Returns a reference to the Link Register.
	 */
	public Register getLr() {
		return lr;
	}

	/**
	 * Returns a reference to the Stack Pointer.
	 */
	public Register getSp() {
		return sp;
	}

	/**
	 * Returns a reference to the Label Map.
	 */
	public Map<String, Integer> getLabelMap() {
		return labelMap;
	}

	/**
	 * Returns a reference to the Ram.
	 */
	public Ram getRam() {
		return ram;
	}
	
	/**
	 * Returns a reference to the CPSR.
	 */
	public Cpsr getCPSR() {
		return this.cpsr;
	}
	
	/**
	 * Returns the list of instructions.
	 */
	public List<Instruction> getInstructions() {
		return instructions;
	}
	
	/**
	 * Returns if the processor was interrupted by an external object using the interrupts() method.
	 */
	public boolean isInterrupted() {
		return this.isInterrupted.get();
	}

	/**
	 * Interrupts the execution flow of the processor.
	 */
	public void interruptMe(boolean interrupt) {
		this.isInterrupted.set(interrupt);
		if (!interrupt) {
			this.isBreakpoint = interrupt;
		}
	}
	
	/**
	 * Returns if the processor was interrupted by a breakpoint / the SWI call #81.
	 */
	public boolean isBreakpoint() {
		return this.isBreakpoint;
	}
	
	/**
	 * Returns if the processor was interrupted by a breakpoint / the SWI call #81.
	 */
	public boolean hasFinished() {
		return this.hasFinished;
	}

	/**
	 * Returns the Psmp
	 */
	public int getPmsp() {
		return this.pmsp;
	}

	/**
	 * Increments the Pmsp
	 */
	public void incrementPmsp() {
		this.pmsp++;
	}

	/**
	 * Returns the number of instructions stored in the Cpu's executable memory.
	 */
	public int instructionsLen() {
		return instructions.size();
	}

	/**
	 * This is the class that performs the calculations behind 24 ARM Assembly
	 * instructions supported by our simulator.
	 */
	private class ALU {

		public void updateFlags(long value) {
			Cpu.this.cpsr.reset();
			if (value < 0) {
				Cpu.this.cpsr.setN(true);
			} else if (value == 0) {
				Cpu.this.cpsr.setZ(true);
			}
		}

		/**
		 * ADC - Add with Carry
		 * 
		 * The ADC instruction adds the values in R2 and Operand2, together with the
		 * carry flag. r1 <- r2 + op2 + carry
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
		 * The ADD instruction adds the value of Operand2 to the value in R2. r1 <- r2 +
		 * op2
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
		 * Operand2. r1 <- r2 AND op2
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
		 * The B instruction causes a branch to op. pc <- op2
		 * 
		 * @param op
		 *            Operand2
		 */
		public void b(Operand2 op) {
			Cpu.this.pc.setValue(Cpu.this.pc.getValue() - 4 + op.getValue());
		}

		/**
		 * BIC - And not
		 * 
		 * The BIC instruction performs an R2 AND NOT OP operation. r1 <- r2 AND NOT op2
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
		 * the link register), and causes a branch to op. pc <- op2
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
		 * the flags. - DISCARD <- r1 - (-op) - Update the flags in CPSR
		 * 
		 * @param r1
		 *            Destination Register
		 * @param op
		 *            Operand2
		 */
		public void cmn(Register r1, Operand2 op) {
			long value = (long) r1.getValue() + op.getValue();
			updateFlags(value);

			if (value > Integer.MAX_VALUE || value < Integer.MIN_VALUE) {
				Cpu.this.cpsr.setV(true);
			}
			if (r1.getValue() > Math.abs((long) op.getValue())) {
				Cpu.this.cpsr.setC(true);
			}
		}

		/**
		 * CMP - Compare
		 * 
		 * The CMP instruction subtracts the value of Operand2 to the value in Rn and
		 * update the flags. - DISCARD <- r1 - op - Update the flags in CPSR
		 * 
		 * @param r1
		 *            Destination Register
		 * @param op
		 *            Operand2
		 */
		public void cmp(Register r1, Operand2 op) {
			long value = (long) r1.getValue() - op.getValue();
			updateFlags(value);

			if (value > Integer.MAX_VALUE || value < Integer.MIN_VALUE) {
				Cpu.this.cpsr.setV(true);
			}
			if (r1.getValue() <= Math.abs((long) op.getValue())) {
				Cpu.this.cpsr.setC(true);
			}
		}

		/**
		 * EOR - Exclusive OR
		 * 
		 * The EOR instruction performs a logical Exclusive OR operation. r1 <- r2 XOR
		 * op2
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
			}
			catch (InvalidMemoryAddressException e) {
				aluError("Memory address out of bounds " + Integer.toUnsignedString(op.getValue()) + ">" + Ram.DEFAULT_SIZE);
			}
		}

		/**
		 * MLA - Multiply Accumulate
		 * 
		 * The MLA instruction performs a multiplication between r2 and r3 and adds the
		 * value from r4. r1 <- r2 * r3 + r4
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
		 * The MOV instruction copies the value of Operand2 into r1. r1 <- op
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
		 * The MUL instruction performs a multiplication between r2 and r3. r1 <- r2 *
		 * r3
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
		 * The MVN instruction copies the complement of Operand2 into r1. r1 <- NOT op
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
		 * The OR instruction performs a logical OR operation. r1 <- r2 OR op2
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
		 * The SDIV instruction performs a signed division between r2 and r3. r1 <- r2 /
		 * r3
		 * 
		 * @param r1
		 *            Destination Register
		 * @param r2
		 *            Source Register
		 * @param r3
		 *            Source Register
		 */
		public void sdiv(Register r1, Register r2, Register r3) {
			if (r3.getValue() != 0) {
				r1.setValue(r2.getValue() / r3.getValue());
			} else {
				aluError("Division by 0");
			}
		}

		/**
		 * STR - Store
		 * 
		 * The STR instruction stores r1 in the memory at the address op. mem[op] <- r1
		 * 
		 * @param r1
		 *            Source Register
		 * @param op
		 *            Operand2
		 */
		public void str(Register r1, Operand2 op, Set<Flag> flags) {
			try {
				if (flags.contains(Flag.B)) {
					Cpu.this.ram.setByte(new Address(op.getValue()), (byte) (r1.getValue()));
				} else if (flags.contains(Flag.H)) {
					Cpu.this.ram.setHWord(new Address(op.getValue()), (short) (r1.getValue()));
				} else {
					Cpu.this.ram.setValue(new Address(op.getValue()), (r1.getValue()));
				}
			}
			catch (InvalidMemoryAddressException e) {
				aluError("Memory address out of bounds " + Integer.toUnsignedString(op.getValue()) + ">" + Ram.DEFAULT_SIZE);
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
		 * @param value
		 *            Operand2
		 */
		public void swi(Operand2 value) {
			try {
				Cpu.this.interruptsVector.get(value.getValue()).run();
			} catch (Exception e) {
				aluError("SWI Call no" + value.getValue() + " doesn't exist.");
			}
		}

		/**
		 * SUB - Substract
		 * 
		 * The SUB instruction subtracts the value of Operand2 to the value in R2. r1 <-
		 * r2 - op2
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
		 * The SWP instruction swaps data between registers and memory. - r1 <-
		 * mem[pointer] - mem[pointer] <- r2
		 * 
		 * @param r1
		 *            Destination Register
		 * @param r2
		 *            Source Register
		 * @param op
		 *            Address in memory
		 */
		public void swp(Register r1, Register r2, Operand2 op, Set<Flag> flags) {
			try {
				r1.setValue(Cpu.this.ram.getValue(new Address(op.getValue())));
				Cpu.this.ram.setValue(new Address(op.getValue()), (r2.getValue()));
			} catch (InvalidMemoryAddressException e) {
				aluError("Memory address out of bounds " + Integer.toUnsignedString(op.getValue()) + ">" + Ram.DEFAULT_SIZE);
			}
		}

		/**
		 * TEQ - Test equivalence
		 * 
		 * The TEQ instruction performs a bitwise Exclusive OR on the value of Operand2
		 * and the value in r1 and update the flags. - DISCARD <- r1 XOR op - Update the
		 * flags in CPSR
		 * 
		 * @param r1
		 *            Destination Register
		 * @param op
		 *            Operand2
		 */
		public void teq(Register r1, Operand2 op) {
			long value = r1.getValue() ^ op.getValue();
			updateFlags(value);
			if (op instanceof ShiftedRegister) {
				ShiftedRegister shiftedRegister = (ShiftedRegister) op;
				Cpu.this.cpsr.setC(shiftedRegister.getCarry());
			}
		}

		/**
		 * TST - Test bits
		 * 
		 * The TST instruction performs a bitwise AND on the value of Operand2 and the
		 * value in r1 and update the flags. - DISCARD <- r1 AND op - Update the flags
		 * in CPSR
		 * 
		 * @param r1
		 *            Destination Register
		 * @param op
		 *            Operand2
		 */
		public void tst(Register r1, Operand2 op) {
			long value = r1.getValue() & op.getValue();
			updateFlags(value);
			if (op instanceof ShiftedRegister) {
				ShiftedRegister shiftedRegister = (ShiftedRegister) op;
				Cpu.this.cpsr.setC(shiftedRegister.getCarry());
			}
		}

		/**
		 * UDIV - Unsigned division
		 * 
		 * The UDIV instruction performs an unsigned division between r2 and r3. r1 <-
		 * r2 / r3
		 * 
		 * @param r1
		 *            Destination Register
		 * @param r2
		 *            Source Register
		 * @param r3
		 *            Source Register
		 */
		public void udiv(Register r1, Register r2, Register r3) {
			if (r3.getValue() != 0) {
				r1.setValue(Integer.divideUnsigned(r2.getValue(), r3.getValue()));
			} else {
				aluError("Division by 0");
			}
		}
		
		// TODO Yeah.. We could use a logger
		private void aluError(String string) {
			System.out.println("[WARNING] " + string);
		}
	}

}
