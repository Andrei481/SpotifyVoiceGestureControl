import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


import static jdk.nashorn.internal.objects.NativeString.toUpperCase;

public class LogInController extends Controller implements Initializable {
    @FXML
    private Label labelWelcome;
    private final ObservableList<String> locations = FXCollections.observableArrayList("A", "B", "C", "D");
    @FXML
    private ComboBox<String> comboBoxLocation;
    @FXML
    private ComboBox<String> comboBoxDestination;
    @FXML
    private Button buttonRequest;
    @FXML
    private Label labelName;
    @FXML
    private Label labelRole;
    @FXML
    private Button buttonLogout;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboBoxLocation.setItems(locations);
        comboBoxDestination.setItems(locations);
        buttonLogout.setOnAction(event -> DBUtils.changeScene(event, "main.fxml", "RideShare", null, null, null, 0, null, null, null));
        buttonRequest.setOnAction(event -> {
            if ((comboBoxLocation.getValue() == null) || (comboBoxDestination.getValue() == null)) {
                displayError("Please fill in your location and desired destination.");
                System.out.println("Error: Empty fields!");
            } else
            if (Objects.equals(comboBoxLocation.getValue(), comboBoxDestination.getValue())) {
                displayError("Can't request ride to the same location!");
                System.out.println("Error: Same location!");
            }


        });
    }

    public void setUserInfo(String username, String role, String name, int age, String gender, String email, String licensePlate)
    {
        //labelName.setText(name);
        labelWelcome.setText("WELCOME, " + toUpperCase(name) + "!");
        //labelRole.setText("Role: " + role);

    }
}
