package simulator.ui.javafx;
import com.sun.xml.internal.ws.util.StringUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import simulator.boilerplate.ArmSimulator;
import simulator.core.Ram;

import org.omg.CORBA.Any;

import java.util.ArrayList;
import java.util.List;

public class GUIMemoryView {

	private ArmSimulator theArmSimulator;
	
	private Scene theScene;

	private TextField goToAddressField;

	private Button button8bitView;
	private Button button16bitView;
	private Button button32bitView;

	private ScrollBar scrollBar;

	AnchorPane memoryPane;

	private List<Text> MemoryAddressList;
	private List<Text> MemoryContentList;
	private int memoryViewFirstAddress;
	private int memoryDisplayMode;

	private int displayableMemoryRows;
	private int displayedMemoryRows;
	
	private Text memoryViewTitleText;

	public GUIMemoryView(Scene theScene, ArmSimulator anArmSimulator) {

		this.theArmSimulator = anArmSimulator;
		
		this.theScene = theScene;
		
		memoryViewTitleText = (Text) theScene.lookup("#goToAddressLabel");
		
		this.memoryPane = (AnchorPane) theScene.lookup("#memoryViewPane");

		this.goToAddressField = (TextField) theScene.lookup("#goToAddressField");

		this.button8bitView = (Button) theScene.lookup("#button8Bit");
		this.button16bitView = (Button) theScene.lookup("#button16Bit");
		this.button32bitView = (Button) theScene.lookup("#button32Bit");

		this.MemoryAddressList = new ArrayList<Text>();
		this.MemoryContentList = new ArrayList<Text>();

		this.MemoryAddressList.add((Text) theScene.lookup("#memoryAddress"));
		this.MemoryContentList.add((Text) theScene.lookup("#memoryContent"));
		
		button8bitView.setOnAction((ActionEvent e) -> {
			memoryDisplayMode = 8;
			updateMemoryView();
		});
		button16bitView.setOnAction((ActionEvent e) -> {
			memoryDisplayMode = 16;
			updateMemoryView();
		});
		button32bitView.setOnAction((ActionEvent e) -> {
			memoryDisplayMode = 32;
			updateMemoryView();
		});

		memoryPane.setOnScroll((ScrollEvent scrollEvent) -> {
			if (scrollEvent.getDeltaY() < 0) {
				updateNewFirstAddress(-1);
			}
			else {
				updateNewFirstAddress(1);
			}
			updateMemoryView();
		});

		memoryPane.heightProperty().addListener((obs, oldVal, newVal) -> {
			updateDisplayedRows();
		});

		scrollBar = (ScrollBar) theScene.lookup("#memoryScrollBar");

		scrollBar.setMin(0);
		scrollBar.setValue(0);
		scrollBar.setUnitIncrement(1);

		scrollBar.setMax(Ram.DEFAULT_SIZE - displayableMemoryRows);

		scrollBar.setOnScroll((ScrollEvent scrollEvent) -> {
			memoryViewFirstAddress = (int) scrollBar.getValue();
			updateMemoryView();
		});
		scrollBar.setOnMouseClicked((MouseEvent mouseEvent) -> {
			memoryViewFirstAddress = (int) scrollBar.getValue();
			updateMemoryView();
		});

		goToAddressField.setOnKeyPressed((KeyEvent ke) -> {
			if (ke.getCode().equals(KeyCode.ENTER)) {
				String addressTyped = goToAddressField.getText();
				
				if(GUI.language.equals("French")) {
					this.memoryViewTitleText.setText("Aller à l'adresse: ");
				}
				else {
					this.memoryViewTitleText.setText("Go to address: ");
				}

				int newAddress = 0;

				try {
					if (addressTyped.startsWith("0x") || addressTyped.startsWith("0X")) {
						newAddress = Integer.parseInt(addressTyped.substring(2), 16); // parsing a int in base 16, the 2
						// first chars of the string are
						// removed (0x)
					} else if (addressTyped.startsWith("0b") || addressTyped.startsWith("0B")) {
						newAddress = Integer.parseInt(addressTyped.substring(2), 2); // parsing a int in base 2, the 2
						// first chars of the string are
						// removed (0b)
					} else if (addressTyped.startsWith("0d") || addressTyped.startsWith("0D")) {
						newAddress = Integer.parseInt(addressTyped.substring(2)); // parsing a int in base 10, the 2
						// first chars of the string are
						// removed (0b)
					} else {
						newAddress = Integer.parseInt(addressTyped);
					}
				} catch (NumberFormatException exeption) {
					
					if(GUI.language.equals("French")) {
						memoryViewTitleText.setText("Adresse invalide");
					}else{
						memoryViewTitleText.setText("The address is invalid");
					}
					memoryViewTitleText.setUnderline(true);
					return;
				}

				if (newAddress < 0 || newAddress > Ram.DEFAULT_SIZE - displayableMemoryRows) {
					if(GUI.language.equals("French")) {
						memoryViewTitleText.setText("Adresse invalide");
					}else {
						memoryViewTitleText.setText("The address is invalid");
					}
					memoryViewTitleText.setUnderline(true);
					return;
				}
				memoryViewTitleText.setUnderline(false);
				memoryViewFirstAddress = newAddress;
				scrollBar.setValue(memoryViewFirstAddress);
				updateMemoryView();
			}
		});

		displayedMemoryRows = 1;
		memoryDisplayMode = 8;
		memoryViewFirstAddress = 0;
		updateDisplayedRows();
		updateMemoryView();
		
		translate();
	}

	public void translate() {
		
		if(GUI.language.equals("French")) {
			this.memoryViewTitleText.setText("Aller à l'adresse: ");
		}
		else {
			this.memoryViewTitleText.setText("Go to address: ");
		}

		memoryViewTitleText.setUnderline(false);
		
		if(GUI.language.equals("French")) {
			((Text) this.theScene.lookup("#viewModeLabel")).setText("Mode d'affichage: ");
			
		}else{
			((Text) this.theScene.lookup("#viewModeLabel")).setText("View mode: ");
		}
		
	}
	
	
	private void updateNewFirstAddress(int delta) {
		int oldAddress = memoryViewFirstAddress;
		
		switch (this.memoryDisplayMode) {
		case 8:
			this.memoryViewFirstAddress += -delta;
			break;
		case 16:
			this.memoryViewFirstAddress += -2 * delta;
			break;
		case 32:
			this.memoryViewFirstAddress += -4 * delta;
			break;
		default:
			this.memoryDisplayMode = 8;
		}
		
		if (memoryViewFirstAddress < 0 || memoryViewFirstAddress > Ram.DEFAULT_SIZE) {
			this.memoryViewFirstAddress = oldAddress;
		}
	}

	/**
	 * updates the memory view
	 */
	public void updateMemoryView() {
		alignMemoryAddress();

		int displayedMemoryAddress = memoryViewFirstAddress;

		scrollBar.setUnitIncrement(this.memoryDisplayMode/8);

		for (int labelNumber = 0; labelNumber < displayedMemoryRows; labelNumber++) {
			String address = Integer.toHexString(displayedMemoryAddress);
			String content;
			switch (this.memoryDisplayMode) {
				case 8:
					content = Integer.toHexString(theArmSimulator.getRamByte(displayedMemoryAddress));
					content = "00".substring(content.length())+content;
					displayedMemoryAddress++;
					break;
				case 16:
					content = Integer.toHexString(theArmSimulator.getRamHWord(displayedMemoryAddress));
					content = "0000".substring(content.length())+content;
					content = content.subSequence(0, 2) + " " + content.subSequence(2, 4);
					displayedMemoryAddress += 2;
					break;
				case 32:
					content = Integer.toHexString(theArmSimulator.getRamWord(displayedMemoryAddress));
					content = "00000000".substring(content.length())+content;
					content = content.subSequence(0, 2) + " " + content.subSequence(2, 4) + " " + content.subSequence(4, 6) + " " + content.subSequence(6, 8);
					displayedMemoryAddress += 4;
					break;
				default:
					content = "--------";

			}
			MemoryAddressList.get(labelNumber).setText("00000000".substring(address.length())+address);
			MemoryContentList.get(labelNumber).setText(content);
		}
	}

	private void alignMemoryAddress() {
		if (this.memoryDisplayMode == 16) {
			memoryViewFirstAddress = memoryViewFirstAddress - memoryViewFirstAddress % 2;
		} else if (this.memoryDisplayMode == 32) {
			memoryViewFirstAddress = memoryViewFirstAddress - memoryViewFirstAddress % 4;
		}
		//TODO or memoryViewFirstAddress = memoryViewFirstAddress - memoryViewFirstAddress % (memoryDisplayMode/8);
	}

	private void updateDisplayedRows(){
		displayableMemoryRows = (((int) memoryPane.getHeight()) / 27);
		if (displayableMemoryRows > displayedMemoryRows) { // there is space to display more lines
			if (displayableMemoryRows > MemoryAddressList.size()) { // there is not enough lines already loaded to use all space
				for (int address = MemoryAddressList.size(); address < displayableMemoryRows; address++) { // loading missing lines
					MemoryAddressList.add(new Text(MemoryAddressList.get(0).getBoundsInParent().getMinX(), MemoryAddressList.get(0).getBoundsInParent().getMinY() + (address * 20) + 13, "0x00000000"));
					MemoryContentList.add(new Text(MemoryContentList.get(0).getBoundsInParent().getMinX(), MemoryContentList.get(0).getBoundsInParent().getMinY() + (address * 20) + 13, "0x00000000"));
				}
			}
			for (int address = displayedMemoryRows; address < displayableMemoryRows; address++) { // display the loaded lines
				memoryPane.getChildren().add(MemoryAddressList.get(address));
				memoryPane.getChildren().add(MemoryContentList.get(address));
			}
			displayedMemoryRows = displayableMemoryRows;
		}

		if (displayableMemoryRows < displayedMemoryRows) { //remove the lines that can't be displayed
			for (int address = displayedMemoryRows - 1; address > displayableMemoryRows - 1; address--) {
				memoryPane.getChildren().remove(MemoryAddressList.get(address));
				memoryPane.getChildren().remove(MemoryContentList.get(address));
			}
			displayedMemoryRows = displayableMemoryRows;
		}
		updateMemoryView();
	}
	
	
}
