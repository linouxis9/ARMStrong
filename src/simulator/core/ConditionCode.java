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
 * ARM, like many other architectures, implements conditional execution using a
 * set of flags which store the state information about a previous operation.
 * 
 * The condition is specified with a two-letter suffix, such as EQ or CC,
 * appended to the mnemonic.
 * 
 * ConditionCode lists the different two-letter suffixes representing the
 * different condition mode available on the ARM's processor.
 */
public enum ConditionCode {
	/**
	 * Equal
	 */
	EQ,
	
	/**
	 * Not Equal
	 */
	NE, 
	
	/**
	 * Carry set (Greater than or equal UNSIGNED)
	 */
	CS,
	
	/**
	 * Carry clear (Lesser than UNSIGNED)
	 */
	CC, 
	
	/**
	 * Negative
	 */
	MI, 
	
	/**
	 * Positive or zero
	 */
	PL, 
	
	/**
	 * Overflow
	 */
	VS, 
	
	/**
	 * No overflow
	 */
	VC, 
	
	/**
	 * Unsigned higher
	 */
	HI, 
	
	/**
	 * Unsigned lesser than or equal
	 */
	LS, 
	
	/**
	 * Signed greater than or equal
	 */
	GE, 
	
	/**
	 * Signed lesser than	
	 */
	LT, 
	
	/**
	 * Signed greater than
	 */
	GT,
	
	/**
	 * Signed less than or equal
	 */
	LE, 
	
	/**
	 * Always (This is the implicit ConditionCode unless explicitly stated for each instruction)
	 */
	AL;
}