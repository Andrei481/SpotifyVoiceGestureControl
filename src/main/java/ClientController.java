import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

public class ClientController extends LoginController implements Initializable {
    @FXML
    private Label labelWelcome, labelName, labelAge, labelGender, labelEmail, labelUsername, labelRole, labelPlate;
    private final ObservableList<String> locations = FXCollections.observableArrayList("A", "B", "C", "D");
    @FXML
    private ComboBox<String> comboBoxLocation, comboBoxDestination;
    @FXML
    private Button buttonRequest, buttonLogout;

    private String chosenLocation, chosenDestination;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // REQUEST TAB
        comboBoxLocation.setItems(locations);
        comboBoxDestination.setItems(locations);

        comboBoxLocation.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                chosenLocation = newValue;
                System.out.println("Chosen Location: "+chosenLocation);
            }
        });

        comboBoxDestination.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                chosenDestination = newValue;
                System.out.println("Chosen Destination: "+chosenDestination);
            }
        });

        buttonRequest.setOnAction(event -> {
            if ((comboBoxLocation.getValue() == null) || (comboBoxDestination.getValue() == null)) {
                displayError("Please fill in your location and desired destination.");
                System.out.println("Error: Empty fields!");
            } else if (Objects.equals(comboBoxLocation.getValue(), comboBoxDestination.getValue())) {
                displayError("Can't request ride to the same location!");
                System.out.println("Error: Same location!");
            }
            else{
                System.out.println("Requesting ride from: "+chosenLocation +" to "+chosenDestination+" for client "+DBUtils.getCurrentLoggedInUserID());
                DBUtils.requestRideClient(event, DBUtils.getCurrentLoggedInUserID(), chosenLocation, chosenDestination);
            }
        });

        // HISTORY TAB


        // PROFILE TAB
        buttonLogout.setOnAction(event -> DBUtils.changeScene(event, "login.fxml", "RideShare", null, null, null, 0, null, null));
    }

    public void setUserInfo(String username, String role, String name, int age, String gender, String email)
    {
        // REQUEST TAB
        String[] names = name.split(" ", 2);
        labelWelcome.setText("WELCOME, " + toUpperCase(names[1]) + "!");

        // HISTORY TAB


        // PROFILE TAB
        labelName.setText(name);
        labelAge.setText(Integer.toString(age));
        labelGender.setText(gender);
        labelEmail.setText(email);
        labelUsername.setText(username);
        labelRole.setText(role);

    }
}
