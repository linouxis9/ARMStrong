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

package simulator;

import simulator.ui.cli.CLI;
import simulator.ui.javafx.GUI;
/**
 * Our software's entrypoint
 */
public class Main {

	public static void main(String[] args) {
		if (args.length == 0) {
			new GUI().startUI();
		} else {
			switch(args[0]) {
			case "cli":
		        new CLI().startUI();
		        break;
			case "gui":
			default:
				new GUI().startUI();
			}
		}
	}
}
