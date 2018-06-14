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

public class ShiftedRegister implements Operand2 {

	private Register register;
	private Operand2 controlRegister;
	private Shift shift;

	public int getValue() {
		int value = this.register.getValue();
		switch(this.shift) {
			case LSL:
				value = value << this.controlRegister.getValue();
				break;
			case LSR:
				value = value >> this.controlRegister.getValue();
				break;
		}
		return value;
	}

	public boolean getCarry() {
		return true;
	}

	public ShiftedRegister(Register register, Shift shift, Operand2 controlRegister) {
		this.register = register;
		this.shift = shift;
		this.controlRegister = controlRegister;
	}

}
