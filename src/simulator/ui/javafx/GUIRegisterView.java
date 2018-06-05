package simulator.ui.javafx;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.scene.text.Text;
import simulator.boilerplate.ArmSimulator;

import java.util.ArrayList;
import java.util.List;

public class GUIRegisterView {

	private ArmSimulator theArmSimulator;

	private List<Text> hexadecimalRegisterText;
	private List<Text> decimalRegisterText;
	private List<Text> signedDecimalRegisterText;

	private Scene theScene;
	
	public GUIRegisterView(Scene theScene, ArmSimulator anArmSimulator) {

		this.theArmSimulator = anArmSimulator;

		this.theScene = theScene;
		
		this.hexadecimalRegisterText = new ArrayList<Text>();
		this.decimalRegisterText = new ArrayList<Text>();
		this.signedDecimalRegisterText = new ArrayList<Text>();

		for (int register = 0; register < 16; register++) {
			this.hexadecimalRegisterText.add((Text) theScene.lookup("#register" + register + "Hex"));
			this.decimalRegisterText.add((Text) theScene.lookup("#register" + register + "UDec"));
			this.signedDecimalRegisterText.add((Text) theScene.lookup("#register" + register + "Dec"));
		}
		
		translate();
	}

	public void translate() {
		if(GUI.language.equals("French")) {
			((TitledPane) this.theScene.lookup("#unsignedDecimal")).setText("Décimal non signé");
			((TitledPane) this.theScene.lookup("#signedDecimal")).setText("Décimal signé");
			((TitledPane) this.theScene.lookup("#hexadecimal")).setText("Hexadécimal");
		}else{
			((TitledPane) this.theScene.lookup("#unsignedDecimal")).setText("Unsigned Decimal");
			((TitledPane) this.theScene.lookup("#signedDecimal")).setText("Signed Decimal");
			((TitledPane) this.theScene.lookup("#hexadecimal")).setText("Hexadecimal");
		}		
		
	}
	
	/**
	 * Update the displayed registers
	 */
	public void updateRegisters() {

		int currentRegisterValue;

		for (int currentRegisterNumber = 0; currentRegisterNumber < 16; currentRegisterNumber++) {
			currentRegisterValue = theArmSimulator.getRegisterValue(currentRegisterNumber);
			hexadecimalRegisterText.get(currentRegisterNumber).setText("0x" + Integer.toHexString(currentRegisterValue));
			decimalRegisterText.get(currentRegisterNumber).setText(Integer.toUnsignedString(currentRegisterValue));
			signedDecimalRegisterText.get(currentRegisterNumber).setText(Integer.toString(currentRegisterValue));
		}
	}
}
