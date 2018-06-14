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

/**
 * An Address class used to interact with the Memory classes.
 */
public class Address {

	/**
	 * A valid address on 32bits
	 */
	private final int addr;

	/**
	 * Initializes the address.
	 * Java doesn't have unsigned Integer, but that's not a problem,
	 * if a signed integer is passed, we can just interpret it as if it was an
	 * unsigned integer. Thanks to two's complements, we don't even have to bother
	 * checking for signed integers, as addition, subtraction and multiplication
	 * behave exactly the same whether the integer is signed or not.
	 * 
	 * @param address
	 *            A valid address on 32bit
	 */
	public Address(int address) {
		if (Integer.compareUnsigned(address,Ram.DEFAULT_SIZE) > 0) {
			throw new InvalidMemoryAddressException();
		}
		this.addr = address;
	}

	public Address offset(int offset) {
		return new Address(addr + offset);
	}

	/**
	 * Returns the address
	 */
	public int getAddress() {
		return addr;
	}

	@Override
	public int hashCode() {
		return addr;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address other = (Address) obj;
		if (addr != other.addr)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Address [address=" + Integer.toUnsignedString(addr) + "]";
	}

}
