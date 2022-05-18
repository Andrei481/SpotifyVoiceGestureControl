import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

import static jdk.nashorn.internal.objects.NativeString.toUpperCase;

public class DriverController extends LoginController implements Initializable {
    @FXML
    private Label labelWelcome, labelName, labelAge, labelGender, labelEmail, labelUsername, labelRole, labelPlate;
    private final ObservableList<String> locations = FXCollections.observableArrayList("A", "B", "C", "D");
    @FXML
    private Button buttonRequest, buttonLogout;
    @FXML
    private ListView<String> listRides;
    private final ObservableList<String> rides = FXCollections.observableArrayList ("A ⟶ B", "C ⟶ A", "D ⟶ B", "B ⟶ C");
            // import rides from database here...

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // RIDES TAB


        // HISTORY TAB


        // PROFILE TAB
        buttonLogout.setOnAction(event -> DBUtils.changeScene(event, "login.fxml", "RideShare", null, null, null, 0, null, null, null));
    }

    public void setUserInfo(String username, String role, String name, int age, String gender, String email, String licensePlate)
    {
        // RIDES TAB
        String[] names = name.split(" ", 2);
        labelWelcome.setText("WELCOME, " + toUpperCase(names[1]) + "!");
        listRides.setItems(rides);


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
