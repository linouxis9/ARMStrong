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
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.dockfx.DockNode;
import org.dockfx.DockPane;

public class ConsoleView {

	ScrollPane mainPane;
    DockNode dockNode;
    Image dockImage;
    TextFlow textArea;
   

    public ConsoleView(){
       // dockImage = new Image(Gui.class.getResource("docknode.png").toExternalForm());
        this.mainPane = new ScrollPane();
 
        this.dockNode = new DockNode(mainPane, "Console", new ImageView(dockImage));

        dockNode.setPrefSize(1000,1500);
        dockNode.setClosable(false);
        
        this.textArea = new TextFlow();
        
        this.textArea.setPadding(new Insets(5));
        this.textArea.setStyle("-fx-line-spacing: -0.4em;");
        
        OutputStream output = new OutputStream(){
	        @Override
	        public void write(int b) throws IOException {
	        	Platform.runLater(()->textArea.getChildren().add(new Text(String.valueOf((char) b))));   
	        	mainPane.setVvalue(1);
	        }
        };
        
        this.mainPane.setFitToWidth(true);
        this.mainPane.setFitToHeight(true);
        this.mainPane.setContent(this.textArea);    
        this.mainPane.setHmin(dockNode.getHeight());
        
        System.setOut(new PrintStream(output));     
    }
    
    public DockNode getNode(){
        return dockNode;
    }
   
}
