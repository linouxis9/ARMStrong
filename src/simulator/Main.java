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
