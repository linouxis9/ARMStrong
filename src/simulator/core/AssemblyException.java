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
 * AssemblyException regroup the different Exception that are due to a wrong assembly.
 */
public abstract class AssemblyException extends Exception {

	private static final long serialVersionUID = -2485352858328151954L;
	protected final int line;
	
	public AssemblyException(int line) {
		this.line = line;
	}
	
	public int getLine() {
		return this.line;
	}
}
