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
import util.Ride;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class DriverController extends LoginController implements Initializable {
    @FXML
    public Label labelName, labelAge, labelGender, labelEmail, labelUsername, labelRole, labelPlate, labelSelectedRide;
    @FXML
    private Button buttonStart, buttonLogout;
    @FXML
    private ListView<String> listRides;
    private ObservableList<String> rides = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // RIDES TAB
        DBUtils.checkAvailableRides();
        listRides.setItems(DBUtils.getAvailableRidesList());
        listRides.getSelectionModel().selectedItemProperty().addListener(
                (ov, old_val, new_val) -> {
                    labelSelectedRide.setText("Selected ride:" + '\n' + new_val);
                    buttonStart.setDisable(false);
                });

        // HISTORY TAB

        // PROFILE TAB
        buttonLogout.setOnAction(event -> DBUtils.changeScene(event, "login.fxml", "RideShare"));
    }

    public void setUserInfo(String username, String role, String name, int age, String gender, String email, String licensePlate)
    {
        // RIDES TAB
        buttonStart.setOnAction(event -> startRide(event, username, role, name, age, gender, email, licensePlate));

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

    public void startRide(ActionEvent event, String username, String role, String name, int age, String gender, String email, String licensePlate)
    {
        Parent root = null;
        try{
            FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource("driverRide.fxml"));
            root = loader.load();
            DriverRideController driverRideController = loader.getController();
            driverRideController.labelWaitsAt.setText("Your client waits at location " + labelSelectedRide.getText());
            driverRideController.username = username;
            driverRideController.role = role;
            driverRideController.name = name;
            driverRideController.age = age;
            driverRideController.gender = gender;
            driverRideController.email = email;
            driverRideController.licensePlate = licensePlate;

        }catch (IOException e)
        {
            e.printStackTrace();
        }



        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("RideShare - Ride started");
        stage.setScene(new Scene(Objects.requireNonNull(root), 800, 600));
        stage.show();
    }
}
