import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

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
        buttonCancel.setOnAction(event -> cancelRide());
        //receiveDriver("Gigi", "Male");
        //finishRide();
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

    public void finishRide () {

        labelPleaseWait.setText("Your driver arrived!");
        buttonCancel.setText("Ok");
        buttonCancel.setStyle("-fx-background-color: #0060FA;");
        buttonCancel.setTextFill(new Color(1,1,1,1));
        buttonCancel.setOnAction(event -> returnToRequestPage(event));
    }

    private void returnToRequestPage (ActionEvent event) {

        DBUtils.changeScene(event, "client.fxml", "RideShare - Client", username, role, name, age, gender, email);
    }

}

