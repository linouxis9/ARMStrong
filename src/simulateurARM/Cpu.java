package simulateurARM;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Cpu {
	private final Register[] registers;
	private final Ram ram;
	private final Cpsr cpsr;
	private final Register pc;
	private final Register lr;
	private final Register sp;
	private final ALU alu;
	private final List<Instruction> instructions;

	public Cpu() {
		this(new ArrayList<Instruction>());
	}

	public Cpu(List<Instruction> instructions) {
		this.ram = new Ram();
		this.cpsr = new Cpsr();
		this.registers = new Register[16];
		
		for (int i = 0; i < 16; i++) {
			this.registers[i] = new Register();
		}

		this.pc = this.registers[15];
		this.lr = this.registers[14];
		this.sp = this.registers[13];
		this.alu = new ALU();
		this.instructions = instructions;
	}
	public void execute() {
		this.alu.adc(null, null, null);
	}
	public void executeStep() {
		this.pc.setValue(this.pc.getValue()+1);
	}

	public void addInstruction(Instruction instruction) {
		this.instructions.add(instruction);
	}
	
	public Register[] getRegisters() {
		return registers;
	}

	public Ram getRam() {
		return ram;
	}

	public Register getPc() {
		return pc;
	}

	public Register getLr() {
		return lr;
	}

	public Register getSp() {
		return sp;
	}

	private class ALU {
		public void adc(Register r1, Register r2, Operand2 op) {
			r1.setValue(r2.getValue() + op.getValue() + Cpu.this.cpsr.booleanToInt(Cpu.this.cpsr.isC()));
		}
		
		public void add(Register r1, Register r2, Operand2 op, Set<Flag> flags) {
			r1.setValue(r2.getValue() + op.getValue());
		}
		
		public void and(Register r1, Register r2, Operand2 op) {
			r1.setValue(r2.getValue() & op.getValue());
		}
		public void b(Operand2 op) {
			Cpu.this.pc.setValue(op.getValue());
		}
		
		public void bic(Register r1, Register r2, Operand2 op) {

		}
		public void bl(Operand2 op) {

		}
		
		public void bx(Register r1) {

		}
		
		public void cmn(Register r1, Operand2 op) {

		}
		
		public void cmp(Register r1, Operand2 op) {
			int value = r1.getValue() - op.getValue();
			Cpu.this.cpsr.reset();
			if (value < 0) {
				Cpu.this.cpsr.setN(true);
			} else if (value == 0) {
				Cpu.this.cpsr.setZ(true);
			}
		}
		
		public void eor(Register r1, Register r2, Operand2 op) {

		}
		
		public void ldr(Register r1, Operand2 op, Set<Flag> flags) {
			r1.setValue(op.getValue());
		}
		
		public void mla(Register r1, Register r2, Register r3, Register r4, Set<Flag> flags) {

		}	
		
		public void mov(Register r1, Operand2 op, Set<Flag> flags) {

		}
		
		public void mul(Register r1, Register r2, Register r3) {
			
		}
		
		public void mvn(Register r1, Operand2 op, Set<Flag> flags) {

		}
		
		public void orr(Register r1, Register r2, Operand2 op, Set<Flag> flags) {
			r1.setValue(r2.getValue() | op.getValue());
		}
		public void sdiv(Register r1, Register r2, Register r3) {

		}
		public void str(Register r1, Operand2 op, Set<Flag> flags) {

		}

		public void swi(ImmediateValue value) {

		}
		
		public void sub(Register r1, Register r2, Operand2 op, Set<Flag> flags) {

		}
		
		public void svc(ImmediateValue value) {

		}
		
		public void swp(Register r1, Register r2, Set<Flag> flags) {

		}
		
		public void teq(Register r1, Operand2 op) {

		}
		
		public void tst(Register r1, Operand2 op) {

		}
		public void udiv(Register r1, Register r2, Register r3) {

		}
	}
}