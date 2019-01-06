package projetarm_v2.simulator.ui.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.dockfx.DockNode;
import projetarm_v2.simulator.core.Ram;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RamView {

    private static final int VERTICAL_SPACE_BETWEEN_TEXT = 10;

    private Pane mainPane;
    private DockNode dockNode;
    private Image dockImage;

    private ScrollBar memoryScrollBar;

    private ArrayList<Text> memoryAdresses;
    private ArrayList<Text> memoryValues;

    private int firstDisplayedAdress = 0;
    private int memoryDisplayMode = 8;

    public RamView(){
        //dockImage = new Image(Gui.class.getResource("docknode.png").toExternalForm());
        try {
            mainPane = FXMLLoader.load(getClass().getResource("/resources/MemoryView.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        dockNode = new DockNode(mainPane, "Ram View", new ImageView(dockImage));
        dockNode.setPrefSize(300, 100);

        memoryAdresses = new ArrayList<Text>();
        memoryValues = new ArrayList<Text>();

        memoryScrollBar = (ScrollBar) mainPane.lookup("#memoryScrollBar");
        memoryScrollBar.setMin(0);
        memoryScrollBar.setValue(0);
        memoryScrollBar.setUnitIncrement(1);
        memoryScrollBar.setMax(2 *1024*1024);

        memoryScrollBar.setOnScroll((ScrollEvent scrollEvent) -> {
            firstDisplayedAdress = (int) memoryScrollBar.getValue();
            updateContents();
        });
        memoryScrollBar.setOnMouseClicked((MouseEvent mouseEvent) -> {
            firstDisplayedAdress = (int) memoryScrollBar.getValue();
            updateContents();
        });

        mainPane.heightProperty().addListener((obs, oldVal, newVal) -> {
            displayLablels();
            updateContents();
        });

    }

    private void updateNewFirstAddress(int delta) {
        int oldAddress = firstDisplayedAdress;

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

        if (firstDisplayedAdress < 0 || firstDisplayedAdress > 2 * 1024 * 1024) {
            this.firstDisplayedAdress = oldAddress;
        }
    }

    private void updateContents() {
        int displayedMemoryRows = memoryAdresses.size();
        int displayedMemoryAddress = firstDisplayedAdress;
        for (int labelNumber = 0; labelNumber < displayedMemoryRows; labelNumber++) {
            String address = Integer.toHexString(firstDisplayedAdress+labelNumber);
            String content;
            switch (this.memoryDisplayMode) {
                case 8:
                    content = Integer.toHexString(00);
                    content = "00".substring(content.length()) + content;
                    displayedMemoryAddress++;
                    break;
                case 16:
                    content = Integer.toHexString(00);
                    content = "0000".substring(content.length()) + content;
                    content = content.subSequence(0, 2) + " " + content.subSequence(2, 4);
                    displayedMemoryAddress += 2;
                    break;
                case 32:
                    content = Integer.toHexString(00);
                    content = "00000000".substring(content.length()) + content;
                    content = content.subSequence(0, 2) + " " + content.subSequence(2, 4) + " " + content.subSequence(4, 6) + " " + content.subSequence(6, 8);
                    displayedMemoryAddress += 4;
                    break;
                default:
                    content = "--------";

            }
            System.out.println(address);
            memoryAdresses.get(labelNumber).setText("00000000".substring(address.length()) + address);
            memoryValues.get(labelNumber).setText(content);
        }
    }

    public void displayLablels() { //NOT OPTIMISED

        //adresses
        for(int i = 1; i<memoryAdresses.size(); i++){
            mainPane.getChildren().remove(memoryAdresses.get(i));
        }
        memoryAdresses.clear();
        Text firstMemoryLabel = (Text) mainPane.lookup("#addressLabel");
        memoryAdresses.add(firstMemoryLabel);
        double paneHeight = mainPane.getHeight();
        double xpos = memoryAdresses.get(0).getBoundsInParent().getMinX();
        double nextYpos = memoryAdresses.get(0).getBoundsInParent().getMaxY() + memoryAdresses.get(0).getLayoutBounds().getHeight();
        double remaningSpace = paneHeight - nextYpos;
        while(remaningSpace > VERTICAL_SPACE_BETWEEN_TEXT){
            memoryAdresses.add(new Text(xpos, nextYpos, "0x00000000"));
            mainPane.getChildren().add(memoryAdresses.get(memoryAdresses.size()-1));
            nextYpos = nextYpos + memoryAdresses.get(memoryAdresses.size()-1).getLayoutBounds().getHeight() + VERTICAL_SPACE_BETWEEN_TEXT;
            remaningSpace = paneHeight - nextYpos;
        }

        //contents
        for(int i = 1; i<memoryValues.size(); i++){
            mainPane.getChildren().remove(memoryValues.get(i));
        }
        memoryValues.clear();
        Text firstValueLabel = (Text) mainPane.lookup("#valueLabel");
        memoryValues.add(firstMemoryLabel);
        paneHeight = mainPane.getHeight(); //already caluclated above
        xpos = firstValueLabel.getBoundsInParent().getMinX();
        nextYpos = memoryValues.get(0).getBoundsInParent().getMaxY() + memoryValues.get(0).getLayoutBounds().getHeight();
        remaningSpace = paneHeight - nextYpos;
        while(remaningSpace > VERTICAL_SPACE_BETWEEN_TEXT){
            Text valueLabel = new Text(xpos, nextYpos, "0x00000000");
            memoryValues.add(valueLabel);
            mainPane.getChildren().add(valueLabel);
            nextYpos = nextYpos + valueLabel.getLayoutBounds().getHeight() + VERTICAL_SPACE_BETWEEN_TEXT;
            remaningSpace = paneHeight - nextYpos;
        }
    }

    public DockNode getNode() {
        return dockNode;
    }
}
