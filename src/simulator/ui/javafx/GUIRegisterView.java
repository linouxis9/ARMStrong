package simulator.ui.javafx;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import simulator.boilerplate.ArmSimulator;

import java.util.ArrayList;
import java.util.List;

public class GUIRegisterView {

	private ArmSimulator theArmSimulator;

	private List<Text> hexadecimalRegisterText;
	private List<Text> decimalRegisterText;
	private List<Text> signedDecimalRegisterText;

	public GUIRegisterView(Scene theScene, ArmSimulator anArmSimulator) {

		this.theArmSimulator = anArmSimulator;

		this.hexadecimalRegisterText = new ArrayList<Text>();
		this.decimalRegisterText = new ArrayList<Text>();
		this.signedDecimalRegisterText = new ArrayList<Text>();

		for (int register = 0; register < 16; register++) {
			this.hexadecimalRegisterText.add((Text) theScene.lookup("#register" + register + "Hex"));
			this.decimalRegisterText.add((Text) theScene.lookup("#register" + register + "UDec"));
			this.signedDecimalRegisterText.add((Text) theScene.lookup("#register" + register + "Dec"));
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
