package projetarm_v2.simulator.ui.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.dockfx.DockNode;
import org.dockfx.demo.DockFX;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RamView {

    private Pane mainPane;
    private DockNode dockNode;
    private Image dockImage;

    int paneHeight;
    int paneWidth;

    public RamView(){
        dockImage = new Image(DockFX.class.getResource("docknode.png").toExternalForm());
        try {
            mainPane = FXMLLoader.load(getClass().getResource("/resources/MemoryView.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        dockNode = new DockNode(mainPane, "Ram View", new ImageView(dockImage));
        dockNode.setPrefSize(300, 100);

        Text firstMemoryLabel = (Text) mainPane.lookup("#addressLabel");
        Text firstValueLabel = (Text) mainPane.lookup("#valueLabel");
        ArrayList<Text> memoryAdresses = new ArrayList<Text>();
        memoryAdresses.add(firstMemoryLabel);

        int remaningSpace = paneHeight;
        while(remaningSpace > 10){
            Text addressLabel = new Text("0x00000000");
            memoryAdresses.add(addressLabel);

        }
    }
}
