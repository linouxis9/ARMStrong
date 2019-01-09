package projetarm_v2.simulator.ui.javafx;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.tools.Tool;

public class ArmToolBar {

    ToolBar toolBar;

    public ArmToolBar(){
        this.toolBar = new ToolBar(
                new Button("", new ImageView(new Image(getClass().getResource("/resources/switch.png").toExternalForm()))),
                new Separator(),
                new Button("", new ImageView(new Image(getClass().getResource("/resources/run.png").toExternalForm()))),
                new Button("", new ImageView(new Image(getClass().getResource("/resources/runByStep.png").toExternalForm()))),
                new Button("", new ImageView(new Image(getClass().getResource("/resources/reload.png").toExternalForm()))),
                new Button("", new ImageView(new Image(getClass().getResource("/resources/stop.png").toExternalForm())))
        );
    }

    public Node getNode(){
        return this.toolBar;
    }
}
