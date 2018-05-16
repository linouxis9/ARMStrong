import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class GUIMemoryView {

    ArmSimulator theArmSimulator;

    TextField goToAddressField;

    private Button button8bitView;
    private Button button16bitView;
    private Button button32bitView;
    private Button memoryButtonUp;
    private Button memoryButtonDown;
    private Button goToAddressButton;

    private List<Text> MemoryAddressViewList;
    private List<Text> MemoryContentList;
    private int memoryViewFirstAddress;
    private int memoryDisplayMode;

    public GUIMemoryView(Scene theScene, ArmSimulator anArmSimulator){

        theArmSimulator = anArmSimulator;

        goToAddressField = (TextField) theScene.lookup("#goToAddressField");

        button8bitView 	= (Button) theScene.lookup("#button8Bit");
        button16bitView = (Button) theScene.lookup("#button16Bit");
        button32bitView = (Button) theScene.lookup("#button32Bit");

        memoryButtonUp      = (Button) theScene.lookup("#memoryButtonUp");
        memoryButtonDown    = (Button) theScene.lookup("#memoryButtonDown");
        goToAddressButton   = (Button) theScene.lookup("#goToAddressButton");

        this.MemoryAddressViewList 	= new ArrayList<Text>();
        this.MemoryContentList 		= new ArrayList<Text>();

        for(int address=1; address<9; address++){
            MemoryAddressViewList	.add((Text) theScene.lookup("#addresslMemoryView"+address));
            MemoryContentList		.add((Text) theScene.lookup("#contentMemoryView"+address));
        }

        button8bitView.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                memoryDisplayMode=8;
                updateMemoryView();
            }
        });
        button16bitView.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                memoryDisplayMode = 16;
                updateMemoryView();
            }
        });
        button32bitView.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                memoryDisplayMode = 32;
                updateMemoryView();
            }
        });

        memoryButtonUp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                switch(memoryDisplayMode){
                    case 8:
                        memoryViewFirstAddress--;
                        break;
                    case 16:
                        memoryViewFirstAddress-=2;
                        break;
                    case 32:
                        memoryViewFirstAddress-=4;
                        break;
                }
                if (memoryViewFirstAddress<0){
                    memoryViewFirstAddress = 0;
                }
                updateMemoryView();
            }
        });
        memoryButtonDown.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                switch(memoryDisplayMode){
                    case 8:
                        memoryViewFirstAddress++;
                        break;
                    case 16:
                        memoryViewFirstAddress+=2;
                        break;
                    case 32:
                        memoryViewFirstAddress+=4;
                        break;
                }
                //TODO add max address check
                updateMemoryView();
            }
        });

        goToAddressButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String addressTyped = goToAddressField.getText();
                Text MemoryViewTitleText = (Text) theScene.lookup("#goToAddressLabel");
                MemoryViewTitleText.setText("Go to address: ");
                MemoryViewTitleText.setUnderline(false);

                try {
                    if (addressTyped.startsWith("0x") || addressTyped.startsWith("0X")) {
                        memoryViewFirstAddress = Integer.parseInt(addressTyped.substring(2), 16); //parsing a int in base 16, the 2 first chars of the string are removed (0x)
                    } else if (addressTyped.startsWith("0b") || addressTyped.startsWith("0B")) {
                        memoryViewFirstAddress = Integer.parseInt(addressTyped.substring(2), 2); //parsing a int in base 2, the 2 first chars of the string are removed (0b)
                    } else if (addressTyped.startsWith("0d") || addressTyped.startsWith("0D")) {
                        memoryViewFirstAddress = Integer.parseInt(addressTyped.substring(2), 10); //parsing a int in base 10, the 2 first chars of the string are removed (0b)
                    } else {
                        memoryViewFirstAddress = Integer.parseInt(addressTyped);
                    }
                    updateMemoryView();
                }
                catch (java.lang.NumberFormatException exeption){
                    MemoryViewTitleText.setText("The address is invalid");
                    MemoryViewTitleText.setUnderline(true);
                }

            }
        });
        goToAddressField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER))
                {
                    String addressTyped = goToAddressField.getText();
                    Text MemoryViewTitleText = (Text) theScene.lookup("#goToAddressLabel");
                    MemoryViewTitleText.setText("Go to address: ");
                    MemoryViewTitleText.setUnderline(false);

                    try {
                        if (addressTyped.startsWith("0x") || addressTyped.startsWith("0X")) {
                            memoryViewFirstAddress = Integer.parseInt(addressTyped.substring(2), 16); //parsing a int in base 16, the 2 first chars of the string are removed (0x)
                        } else if (addressTyped.startsWith("0b") || addressTyped.startsWith("0B")) {
                            memoryViewFirstAddress = Integer.parseInt(addressTyped.substring(2), 2); //parsing a int in base 2, the 2 first chars of the string are removed (0b)
                        } else if (addressTyped.startsWith("0d") || addressTyped.startsWith("0D")) {
                            memoryViewFirstAddress = Integer.parseInt(addressTyped.substring(2), 10); //parsing a int in base 10, the 2 first chars of the string are removed (0b)
                        } else {
                            memoryViewFirstAddress = Integer.parseInt(addressTyped);
                        }
                        updateMemoryView();
                    }
                    catch (java.lang.NumberFormatException exeption){
                        MemoryViewTitleText.setText("The address is invalid");
                        MemoryViewTitleText.setUnderline(true);
                    }
                }
            }
        });

        memoryDisplayMode = 8;
        memoryViewFirstAddress=0;
    }


    /**
     * updates the memory view
     */
    public void updateMemoryView(){
        int displayableMemoryRows = 8; //TODO temporaire!!!

        int displayedMemoryAddress = memoryViewFirstAddress;

        switch(this.memoryDisplayMode){
            case 8:
                for (int labelNumber=0 ; labelNumber < displayableMemoryRows; labelNumber++){
                    MemoryAddressViewList.get(labelNumber).	setText("0x" + Integer.toHexString(displayedMemoryAddress));
                    MemoryContentList.get(labelNumber).		setText(""+theArmSimulator.getRamByte(displayedMemoryAddress));
                    displayedMemoryAddress++;
                }
                break;
            case 16:
                for (int labelNumber=0 ; labelNumber < displayableMemoryRows; labelNumber++){
                    MemoryAddressViewList.get(labelNumber).setText("0x" +Integer.toHexString(displayedMemoryAddress));
                    MemoryContentList.get(labelNumber).setText(""+theArmSimulator.getRamHWord(displayedMemoryAddress));
                    displayedMemoryAddress+=2;
                }
                break;
            case 32:
                for (int labelNumber=0 ; labelNumber < displayableMemoryRows; labelNumber++) {
                    MemoryAddressViewList.get(labelNumber).setText("0x"+Integer.toHexString(displayedMemoryAddress));
                    MemoryContentList.get(labelNumber).setText("" +theArmSimulator.getRamWord(displayedMemoryAddress));
                    displayedMemoryAddress += 4;
                }
                break;
            default:
                this.memoryDisplayMode=8;
        }
    }
}
