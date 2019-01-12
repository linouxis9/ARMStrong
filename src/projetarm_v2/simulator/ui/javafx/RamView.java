package projetarm_v2.simulator.ui.javafx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.dockfx.DockNode;
import projetarm_v2.simulator.boilerplate.ArmSimulator;
import simulator.core.Ram;

import java.io.IOException;
import java.util.ArrayList;

public class RamView {

    private static final int VERTICAL_SPACE_BETWEEN_TEXT = 10;

    private AnchorPane mainPane;
    private DockNode dockNode;
    private Image dockImage;

    private ArmSimulator simulator;

    private ScrollBar memoryScrollBar;

    private ArrayList<Text> memoryAddresses;
    private ArrayList<Text> memoryValues;

    private int firstDisplayedAdress = 0;
    private int memoryDisplayMode = 8;
    private int displayedLines;

    public RamView(ArmSimulator simulator){
        //dockImage = new Image(Gui.class.getResource("docknode.png").toExternalForm());
        try {
            mainPane = FXMLLoader.load(getClass().getResource("/resources/MemoryView.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.simulator = simulator;

        dockNode = new DockNode(mainPane, "Ram View", new ImageView(dockImage));
        dockNode.setPrefSize(300, 100);

        memoryAddresses = new ArrayList<Text>();
        memoryValues = new ArrayList<Text>();

        memoryAddresses.add((Text) mainPane.lookup("#addressLabel"));
        memoryValues.add((Text) mainPane.lookup("#valueLabel"));

        loadLabels(100);

        mainPane.heightProperty().addListener((obs, oldVal, newVal) -> {
            displayLablels();
        });
        updateContents();

        loadButonsEvents();

    }

    private void updateContents() {
        int displayedMemoryRows = memoryAddresses.size();
        int displayedMemoryAddress = firstDisplayedAdress;
        for (int labelNumber = 0; labelNumber < displayedMemoryRows; labelNumber++) {
            String address = Integer.toHexString(displayedMemoryAddress);
            String content;
            switch (this.memoryDisplayMode) {
                case 8:
                    content = Integer.toHexString(this.simulator.getRamByte(displayedMemoryAddress));
                    content = "00".substring(content.length()) + content;
                    displayedMemoryAddress++;
                    break;
                case 16:
                    content = Integer.toHexString(this.simulator.getRamHWord(displayedMemoryAddress));
                    content = "0000".substring(content.length()) + content;
                    content = content.subSequence(0, 2) + " " + content.subSequence(2, 4);
                    displayedMemoryAddress += 2;
                    break;
                case 32:
                    content = Integer.toHexString(this.simulator.getRamWord(displayedMemoryAddress));
                    content = "00000000".substring(content.length()) + content;
                    content = content.subSequence(0, 2) + " " + content.subSequence(2, 4) + " " + content.subSequence(4, 6) + " " + content.subSequence(6, 8);
                    displayedMemoryAddress += 4;
                    break;
                default:
                    content = "--------";
            }
            memoryAddresses.get(labelNumber).setText("00000000".substring(address.length()) + address);
            memoryValues.get(labelNumber).setText(content);
        }
    }

    private void loadLabels(int number){
        double xAddressPos = this.memoryAddresses.get(0).getBoundsInParent().getMinX();
        double xValuePos = this.memoryValues.get(0).getBoundsInParent().getMinX();
        double yPos = this.memoryAddresses.get(0).getBoundsInParent().getMinY();
        double spaceForLabel = this.memoryAddresses.get(0).getLayoutBounds().getHeight() + VERTICAL_SPACE_BETWEEN_TEXT;
        double nextPos = yPos + spaceForLabel + VERTICAL_SPACE_BETWEEN_TEXT; //add VERTICAL_SPACE_BETWEEN_TEXT
        for(int i = 1; i<number; i++){
            this.memoryAddresses.add(new Text(xAddressPos, nextPos, "--------"));
            this.memoryValues.add(new Text(xValuePos, nextPos, "----------"));
            nextPos += spaceForLabel;
        }

    }

    public void displayLablels(){
        double spaceForLabel = this.memoryAddresses.get(0).getLayoutBounds().getHeight() + VERTICAL_SPACE_BETWEEN_TEXT;
        double firstLabelYPos = this.memoryAddresses.get(0).getBoundsInParent().getMinY();
        //Removing the Texts in the interface
        for(int i = 0; i< memoryAddresses.size(); i++){
            try{
                mainPane.getChildren().remove(memoryAddresses.get(i));
                mainPane.getChildren().remove(memoryValues.get(i));
            } catch (Exception e){}
        }
        double paneHeight = mainPane.getHeight(); //THE HEIGHT IS NOT UPDATED IN THE MAIN PANE

        int displayableLines = (int)((paneHeight - firstLabelYPos - VERTICAL_SPACE_BETWEEN_TEXT*2)/ spaceForLabel);
        for(int i = 0; i<displayableLines; i++){
            mainPane.getChildren().add(this.memoryAddresses.get(i));
            mainPane.getChildren().add(this.memoryValues.get(i));
        }
        this.displayedLines = displayableLines;
    }
    
    private void loadButonsEvents() {
        memoryScrollBar = (ScrollBar) mainPane.lookup("#memoryScrollBar");
        memoryScrollBar.setMin(0);
        memoryScrollBar.setValue(0);
        memoryScrollBar.setUnitIncrement(1);
        memoryScrollBar.setMax(2 *1024*1024); //TODO: set proper value

        memoryScrollBar.setOnScroll((ScrollEvent scrollEvent) -> {
            this.firstDisplayedAdress = (int) memoryScrollBar.getValue();
            updateContents();
        });
        this.mainPane.setOnScroll((ScrollEvent scrollEvent) -> {
            if (scrollEvent.getDeltaY() < 0) {
                updateNewFirstAddress(-1);
            }
            else {
                updateNewFirstAddress(1);
            }
            updateContents();
        });

        Button button8Bit = (Button) mainPane.lookup("#button8Bit");
    	Button button16Bit = (Button) mainPane.lookup("#button16Bit");
    	Button button32Bit = (Button) mainPane.lookup("#button32Bit");
    	
    	button8Bit.setOnMouseClicked((MouseEvent mouseEvent) -> {
    		this.memoryDisplayMode = 8;
            memoryScrollBar.setUnitIncrement(1);
            firstDisplayedAdress = firstDisplayedAdress - firstDisplayedAdress % (memoryDisplayMode/8);
            updateContents();
    	});
    	button16Bit.setOnMouseClicked((MouseEvent mouseEvent) -> {
    		this.memoryDisplayMode = 16;
            memoryScrollBar.setUnitIncrement(2);
            firstDisplayedAdress = firstDisplayedAdress - firstDisplayedAdress % (memoryDisplayMode/8);
            updateContents();
    	});
    	button32Bit.setOnMouseClicked((MouseEvent mouseEvent) -> {
    		this.memoryDisplayMode = 32;
            memoryScrollBar.setUnitIncrement(4);
            firstDisplayedAdress = firstDisplayedAdress - firstDisplayedAdress % (memoryDisplayMode/8);
    		updateContents();
    	});

        Text memoryViewTitleText = (Text) mainPane.lookup("#goToAddressLabel");
        TextField goToAddressField = (TextField) this.mainPane.lookup("#goToAddressField");
        goToAddressField.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                String addressTyped = goToAddressField.getText();
                int newAddress = 0;
                try {
                    newAddress = parseUserInput(addressTyped);
                } catch (NumberFormatException exeption) {
                    memoryViewTitleText.setText("invalidAddress");
                    memoryViewTitleText.setUnderline(true);
                    return;
                }

                if (newAddress < 0 || newAddress > Ram.DEFAULT_SIZE - displayedLines) {
                    memoryViewTitleText.setText("invalidAddress");
                    memoryViewTitleText.setUnderline(true);
                    return;
                }
                firstDisplayedAdress = newAddress;
                memoryScrollBar.setValue(firstDisplayedAdress);
                memoryViewTitleText.setText("goToAddress:");
                memoryViewTitleText.setUnderline(false);
                updateContents();
            }
        });

        for(int i=0; i< memoryValues.size(); i++){
            memoryValues.get(i).setOnMouseClicked((MouseEvent mouseEvent) -> {
                Text text = (Text)mouseEvent.getSource();
                int row = memoryValues.indexOf(text);
                TextField textField = new TextField(text.getText());
                mainPane.getChildren().add(textField);
                this.mainPane.setTopAnchor(textField, text.getBoundsInParent().getMinY());
                this.mainPane.setLeftAnchor(textField, text.getBoundsInParent().getMinX());

                textField.focusedProperty().addListener((new ChangeListener<Boolean>(){
                    @Override
                    public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
                    {
                        if (!newPropertyValue){
                            mainPane.getChildren().remove(textField);
                        }
                    }
                }));
                textField.setOnKeyPressed((KeyEvent ke) -> {
                    if (ke.getCode().equals(KeyCode.ENTER)) {
                        int address = firstDisplayedAdress+row;
                        int value = 0;
                        try {
                            value = parseUserInput(textField.getText());
                        }catch (Exception e){
                            errorPopup("Invalid value");
                            return;
                        }
                        switch (memoryDisplayMode){
                            case 8:
                                if(value>256){
                                    errorPopup("Value too big for a byte (change memory view mode to enter bigger numbers");
                                }
                                //simulator.setByte(address, value);
                                break;
                            case 16:
                                if(value>512){
                                    errorPopup("Value too big for a HalfWord (change memory view mode to enter bigger numbers");
                                }
                                //simulator.setHWord(address, value);
                                break;
                            case 32:
                                //simulator.setWord(address, value);
                                break;
                        }
                        mainPane.getChildren().remove(textField);
                    } else if(ke.getCode().equals(KeyCode.ESCAPE)){
                        mainPane.getChildren().remove(textField);
                    }
                    updateContents();
                });

            });
        }

    }

    private void errorPopup(String s) {
        System.out.println(s);
    }

    static int parseUserInput(String input) throws NumberFormatException{
        int parsedNumber;
        if (input.startsWith("0x") || input.startsWith("0X")) {
            parsedNumber = Integer.parseInt(input.substring(2), 16); // parsing a int in base 16, the 2

        } else if (input.startsWith("0b") || input.startsWith("0B")) {
            parsedNumber = Integer.parseInt(input.substring(2), 2); // parsing a int in base 2, the 2

        } else if (input.startsWith("0d") || input.startsWith("0D")) {
            parsedNumber = Integer.parseInt(input.substring(2)); // parsing a int in base 10, the 2

        } else {
            parsedNumber = Integer.parseInt(input);
        }
        return parsedNumber;
    }

    private void updateNewFirstAddress(int delta) {
        int oldAddress = this.firstDisplayedAdress;

        switch (this.memoryDisplayMode) {
            case 8:
                this.firstDisplayedAdress += -delta;
                break;
            case 16:
                this.firstDisplayedAdress += -2 * delta;
                break;
            case 32:
                this.firstDisplayedAdress += -4 * delta;
                break;
            default:
                this.memoryDisplayMode = 8;
        }

        if (firstDisplayedAdress < 0 || firstDisplayedAdress > Ram.DEFAULT_SIZE) {
            this.firstDisplayedAdress = oldAddress;
        }
    }

    public DockNode getNode() {
        return dockNode;
    }
}
