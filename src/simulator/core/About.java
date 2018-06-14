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
 * Information about the Software
 */
public final class About {
	/**
	 * The name of the software.
	 */
	public static final String NAME = "#@rm Simulator";
	
	/**
	 * The software's version.
	 */
	public static final String VERSION = "v1.0";
	
	/**
	 * The software's copyright.
	 */
	public static final String COPYRIGHT = "Copyright (C) 2018 Project #@rm Simulator";
	
	/**
	 * The software's developers.
	 */
	public static final String DEVELOPERS = "Valentin D'Emmanuele, Gilles Mertens, Nicolas Gervasi, Hugo Chemarin, Dylan Fraisse";
	
	/**
	 * The software's copyright.
	 */
	public static final String EMAIL = "projectarm@devling.xyz";
	
	/**
	 * Returns a small recap of the software About's information.
	 * It should be noted that toString can't be a static method, which explains this method's name.
	 */
	public static String info() {
		return NAME + " " + VERSION + " by " + DEVELOPERS + System.lineSeparator() + COPYRIGHT;
	}
	
	/**
	 * Prevents the creation of an About object.
	 */	
	private About() {
		
	}
}
