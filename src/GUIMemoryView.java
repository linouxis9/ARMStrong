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

    private ScrollBar scrollBar;

    private List<Text> MemoryAddressList;
    private List<Text> MemoryContentList;
    private int memoryViewFirstAddress;
    private int memoryDisplayMode;

    int displayableMemoryRows;
    int displayedMemoryRows;

    public GUIMemoryView(Scene theScene, ArmSimulator anArmSimulator){

        this.theArmSimulator = anArmSimulator;

        AnchorPane memoryPane = (AnchorPane) theScene.lookup("#memoryViewPane");
        Button alignMemoryButton = (Button) theScene.lookup("#alignMemory");

        this.goToAddressField = (TextField) theScene.lookup("#goToAddressField");

        this.button8bitView  = (Button) theScene.lookup("#button8Bit");
        this.button16bitView = (Button) theScene.lookup("#button16Bit");
        this.button32bitView = (Button) theScene.lookup("#button32Bit");

        this.MemoryAddressList = new ArrayList<Text>();
        this.MemoryContentList = new ArrayList<Text>();

        this.MemoryAddressList.add((Text) theScene.lookup("#memoryAddress"));
        this.MemoryContentList.add((Text) theScene.lookup("#memoryContent"));

        displayableMemoryRows = (int)(((int) memoryPane.getHeight())/20);
        displayedMemoryRows = 1;

        button8bitView.setOnAction((ActionEvent e) -> {
                memoryDisplayMode=8;
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
                if (scrollEvent.getDeltaY()<0) updateNewFirstAddress(-1);
                else updateNewFirstAddress(1);
                updateMemoryView();
        });

        memoryPane.heightProperty().addListener((obs, oldVal, newVal) -> {
            displayableMemoryRows = (int)(((int) memoryPane.getHeight())/40);


            if(displayableMemoryRows > displayedMemoryRows) { //there is space to display more lines
                System.err.println("the number of displayed rows is"+displayedMemoryRows+" but there is space to display" + displayableMemoryRows+"rows");
                if (displayableMemoryRows > MemoryAddressList.size()) { //there is not enough lines already loaded to use all space
                    System.err.println("The number of loaded rows in the program is "+ MemoryAddressList.size() +" but we can display" + displayableMemoryRows);
                    for (int address = MemoryAddressList.size(); address < displayableMemoryRows; address++) { //loading missing lines
                        System.err.println("loading row:" + (address+1));
                        MemoryAddressList.add(new Text(MemoryAddressList.get(0).getBoundsInParent().getMinX(), MemoryAddressList.get(0).getBoundsInParent().getMinY() + (address * 20) + 13, "0x00000000"));
                        MemoryContentList.add(new Text(MemoryContentList.get(0).getBoundsInParent().getMinX(), MemoryContentList.get(0).getBoundsInParent().getMinY() + (address * 20) + 13, "0x00000000"));
                    }
                }
                for (int address = displayedMemoryRows; address < displayableMemoryRows; address++) { //display the loaded lines
                    System.err.println("displaying row:" + (address+1));
                    memoryPane.getChildren().add(MemoryAddressList.get(address));
                    memoryPane.getChildren().add(MemoryContentList.get(address));
                }
                displayedMemoryRows = displayableMemoryRows;
            }

            if(displayableMemoryRows < displayedMemoryRows) {
                System.err.println("the number of displayed rows is"+displayedMemoryRows+" but there is ONLY space to load" + displayableMemoryRows+"rows");
                for(int address=displayedMemoryRows-1; address>displayableMemoryRows-1; address--) {
                    System.err.println("removing row :"+ address);
                    memoryPane.getChildren().remove(MemoryAddressList.get(address));
                    memoryPane.getChildren().remove(MemoryContentList.get(address));
                }
                displayedMemoryRows = displayableMemoryRows;
            }
            //System.err.println(displayableMemoryRows);
        });

        scrollBar = (ScrollBar) theScene.lookup("#memoryScrollBar");

        scrollBar.setMin(0);
        scrollBar.setValue(0);
        scrollBar.setUnitIncrement(1);

        scrollBar.setMax(Ram.DEFAULT_SIZE-displayableMemoryRows);


        //TODO how can I reduce this?
        scrollBar.setOnScroll((ScrollEvent scrollEvent) -> {
                memoryViewFirstAddress = (int) scrollBar.getValue();
                updateMemoryView();
        });
        scrollBar.setOnDragDetected((MouseEvent mouseEvent) -> {
                memoryViewFirstAddress = (int) scrollBar.getValue();
                updateMemoryView();
        });
        scrollBar.setOnMouseClicked((MouseEvent mouseEvent) -> {
            memoryViewFirstAddress = (int) scrollBar.getValue();
            updateMemoryView();
        });

        goToAddressField.setOnKeyPressed((KeyEvent ke) -> {
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
        });

        alignMemoryButton.setOnAction((ActionEvent e) ->{
            if(this.memoryDisplayMode == 16 && this.memoryViewFirstAddress%2 != 0) memoryViewFirstAddress-=1;
            if(this.memoryDisplayMode == 32) while(this.memoryViewFirstAddress%4 != 0) memoryViewFirstAddress-=1;
        });

        memoryDisplayMode = 8;
        memoryViewFirstAddress=0;
        updateMemoryView();
    }




    private void updateNewFirstAddress(int delta){
        switch (this.memoryDisplayMode){
            case 8:
                this.memoryViewFirstAddress+=-delta;
                break;
            case 16:
                this.memoryViewFirstAddress+=-2*delta;
                break;
            case 32:
                this.memoryViewFirstAddress+=-4*delta;
                break;
            default:
                this.memoryDisplayMode = 8;
        }
    }

    /**
     * updates the memory view
     */
    // TODO Maybe you could do something less redundant?
    public void updateMemoryView(){

        int displayedMemoryAddress = memoryViewFirstAddress;

        switch(this.memoryDisplayMode){
            case 8:
                scrollBar.setUnitIncrement(1);
                for (int labelNumber=0 ; labelNumber < displayedMemoryRows; labelNumber++){
                    MemoryAddressList.get(labelNumber).	setText("0x" + Integer.toHexString(displayedMemoryAddress));
                    MemoryContentList.get(labelNumber).		setText(""+theArmSimulator.getRamByte(displayedMemoryAddress));
                    displayedMemoryAddress++;
                }
                break;
            case 16:
                scrollBar.setUnitIncrement(2);
                for (int labelNumber=0 ; labelNumber < displayedMemoryRows; labelNumber++){
                    MemoryAddressList.get(labelNumber).setText("0x" +Integer.toHexString(displayedMemoryAddress));
                    MemoryContentList.get(labelNumber).setText(""+theArmSimulator.getRamHWord(displayedMemoryAddress));
                    displayedMemoryAddress+=2;
                }
                break;
            case 32:
                scrollBar.setUnitIncrement(4);
                for (int labelNumber=0 ; labelNumber < displayedMemoryRows; labelNumber++) {
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
