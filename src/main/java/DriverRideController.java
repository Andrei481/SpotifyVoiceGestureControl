import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class DriverRideController extends DriverController implements Initializable {

    @FXML
    public Label labelWaitsAt;
    @FXML
    private Button buttonCancel, buttonArrived;

    public void initialize(URL location, ResourceBundle resources) {

        buttonCancel.setOnAction(event -> DBUtils.changeScene(event, "driver.fxml", "RideShare - Driver", null, null, null, 0, null, null));
        buttonArrived.setOnAction(event -> DBUtils.changeScene(event, "driver.fxml", "RideShare", null, null, null, 0, null, null));
    }

    public void startScene () {

    }
}

