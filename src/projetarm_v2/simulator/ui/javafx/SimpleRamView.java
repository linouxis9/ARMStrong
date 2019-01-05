package projetarm_v2.simulator.ui.javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.dockfx.DockNode;
import org.dockfx.demo.DockFX;

import java.io.IOException;
import java.util.ArrayList;

public class SimpleRamView {

    private Pane mainPane;
    private DockNode dockNode;
    private Image dockImage;

    public SimpleRamView(){
        dockImage = new Image(DockFX.class.getResource("docknode.png").toExternalForm());

        try {
            mainPane = FXMLLoader.load(getClass().getResource("/resources/simpleMemoryView.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        dockNode = new DockNode(mainPane, "Simple Ram View", new ImageView(dockImage));
        dockNode.setPrefSize(300, 100);

        TableView<RamTuple> tableView = (TableView<RamTuple>) mainPane.lookup("#simpleMemoryTabView");

        TableColumn<RamTuple, String> addressColumn = new TableColumn<RamTuple, String>("address");
        TableColumn<RamTuple, String> contentColumn = new TableColumn<RamTuple, String>("content");

        tableView.getColumns().addAll(addressColumn,contentColumn);

        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("content"));

        ArrayList<RamTuple> ramDisplayed = new ArrayList<>();
        for (int i=0; i<2000000; i++){
            ramDisplayed.add(new RamTuple(i,8));
        }
        ObservableList list = FXCollections.observableArrayList(ramDisplayed);

        tableView.setItems(list);
    }



    DockNode getNode(){
        return dockNode;
    }
}
