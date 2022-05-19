import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import util.Ride;

import java.net.URL;
import java.util.ResourceBundle;

import static jdk.nashorn.internal.objects.NativeString.toUpperCase;

public class DriverController extends LoginController implements Initializable {
    @FXML
    private Label labelWelcome, labelName, labelAge, labelGender, labelEmail, labelUsername, labelRole, labelPlate, labelSelectedRide;
    @FXML
    private Button buttonStart, buttonLogout;
    @FXML
    private ListView<String> listRides;
    private ObservableList<String> rides = FXCollections.observableArrayList();
            // import rides from database here...

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // RIDES TAB
        DBUtils.checkAvailableRides();
        listRides.setItems(DBUtils.getAvailableRidesList());
        // HISTORY TAB


        // PROFILE TAB
        buttonLogout.setOnAction(event -> DBUtils.changeScene(event, "login.fxml", "RideShare"));
    }

    public void setUserInfo(String username, String role, String name, int age, String gender, String email, String licensePlate)
    {
        // RIDES TAB
        //listRides.setItems(ClientController.getRideList());
        listRides.getSelectionModel().selectedItemProperty().addListener(
                (ov, old_val, new_val) -> labelSelectedRide.setText(new_val));



        // HISTORY TAB


        // PROFILE TAB
        labelName.setText(name);
        labelAge.setText(Integer.toString(age));
        labelGender.setText(gender);
        labelEmail.setText(email);
        labelUsername.setText(username);
        labelRole.setText(role);
        labelPlate.setText(licensePlate);

    }
}
