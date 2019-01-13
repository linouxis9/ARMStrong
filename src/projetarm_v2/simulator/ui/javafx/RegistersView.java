package projetarm_v2.simulator.ui.javafx;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import projetarm_v2.simulator.boilerplate.ArmSimulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dockfx.DockNode;
import org.dockfx.DockPane;

public class RegistersView {

	private TabPane mainPane;
	private DockNode dockNode;
	private Image dockImage;
	private ArrayList<Text> registersHex;
	private ArrayList<Text> registersSigHex;
	private ArrayList<Text> registersDec;
	private ArmSimulator simulator;
	private Pane hexPane;
	private Pane sigHexPane;
	private Pane decPane;
	private Tab hexTab;
	private Tab sigHexTab;
	private Tab decTab;

	public RegistersView(ArmSimulator simulator) {
		// dockImage = new
		// Image(Gui.class.getResource("docknode.png").toExternalForm());
		this.simulator = simulator;

		this.hexPane = new Pane();
		this.sigHexPane = new Pane();
		this.decPane = new Pane();

		this.mainPane = new TabPane();
		this.hexTab = new Tab("Hexa", hexPane);
		this.sigHexTab = new Tab("Sig Hexa", sigHexPane);
		this.decTab = new Tab("Decimal", decPane);
		this.mainPane.getTabs().addAll(hexTab, sigHexTab, decTab);

		this.dockNode = new DockNode(mainPane, "Register View", new ImageView(dockImage));
		this.dockNode.setPrefSize(300, 100);

		this.registersHex = new ArrayList<>();
		this.registersSigHex = new ArrayList<>();
		this.registersDec = new ArrayList<>();

		updateRegisters();

	}

	public void updateRegisters() {
		
		for (int i = 0; i < this.registersHex.size(); i++) {
			this.hexPane.getChildren().remove(this.registersHex.get(i));
			this.sigHexPane.getChildren().remove(this.registersSigHex.get(i));
			this.decPane.getChildren().remove(this.registersDec.get(i));
		}
		
		this.registersHex.clear();
		this.registersSigHex.clear();
		this.registersDec.clear();

		for (int i = 0; i < 16; i++) {
			Text registerHex = new Text(10, 20 * (i + 1),
					String.format("R%d : 0x%x", i, simulator.getRegisterValue(i)));
			this.registersHex.add(registerHex);

			Text registerDec = new Text(10, 20 * (i + 1), String.format("R%d : %d", i, simulator.getRegisterValue(i)));
			this.registersDec.add(registerDec);

			int valueDec = simulator.getRegisterValue(i);
			int contentSigHex = (valueDec > 32767) ? valueDec - 65536 : valueDec;
			Text registerSigHex = new Text(10, 20 * (i + 1), String.format("R%d : 0x%x", i, contentSigHex));
			this.registersSigHex.add(registerSigHex);
		}

		Text flagHex = new Text(10, 20 * (17), "[FLAGS] : " + simulator.getCpu().getCPSR().toString());
		Text flagSigHex = new Text(10, 20 * (17), "[FLAGS] : " + simulator.getCpu().getCPSR().toString());
		Text flagDec = new Text(10, 20 * (17), "[FLAGS] : " + simulator.getCpu().getCPSR().toString());

		this.registersHex.add(flagHex);
		this.registersSigHex.add(flagSigHex);
		this.registersDec.add(flagDec);

		this.hexPane.getChildren().addAll(this.registersHex);
		this.sigHexPane.getChildren().addAll(this.registersSigHex);
		this.decPane.getChildren().addAll(this.registersDec);

		this.hexTab.setContent(this.hexPane);
		this.sigHexTab.setContent(this.sigHexPane);
		this.decTab.setContent(this.decPane);
	}

	DockNode getNode() {
		return dockNode;
	}

}
