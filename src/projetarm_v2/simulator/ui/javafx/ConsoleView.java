package projetarm_v2.simulator.ui.javafx;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.dockfx.DockNode;
import org.dockfx.DockPane;

public class ConsoleView {

    TabPane mainPane;
    DockNode dockNode;
    Image dockImage;

    public ConsoleView(){
       // dockImage = new Image(Gui.class.getResource("docknode.png").toExternalForm());

        mainPane = new TabPane();

        dockNode = new DockNode(mainPane, "Console", new ImageView(dockImage));
        dockNode.setPrefSize(100,100);        
    }
    
    public DockNode getNode(){
        return dockNode;
    }
   
}
