package projetarm_v2.simulator.ui.javafx;

import org.dockfx.DockNode;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class LedView {

    ScrollPane mainPane;
    DockNode dockNode;
    Image dockImage;
    TextFlow textArea;
    AnchorPane anchorPane;
    VBox ledContainer;
    
    
    public LedView(){
       // dockImage = new Image(Gui.class.getResource("docknode.png").toExternalForm());
        
        
        this.mainPane = new ScrollPane();
        
        Image ledOff = new Image(getClass().getResource("/resources/ledOff.png").toExternalForm());
        Image ledOn = new Image(getClass().getResource("/resources/ledOn.png").toExternalForm());
        
        
        
        /* The first element in the VBox is one anchorPane containing 2 buttons : more led and less led */
        Button moreLedButton = new Button();
        moreLedButton.setText("More Led");
        
        moreLedButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                AnchorPane newLedAddress = new AnchorPane();
                ImageView newLed = new ImageView();
                newLed.setImage(ledOn);
                newLed.setLayoutX(0);
                newLed.setLayoutY(0);
                Text newAddress = new Text();
                newAddress.setText("Address : ");
                newAddress.setLayoutX(150);
                newAddress.setLayoutY(55);
                
                newLedAddress.getChildren().addAll(newLed, newAddress);
                ledContainer.getChildren().add(newLedAddress);
            }
        });
        
        Button lessLedButton = new Button();
        lessLedButton.setText("Less Led");
        lessLedButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
            	if(ledContainer.getChildren().size()-1 != 0) {
            		ledContainer.getChildren().remove(ledContainer.getChildren().size()-1);	
            	}
            	
            }
        });
          
        AnchorPane buttonAnchor = new AnchorPane();
        
        moreLedButton.setLayoutX(20);
        moreLedButton.setLayoutY(20);
        
        lessLedButton.setLayoutX(200);
        lessLedButton.setLayoutY(20);
        
        buttonAnchor.getChildren().addAll(moreLedButton, lessLedButton);
        
        /* This anchor pane stand for one line <led image> -> <address> */
        AnchorPane addressLine = new AnchorPane();
        ImageView led = new ImageView();
        led.setImage(ledOn);
        led.setLayoutX(0);
        led.setLayoutY(0);
        Text address = new Text();
        address.setText("Address : ");
        address.setLayoutX(150);
        address.setLayoutY(55);
        
        addressLine.getChildren().addAll(led, address);
        
        
        ledContainer = new VBox(buttonAnchor, addressLine);
        
        this.dockNode = new DockNode(mainPane, "Led Game", new ImageView(dockImage));
        
        dockNode.setPrefSize(460,666);
        
        this.mainPane.setContent(ledContainer);  
        this.mainPane.setFitToWidth(true);
        this.mainPane.setFitToHeight(true);  
        this.mainPane.setHmin(dockNode.getHeight());
    }
    
    
    public DockNode getNode(){
        return dockNode;
    }
   
}



