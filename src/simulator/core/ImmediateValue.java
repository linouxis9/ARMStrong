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
 * An immediate value is a piece of data that is stored as part of the instruction itself instead of being in a memory
 * location or a register. Immediate values are typically used in instructions that load a value or performs an
 * arithmetic or a logical operation on a constant.
 */
public class ImmediateValue implements Operand2 {

	/**
	 * An immediate value
	 */
	private final int value;

	/**
	 * Initializes the immediate value
	 */
	public ImmediateValue(int value) {
		this.value = value;
	}

	/**
	 * Returns an immediate value
	 */
	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "#" + value;
	}

}
