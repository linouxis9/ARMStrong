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

import simulator.core.exceptions.InvalidMemoryAddressException;

public interface Memory {

	/**
	 * Get the byte at the given address in the memory
	 * @param address
	 * 		the address targeted
	 */
	public byte getByte(Address address) throws InvalidMemoryAddressException;

	/**
	 * Set a byte at the given address in the memory
	 * @param address
	 * 		the address targeted
	 * @param value
	 * 		the new value of the targeted byte 
	 */
	public void setByte(Address address, byte value) throws InvalidMemoryAddressException;

	/**
	 * Get the half word (16bit value) at the given address in the memory
	 * @param address
	 * 		the address targeted
	 */
	public short getHWord(Address address) throws InvalidMemoryAddressException;

	/**
	 * Set a half word (16bit value) at the given address in the memory
	 * @param address
	 * 		the address targeted
	 * @param value
	 * 		the new value of the targeted half word (16bit value)
	 */
	public void setHWord(Address address, short value) throws InvalidMemoryAddressException;

	/**
	 * Get the word (32bit value) at the given address in the memory
	 * @param address
	 * 		the address targeted
	 */
	public int getValue(Address address) throws InvalidMemoryAddressException;

	/**
	 * Set a word (32bit value) at the given address in the memory
	 * @param address
	 * 		the address targeted
	 * @param value
	 * 		the new value of the targeted word (32bit value)
	 */
	public void setValue(Address address, int value) throws InvalidMemoryAddressException;
}
