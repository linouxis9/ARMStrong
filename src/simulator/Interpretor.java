package simulator;

import java.util.HashSet;
import java.util.List;

public class Interpretor {

	// TODO write javadoc comment
	/**
	 * 
	 */
	private final Program program;
	private final Cpu cpu;
	private int line;

	// TODO write javadoc comment
	/**
	 * 
	 */
	public Interpretor(Cpu cpu, Program program) {
		this.program = program;
		this.cpu = cpu;
		this.line = 0;
	}

	// TODO write javadoc comment
	/**
	 * @throws UnknownLabelException
	 * 
	 */
	public void parseProgram() throws InvalidSyntaxException, InvalidOperationException, InvalidRegisterException,
			InvalidLabelException, UnknownLabelException {
		while (this.program.hasNext()) {
			this.line++;
			List<Token> tokens = this.program.next();
			this.cpu.addInstruction(this.parse(tokens));
		}
	}

	// TODO write javadoc comment
	/**
	 * @throws InvalidLabelException
	 * @throws UnknownLabelException
	 * 
	 */
	public Instruction parse(List<Token> tokens) throws InvalidSyntaxException, InvalidOperationException,
			InvalidRegisterException, InvalidLabelException, UnknownLabelException {

		SyntaxChecker.checkSyntax(tokens, line);
		ConditionCode cc = ConditionCode.AL;
		HashSet<Flag> flags = new HashSet<Flag>();
		int i = 0;
		int b = 0;
		if (tokens.get(b).getToken() == TokenType.LABEL) {
			String label = tokens.get(b).getValue();
			if (this.cpu.getLabelMap().containsKey(label)) {
				throw new InvalidLabelException(line, label);
			}
			this.cpu.getLabelMap().put(label, this.cpu.instructionsLen() * 4);
			b++;
		}

		if (tokens.get(tokens.size() - 1).getToken() == TokenType.IDENTIFIER) {
			String label = tokens.get(tokens.size() - 1).getValue();
			if (!this.cpu.getLabelMap().containsKey(label)) {
				throw new UnknownLabelException(line, label);
			}
		}
		i = b + 1;
		while (tokens.get(i).getToken() == TokenType.FLAG) {
			switch (tokens.get(i).getValue()) {
			case "b":
				flags.add(Flag.B);
				break;
			case "h":
				flags.add(Flag.H);
				break;
			case "s":
				flags.add(Flag.S);
				break;
			default:
			}
			i++;
		}
		if (tokens.get(i).getToken() == TokenType.CONDITIONCODE) {
			switch (tokens.get(1).getValue()) {
			case "eq":
				cc = ConditionCode.EQ;
				break;
			case "ne":
				cc = ConditionCode.NE;
				break;
			case "cs":
				cc = ConditionCode.CS;
				break;
			case "cc":
				cc = ConditionCode.CC;
				break;
			case "mi":
				cc = ConditionCode.MI;
				break;
			case "pl":
				cc = ConditionCode.PL;
				break;
			case "vs":
				cc = ConditionCode.VS;
				break;
			case "vc":
				cc = ConditionCode.VC;
				break;
			case "hi":
				cc = ConditionCode.HI;
				break;
			case "ls":
				cc = ConditionCode.LS;
				break;
			case "ge":
				cc = ConditionCode.GE;
				break;
			case "lt":
				cc = ConditionCode.LT;
				break;
			case "gt":
				cc = ConditionCode.GT;
				break;
			case "le":
				cc = ConditionCode.LE;
				break;
			case "al":
				cc = ConditionCode.AL;
				break;
			default:
			}
			i++;
		}

		switch (tokens.get(b).getValue()) {
		case "adc":
			return new Instruction(Operation.ADC, toRegister(tokens.get(i)), toRegister(tokens.get(i + 2)),
					handleOpe2(tokens.get(i + 4)), flags, cc);
		case "add":
			return new Instruction(Operation.ADD, toRegister(tokens.get(i)), toRegister(tokens.get(i + 2)),
					handleOpe2(tokens.get(i + 4)), flags, cc);
		case "and":
			return new Instruction(Operation.AND, toRegister(tokens.get(i)), toRegister(tokens.get(i + 2)),
					handleOpe2(tokens.get(i + 4)), flags, cc);
		case "b":
			return new Instruction(Operation.B, handleOpe2(tokens.get(i)), flags, cc);
		case "bic":
			return new Instruction(Operation.BIC, toRegister(tokens.get(i)), toRegister(tokens.get(i + 2)),
					handleOpe2(tokens.get(i + 4)), flags, cc);
		case "bl":
			return new Instruction(Operation.BL, handleOpe2(tokens.get(i)), flags, cc);
		case "cmn":
			return new Instruction(Operation.CMN, toRegister(tokens.get(i)), handleOpe2(tokens.get(i + 2)), flags, cc);
		case "cmp":
			return new Instruction(Operation.CMP, toRegister(tokens.get(i)), handleOpe2(tokens.get(i + 2)), flags, cc);
		case "eor":
			return new Instruction(Operation.EOR, toRegister(tokens.get(i)), toRegister(tokens.get(i + 2)),
					handleOpe2(tokens.get(i + 4)), flags, cc);
		case "ldr":
			return new Instruction(Operation.LDR, toRegister(tokens.get(i)), handleOpe2(tokens.get(i + 2)), flags, cc);
		case "mla":
			return new Instruction(Operation.MLA, toRegister(tokens.get(i)), toRegister(tokens.get(i + 2)), flags, cc);
		case "mov":
			return new Instruction(Operation.MOV, toRegister(tokens.get(i)), handleOpe2(tokens.get(i + 2)), flags, cc);
		case "mul":
			return new Instruction(Operation.MUL, toRegister(tokens.get(i)), toRegister(tokens.get(i + 2)),
					toRegister(tokens.get(i + 4)), flags, cc);
		case "mvn":
			return new Instruction(Operation.MVN, toRegister(tokens.get(i)), handleOpe2(tokens.get(i + 2)), flags, cc);
		case "orr":
			return new Instruction(Operation.ORR, toRegister(tokens.get(i)), toRegister(tokens.get(i + 2)),
					handleOpe2(tokens.get(i + 4)), flags, cc);
		case "sdiv":
			return new Instruction(Operation.SDIV, toRegister(tokens.get(i)), toRegister(tokens.get(i + 2)),
					toRegister(tokens.get(i + 4)), flags, cc);
		case "str":
			return new Instruction(Operation.STR, toRegister(tokens.get(i)), handleOpe2(tokens.get(i + 2)), flags, cc);
		case "svc":
		case "swi":
			return new Instruction(Operation.SWI, handleOpe2(tokens.get(i)), flags, cc);
		case "sub":
			return new Instruction(Operation.SUB, toRegister(tokens.get(i)), toRegister(tokens.get(i + 2)),
					handleOpe2(tokens.get(i + 4)), flags, cc);
		case "swp":
			return new Instruction(Operation.SWP, toRegister(tokens.get(i)), toRegister(tokens.get(i + 2)),
					handleOpe2(tokens.get(i + 4)), flags, cc);
		case "teq":
			return new Instruction(Operation.TEQ, toRegister(tokens.get(i)), handleOpe2(tokens.get(i + 2)), flags, cc);
		case "tst":
			return new Instruction(Operation.TST, toRegister(tokens.get(i)), handleOpe2(tokens.get(i + 2)), flags, cc);
		case "udiv":
			return new Instruction(Operation.SDIV, toRegister(tokens.get(i)), toRegister(tokens.get(i + 2)),
					toRegister(tokens.get(i + 4)), flags, cc);

		}

		return null;
	}

	
	
	private Register toRegister(Token register) {
		return this.cpu.getRegisters()[Integer.parseInt(register.getValue())];
	}

	private Operand2 handleOpe2(Token ope2) {
		String inner = ope2.getValue();
		switch (ope2.getToken()) {
		case REGISTER:
			return toRegister(ope2);
		case HASH:
			return new ImmediateValue(Integer.parseInt(inner));
		case OFFSET:
			return toRegister(ope2);
		case INDEXEDOFFSET:
			int address = Integer.parseInt(inner);
			address = address + Integer.parseInt(inner.substring(inner.indexOf(",")));
			return null;
		case IDENTIFIER:
			return new ImmediateValue(this.cpu.getLabelMap().get(inner) - this.cpu.instructionsLen() * 4);
		default:
			return null;
		}
	}
}
