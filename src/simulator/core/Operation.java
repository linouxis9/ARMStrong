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

/**
 * Short mnemonics representing the different ARM operations possibles.
 */
public enum Operation {
	/**
	 * ADC - Add with Carry
	 * 
	 * The ADC instruction adds the values in R2 and Operand2, together with the
	 * carry flag. r1 <- r2 + op2 + carry
	 */
	ADC,
	
	/**
	 * ADD - Add
	 * 
	 * The ADD instruction adds the value of Operand2 to the value in R2. r1 <- r2 +
	 * op2
	 */
	ADD,
	
	/**
	 * AND - And
	 * 
	 * The AND instruction performs bitwise AND operations on the values in R2 and
	 * Operand2. r1 <- r2 AND op2
	 */
	AND, 
	
	/**
	 * B - Branch
	 * 
	 * The B instruction causes a branch to op. pc <- op2
	 */
	B, 

	/**
	 * BIC - And not
	 * 
	 * The BIC instruction performs an R2 AND NOT OP operation. r1 <- r2 AND NOT op2
	 */
	BIC,	
	
	/**
	 * B - Branch with link
	 * 
	 * The BL instruction copies the address of the next instruction into r14 (lr,
	 * the link register), and causes a branch to op. pc <- op2
	 */
	BL,
	
	/**
	 * CMN - Compare Negative
	 * 
	 * The CMN instruction adds the value of Operand2 to the value in Rn and update
	 * the flags. - DISCARD <- r1 - (-op) - Update the flags in CPSR
	 */
	CMN, 
	
	/**
	 * CMP - Compare
	 * 
	 * The CMP instruction subtracts the value of Operand2 to the value in Rn and
	 * update the flags. - DISCARD <- r1 - op - Update the flags in CPSR
	 */
	CMP, 
	
	/**
	 * EOR - Exclusive OR
	 * 
	 * The EOR instruction performs a logical Exclusive OR operation. r1 <- r2 XOR
	 * op2
	 */
	EOR,
	
	/**
	 * LDR - Load
	 * 
	 * The LDR instruction loads the value stored in the memory at the address op.
	 * r1 <- mem[op]
	 */
	LDR,

	/**
	 * MLA - Multiply Accumulate
	 * 
	 * The MLA instruction performs a multiplication between r2 and r3 and adds the
	 * value from r4. r1 <- r2 * r3 + r4
	 */
	MLA,
	
	/**
	 * MOV - Move
	 * 
	 * The MOV instruction copies the value of Operand2 into r1. r1 <- op
	 */
	MOV,
	
	/**
	 * MVN - Move NOT
	 * 
	 * The MVN instruction copies the complement of Operand2 into r1. r1 <- NOT op
	 */
	MVN, 
	
	/**
	 * MUL - Multiply
	 * 
	 * The MUL instruction performs a multiplication between r2 and r3. r1 <- r2 *
	 * r3
	 */
	MUL, 
	
	/**
	 * ORR - OR
	 * 
	 * The OR instruction performs a logical OR operation. r1 <- r2 OR op2
	 */
	ORR, 
	
	/**
	 * SDIV - Signed division
	 * 
	 * The SDIV instruction performs a signed division between r2 and r3. r1 <- r2 /
	 * r3
	 */
	SDIV,
	
	/**
	 * STR - Store
	 * 
	 * The STR instruction stores r1 in the memory at the address op. mem[op] <- r1
	 */
	STR, 
	
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
	 */
	SWI, 
	
	/**
	 * SUB - Substract
	 * 
	 * The SUB instruction subtracts the value of Operand2 to the value in R2. r1 <-
	 * r2 - op2
	 */
	SUB, 
	
	/**
	 * SVC - Supervisor call
	 * 
	 * The SVC instruction causes a SWI exception. The processor branches to the SWI
	 * vector.
	 * 
	 * SVC replaces the SWI call in newer ARM instructions set.
	 * 
	 * #@rmsim provides several calls for the assembly to interact with the
	 * simulator. On a bare metal computer, these calls are usually provided by the
	 * OS kernel. Please read the User Manual for more informations about the
	 * available calls.
	 */ 
	SVC, 
	
	/**
	 * SWP - Swap
	 * 
	 * The SWP instruction swaps data between registers and memory. - r1 <-
	 * mem[pointer] - mem[pointer] <- r2
	 */
	SWP, 
	
	/**
	 * TEQ - Test equivalence
	 * 
	 * The TEQ instruction performs a bitwise Exclusive OR on the value of Operand2
	 * and the value in r1 and update the flags. - DISCARD <- r1 XOR op - Update the
	 * flags in CPSR
	 */
	TEQ, 
	
	/**
	 * TST - Test bits
	 * 
	 * The TST instruction performs a bitwise AND on the value of Operand2 and the
	 * value in r1 and update the flags. - DISCARD <- r1 AND op - Update the flags
	 * in CPSR
	 */
	TST,
	
	/**
	 * UDIV - Signed division
	 * 
	 * The UDIV instruction performs an unsigned division between r2 and r3. r1 <-
	 * r2 / r3
	 */
	UDIV;
}