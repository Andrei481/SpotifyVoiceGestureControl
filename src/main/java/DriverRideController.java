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

    public String username, role, name, gender, email, licensePlate;
    public int age;
    
    public void initialize(URL location, ResourceBundle resources) {

        buttonCancel.setOnAction(event -> DBUtils.changeScene(event, "driver.fxml", "RideShare - Driver", username, role, name, age, gender, email, licensePlate));
        buttonArrived.setOnAction(event -> DBUtils.changeScene(event, "driver.fxml", "RideShare - Driver", username, role, name, age, gender, email, licensePlate));
    }

}

