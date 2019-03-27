package projetarm_v2.simulator.ui.javafx;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.dockfx.DockNode;
import projetarm_v2.simulator.boilerplate.ArmSimulator;
import projetarm_v2.simulator.core.io.IO8Segment;

import java.util.ArrayList;

public class EightSegmentDisplay {
    HBox mainPane;
    DockNode dockNode;

    Text address;
    ArrayList<ArrayList<ImageView>> images;
    ArrayList<IO8Segment> segments;

    private ArmSimulator simulator;

    public EightSegmentDisplay(ArmSimulator simulator){

        this.mainPane = new HBox();
        images = new ArrayList<>();
        segments = new ArrayList<>();

        for(int j = 0; j<15; j++) {
            VBox currentVBox = new VBox();
            this.mainPane.getChildren().add(currentVBox);
            AnchorPane imagesContainer = new AnchorPane();
            currentVBox.getChildren().add(imagesContainer);
            imagesContainer.getChildren().add(new ImageView(new Image(getClass().getResource("/resources/8_segment/8seg_base.png").toExternalForm())));
            images.add(new ArrayList<>());
            for (int i = 0; i < 8; i++) {
                ImageView currentImage = new ImageView(new Image(getClass().getResource("/resources/8_segment/8seg_" + (i + 1) + ".png").toExternalForm()));
                imagesContainer.getChildren().add(currentImage);
                images.get(j).add(currentImage);
                currentImage.setVisible(false);
            }
            segments.add(simulator.newIO8Segment());
            currentVBox.getChildren().add(new Text("0x" + Long.toHexString(segments.get(j).getSegment(0).getPortAddress())));
        }

        this.dockNode = new DockNode(mainPane, "8 segment view");
        dockNode.setPrefSize(460,666);

    }

    public void refresh() {
        for (int j=0; j<15; j++){
            for (int i=0; i<8; i++){
                images.get(j).get(i).setVisible(segments.get(j).getSegmentState(i));
            }
        }
    }

    public DockNode getNode() {
        return dockNode;
    }

}
