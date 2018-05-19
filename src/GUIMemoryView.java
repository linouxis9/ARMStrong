import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.omg.CORBA.Any;
import simulator.Ram;

import java.util.ArrayList;
import java.util.List;

public class GUIMemoryView {

    ArmSimulator theArmSimulator;

    TextField goToAddressField;

    private Button button8bitView;
    private Button button16bitView;
    private Button button32bitView;

    private List<Text> MemoryAddressList;
    private List<Text> MemoryContentList;
    private int memoryViewFirstAddress;
    private int memoryDisplayMode;

    int displayableMemoryRows;

    public GUIMemoryView(Scene theScene, ArmSimulator anArmSimulator){

        theArmSimulator = anArmSimulator;

        AnchorPane memoryPane = (AnchorPane) theScene.lookup("#memoryViewPane");

        goToAddressField = (TextField) theScene.lookup("#goToAddressField");

        button8bitView 	= (Button) theScene.lookup("#button8Bit");
        button16bitView = (Button) theScene.lookup("#button16Bit");
        button32bitView = (Button) theScene.lookup("#button32Bit");

        this.MemoryAddressList 	= new ArrayList<Text>();
        this.MemoryContentList 		= new ArrayList<Text>();

        MemoryAddressList.add((Text) theScene.lookup("#memoryAddress"));
        MemoryContentList.add((Text) theScene.lookup("#memoryContent"));

        displayableMemoryRows = (int)(((int) memoryPane.getHeight())/5.5);

        for(int address=1; address<displayableMemoryRows; address++){
            MemoryAddressList.add(new Text(MemoryAddressList.get(0).getBoundsInParent().getMinX(),MemoryAddressList.get(0).getBoundsInParent().getMinY()+(address*20)+13, "0x00000000"));
            MemoryContentList.add(new Text(MemoryContentList.get(0).getBoundsInParent().getMinX(),MemoryContentList.get(0).getBoundsInParent().getMinY()+(address*20)+13, "0x00000000"));
            memoryPane.getChildren().add(MemoryAddressList.get(address));
            memoryPane.getChildren().add(MemoryContentList.get(address));
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


        ScrollBar scrollBar = (ScrollBar) theScene.lookup("#memoryScrollBar");
        scrollBar.setMax(0);


        scrollBar.setMax(Ram.DEFAULT_SIZE-displayableMemoryRows); //to update when a "redimentionnement" is done
        scrollBar.setUnitIncrement(1);                             //change the value when the display mode changes (8bit, 16, 32)

        scrollBar.setValue(0);

        scrollBar.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent scrollEvent) {
                memoryViewFirstAddress = (int) scrollBar.getValue();
                updateMemoryView();
            }
        });

        /*
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
        */

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

                    int newAddress = 0;

                    try {
                        if (addressTyped.startsWith("0x") || addressTyped.startsWith("0X")) {
                            newAddress = Integer.parseInt(addressTyped.substring(2), 16); //parsing a int in base 16, the 2 first chars of the string are removed (0x)
                        } else if (addressTyped.startsWith("0b") || addressTyped.startsWith("0B")) {
                            newAddress = Integer.parseInt(addressTyped.substring(2), 2); //parsing a int in base 2, the 2 first chars of the string are removed (0b)
                        } else if (addressTyped.startsWith("0d") || addressTyped.startsWith("0D")) {
                            newAddress = Integer.parseInt(addressTyped.substring(2), 10); //parsing a int in base 10, the 2 first chars of the string are removed (0b)
                        } else {
                            newAddress = Integer.parseInt(addressTyped);
                        }
                        if(newAddress<0 || newAddress>Ram.DEFAULT_SIZE-displayableMemoryRows){
                            MemoryViewTitleText.setText("The address too low or to high");
                            MemoryViewTitleText.setUnderline(true);
                            return;
                        }
                        memoryViewFirstAddress = newAddress;
                        updateMemoryView();
                        scrollBar.setValue(memoryViewFirstAddress);
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
        updateMemoryView();
    }


    /**
     * updates the memory view
     */
    public void updateMemoryView(){

        int displayedMemoryAddress = memoryViewFirstAddress;

        switch(this.memoryDisplayMode){
            case 8:
                for (int labelNumber=0 ; labelNumber < displayableMemoryRows; labelNumber++){
                    MemoryAddressList.get(labelNumber).	setText("0x" + Integer.toHexString(displayedMemoryAddress));
                    MemoryContentList.get(labelNumber).		setText(""+theArmSimulator.getRamByte(displayedMemoryAddress));
                    displayedMemoryAddress++;
                }
                break;
            case 16:
                for (int labelNumber=0 ; labelNumber < displayableMemoryRows; labelNumber++){
                    MemoryAddressList.get(labelNumber).setText("0x" +Integer.toHexString(displayedMemoryAddress));
                    MemoryContentList.get(labelNumber).setText(""+theArmSimulator.getRamHWord(displayedMemoryAddress));
                    displayedMemoryAddress+=2;
                }
                break;
            case 32:
                for (int labelNumber=0 ; labelNumber < displayableMemoryRows; labelNumber++) {
                    MemoryAddressList.get(labelNumber).setText("0x"+Integer.toHexString(displayedMemoryAddress));
                    MemoryContentList.get(labelNumber).setText("" +theArmSimulator.getRamWord(displayedMemoryAddress));
                    displayedMemoryAddress += 4;
                }
                break;
            default:
                this.memoryDisplayMode=8;
        }
    }
}
