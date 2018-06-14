/**
 * Copyright (c) 2018 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * Licensed under the MIT License;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         https://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package simulator.core;

import java.util.*;

import simulator.core.exceptions.Bug;
import simulator.core.exceptions.InvalidDirectiveException;
import simulator.core.exceptions.InvalidLabelException;
import simulator.core.exceptions.InvalidOperationException;
import simulator.core.exceptions.InvalidRegisterException;
import simulator.core.exceptions.InvalidSyntaxException;
import simulator.core.exceptions.UnknownLabelException;

public class Interpretor {

	/**
	 * Stores a reference to the object responsible of transforming an assembly
	 * program composed of String into a Tokenized's representation.
	 */
	private final Program program;

	/**
	 * Stores a reference to the Cpu. It is required for context-dependent
	 * Instruction.
	 */
	private final Cpu cpu;

	/**
	 * Stores a reference to the preprocessor.
	 */
	private final Preprocessor preprocessor;

	/**
	 * Stores a reference on the Map, that keep the link between the instruction and it's line, from the ArmSimulator
	 * it refreshes it when a new program is parsed
	 */
	private final Map<Instruction, Integer> linesMap;
	
	/**
	 * Returns a fully initialized Interpretor able to convert a Tokenized's
	 * representation of a program into instructions.
	 * 
	 * @param cpu
	 *            A reference for the Cpu where the instructions are going to be
	 *            added.
	 * @param program
	 *            The program to transform into a full set of instructions.
	 */
	public Interpretor(Cpu cpu, Program program, Map<Instruction, Integer> lines) {
		this.program = program;
		this.preprocessor = new Preprocessor(cpu);
		this.cpu = cpu;
		this.linesMap = lines;
	}

	/**
	 * Converts each line of assembly into instructions and add them into the CPU
	 * directly.
	 * 
	 * @throws InvalidSyntaxException
	 * @throws InvalidOperationException
	 * @throws InvalidRegisterException
	 * @throws InvalidLabelException
	 * @throws UnknownLabelException
	 * @throws InvalidDirectiveException 
	 * 
	 */
	public void parseProgram() throws InvalidSyntaxException, InvalidOperationException, InvalidRegisterException, InvalidLabelException, UnknownLabelException, InvalidDirectiveException {
		List<List<Token>> lines = new ArrayList<>();
		int line = 0;
		while (this.program.hasNext()) {
			lines.add(this.program.next());
		}
		
		this.preprocessor.preProcessPass1(lines);
		
		for (List<Token> tokens : lines) {
			line++;
			PreprocessorMessage message = this.preprocessor.preProcessPass2(tokens, line);
			if (message == PreprocessorMessage.VALIDINSTRUCTION) {
				Instruction i = this.parse(tokens);
				this.cpu.addInstruction(i);
				this.linesMap.put(i, line);
			}
		}
	}

	/**
	 * This static method parses an Instruction's Tokenized representation into a
	 * full-fledged Instruction instance. Why is this not a static method? The
	 * simulator package intends to be a library providing an ARM simulator but also
	 * some encapsulatable ARM tools easy to integrate in others java programs.
	 * However, creating an Instruction instance is a very context-dependent
	 * process, therefore, we cannot provide it as a static method.
	 * 
	 * @param tokens
	 *            The Tokenized's representation of an instruction.
	 * @return An instance of the Instruction class representing the line of
	 *         assembly.
	 */
	private Instruction parse(List<Token> tokens) {
		
		ConditionCode cc = ConditionCode.AL;
		HashSet<Flag> flags = new HashSet<>();
		int i = 1;
	
		while (tokens.get(i).getTokenType() == TokenType.FLAG) {
			switch (tokens.get(i).getRawFlag()) {
			case 'b':
				flags.add(Flag.B);
				break;
			case 'h':
				flags.add(Flag.H);
				break;
			case 's':
				flags.add(Flag.S);
				break;
			default:
			}
			i++;
		}
		if (tokens.get(i).getTokenType() == TokenType.CONDITIONCODE) {
			switch (tokens.get(1).getRawConditionCode()) {
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

		switch (tokens.get(0).getRawOperation()) {
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
			return new Instruction(Operation.UDIV, toRegister(tokens.get(i)), toRegister(tokens.get(i + 2)),
					toRegister(tokens.get(i + 4)), flags, cc);
		default:
			throw new Bug("Unknown operation caught by the Lexer? op: " + tokens.get(0).getRawOperation());
		}
	}

	/**
	 * Converts a Token representing a Register into a reference pointing to this very same register.
	 * 
	 * @param register The Token to convert.
	 * @return A reference pointing to the register.
	 */
	private Register toRegister(Token register) {
		int registerId = 0;
		switch(register.getTokenType()) {
		case REGISTER:
			registerId = register.getRawRegister();
			break;
		case OFFSET:
			registerId = register.getRawOffset();
			break;
		default:
			throw new Bug("toRegister: invalid token");
		}
		return this.cpu.getRegister(registerId);
	}

	/**
	 * Converts a Token into its Operand2 counterpart.
	 * 
	 * @param ope2 The token to convert.
	 * @return An instance of an Operand2 subclasses representing the token.
	 */
	private Operand2 handleOpe2(Token ope2) {
		switch (ope2.getTokenType()) {
		case REGISTER:
			return toRegister(ope2);
		case HASH:
			return new ImmediateValue(ope2.getRawImmediateValue());
		case OFFSET:
			return toRegister(ope2);
		default:
			throw new Bug("handleOpe2: invalid token");
		}
	}
}
