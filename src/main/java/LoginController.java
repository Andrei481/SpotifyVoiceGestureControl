import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private Button buttonLogin, buttonSignup;
    @FXML
    private TextField textFieldUsername;
    @FXML
    private PasswordField passwordField;
    private boolean emptyUsername = true;
    private boolean emptyPassword = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        textFieldUsername.textProperty().addListener((observable, oldValue, newValue) -> {
            emptyUsername = newValue.equals("");
            buttonLogin.setDisable((emptyUsername) || (emptyPassword));
        });
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            emptyPassword = newValue.equals("");
            buttonLogin.setDisable((emptyUsername) || (emptyPassword));
        });
        buttonLogin.setOnAction(event -> DBUtils.loginUser(event, textFieldUsername.getText(), passwordField.getText()));

        buttonSignup.setOnAction(event -> DBUtils.changeScene(event, "register.fxml", "RideShare - Sign up", null, null, null, 0, null, null));
    }

    public void displayError(String errorMessage)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Oops!");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}
