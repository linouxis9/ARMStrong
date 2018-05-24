import simulator.core.*;
import simulator.ui.cli.CLI;
import simulator.ui.javafx.GUI;
/**
 * Our software's entrypoint
 */
public class Main {

	public static void main(String[] args) {
		if (args.length == 0) {
			new GUI().startUI();
		}
		switch("cli") {
		case "cli":
	        new CLI().startUI();
	        break;
		default:
		case "gui":
			new GUI().startUI();
		}
	}
}
