package projetarm_v2.simulator.ui.javafx;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.dockfx.DockNode;
import projetarm_v2.simulator.boilerplate.ArmSimulator;
import projetarm_v2.simulator.core.io.IO8Segment;

import java.util.ArrayList;

public class EightSegmentDisplay {
    Pane mainPane;
    DockNode dockNode;

    Text address;
    ArrayList<ImageView> images;
    IO8Segment segment;

    private ArmSimulator simulator;

    public EightSegmentDisplay(ArmSimulator simulator){

        this.mainPane = new AnchorPane();
        images = new ArrayList<>();

        this.mainPane.getChildren().add(new ImageView(new Image(getClass().getResource("/resources/8_segment/8seg_base.png").toExternalForm())));
        for (int i=0; i<8; i++){
            images.add(new ImageView(new Image(getClass().getResource("/resources/8_segment/8seg_" + (i+1) + ".png").toExternalForm())));
            this.mainPane.getChildren().add(images.get(i));
            images.get(i).setVisible(false);
        }

        this.dockNode = new DockNode(mainPane, "8 segment view");

        dockNode.setPrefSize(460,666);
        segment = simulator.newIO8Segment();

        address = new Text(""+segment.getSegment(1).getPortAddress());
        mainPane.getChildren().add(address);

    }

    public void refresh() {
        for (int i=0; i<8; i++){
            images.get(i).setVisible(segment.getSegmentState(i));
        }

    }

    public DockNode getNode() {
        return dockNode;
    }

}
