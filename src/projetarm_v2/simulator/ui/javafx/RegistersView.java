package projetarm_v2.simulator.ui.javafx;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.dockfx.DockNode;
import org.dockfx.DockPane;

public class RegistersView {

    TabPane mainPane;
    DockNode dockNode;
    Image dockImage;

    public RegistersView(){
       // dockImage = new Image(Gui.class.getResource("docknode.png").toExternalForm());

        mainPane = new TabPane();
        mainPane.getTabs().addAll(new Tab("Hex"), new Tab("sig Dec"), new Tab("unsig Dec"));

        dockNode = new DockNode(mainPane, "Register View", new ImageView(dockImage));
        dockNode.setPrefSize(300, 100);
    }

    DockNode getNode(){
        return dockNode;
    }

}
