import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Button button_login;
    @FXML
    private Button button_signup;
    @FXML
    private TextField tf_username;
    @FXML
    private PasswordField passwordField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        button_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.loginUser(event, tf_username.getText(), passwordField.getText());
            }
        });

        button_signup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "register.fxml", "Signup", null, null);
            }
        });
    }
}
