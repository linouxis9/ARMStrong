import java.util.Scanner;

import simulator.core.*;
import simulator.ui.javafx.GUI;
/**
 * Our software's entrypoint
 */
public class Main {

	public static void main(String[] args) {
		if (args.length == 0) {
			//new GUI().start();
		}
		switch("cli") {
		case "gui":
			// new GUI().start();
		case "cli":
	        new CLI().start();
		}
	}
}
