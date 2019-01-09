package projetarm_v2.simulator.ui.javafx;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.dockfx.DockNode;
import org.dockfx.DockPane;

public class RegistersView {

    TabPane mainPane;
    DockNode dockNode;
    Image dockImage;

    public RegistersView(){
       // dockImage = new Image(Gui.class.getResource("docknode.png").toExternalForm());

        Pane hexPane = new Pane();
        Pane sigHexPane = new Pane();
        Pane decPane = new Pane();

        mainPane = new TabPane();
        Tab hexTab = new Tab("Hexa", hexPane);
        Tab sigHexTab = new Tab("Sig Hexa", sigHexPane);
        Tab decTab = new Tab("Decimal", decPane);
        mainPane.getTabs().addAll(hexTab, sigHexTab, decTab);

        hexPane.getChildren().add(new Text(10, 10, "test1"));
        sigHexPane.getChildren().add(new Text(10, 10,"test2"));
        decPane.getChildren().add(new Text(10, 10,"test3"));

        hexTab.setContent(hexPane);

        dockNode = new DockNode(mainPane, "Register View", new ImageView(dockImage));
        dockNode.setPrefSize(300, 100);


    }

    DockNode getNode(){
        return dockNode;
    }

}
