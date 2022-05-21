import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientRideController extends ClientController implements Initializable {

    @FXML
    public Label labelPleaseWait;
    @FXML
    private Button buttonCancel;

    public String username, role, name, gender, email;
    public int age;
    
    public void initialize(URL location, ResourceBundle resources) {
        //receiveDriver("Gigi", "Male");
    }

    public void cancelRide() {

        Alert alert = new Alert(Alert.AlertType.WARNING, "This will cancel the current ride. Are you sure you want to proceed?", ButtonType.CANCEL, ButtonType.YES);
        alert.setTitle("Cancel ride - WARNING");
        alert.showAndWait();
    }

    public void receiveDriver(String driverName, String gender) {

        String appendS = "";
        switch (gender) {
            case "Male":
                gender = "he";
                appendS = "s";
                break;
            case "Female":
                gender = "she";
                appendS = "s";
                break;
            case "Other":
                gender = "they";
                break;
            default:
                gender = "db error";
        }
        labelPleaseWait.setText("Your ride was accepted by " + driverName + ".\n" + "Please wait until " + gender + " arrive" + appendS + " at your location.");
    }

}

