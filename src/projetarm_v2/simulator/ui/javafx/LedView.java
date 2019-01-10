package projetarm_v2.simulator.ui.javafx;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.dockfx.DockNode;
import org.dockfx.DockPane;

public class LedView {

	ScrollPane mainPane;
    DockNode dockNode;
    Image dockImage;
    TextFlow textArea;
    AnchorPane anchorPane;
    
    public LedView(){
       // dockImage = new Image(Gui.class.getResource("docknode.png").toExternalForm());
    	Image ledOff = new Image(getClass().getResource("/resources/stop.png").toExternalForm());
        Image ledOn = new Image(getClass().getResource("/resources/stop.png").toExternalForm());
        this.mainPane = new ScrollPane();
        this.anchorPane = new AnchorPane();
        this.dockNode = new DockNode(mainPane, "Led Game", new ImageView(dockImage));
        
        dockNode.setPrefSize(460,666);
        
        this.mainPane.setContent(anchorPane);  
        this.mainPane.setFitToWidth(true);
        this.mainPane.setFitToHeight(true);  
        this.mainPane.setHmin(dockNode.getHeight());
        
        ImageView ledInit = new ImageView();
        ledInit.setImage(ledOff);
        
        anchorPane.getChildren().add(ledInit);
        

    }
    
    public DockNode getNode(){
        return dockNode;
    }
   
}

