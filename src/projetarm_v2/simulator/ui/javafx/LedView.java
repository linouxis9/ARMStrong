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
import javafx.scene.layout.GridPane;
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
    GridPane gridPane;
    
    public LedView(){
       // dockImage = new Image(Gui.class.getResource("docknode.png").toExternalForm());
    	Image ledOff = new Image(getClass().getResource("/resources/ledOff.png").toExternalForm());
        Image ledOn = new Image(getClass().getResource("/resources/ledOn.png").toExternalForm());
        this.mainPane = new ScrollPane();
        gridPane = new GridPane();
        gridPane.setPadding(new Insets(5));
        gridPane.setHgap(10);
        gridPane.setVgap(40);
        this.dockNode = new DockNode(mainPane, "Led Game", new ImageView(dockImage));
        
        dockNode.setPrefSize(460,666);
        
        this.mainPane.setContent(gridPane);  
        this.mainPane.setFitToWidth(true);
        this.mainPane.setFitToHeight(true);  
        this.mainPane.setHmin(dockNode.getHeight());
        
        ImageView led1 = new ImageView();
        led1.setImage(ledOn);

        ImageView led2 = new ImageView();
        led2.setImage(ledOn);

        ImageView led3 = new ImageView();
        led3.setImage(ledOn);

        ImageView led4 = new ImageView();
        led4.setImage(ledOn);

        ImageView led5 = new ImageView();
        led5.setImage(ledOn);

        ImageView led6 = new ImageView();
        led6.setImage(ledOff);


        gridPane.add(led1, 1, 0);
        gridPane.add(led2, 1, 1);
        gridPane.add(led3, 1, 2);
        gridPane.add(led4, 1, 3);
        gridPane.add(led5, 1, 4);
        gridPane.add(led6, 1, 5);
        
        //anchorPane.getChildren().add(gridPane); 
    }
    
    public DockNode getNode(){
        return dockNode;
    }
   
}

