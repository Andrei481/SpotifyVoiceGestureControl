import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javax.xml.soap.Text;
import java.net.URL;
import java.util.ResourceBundle;

public class RegistrationController implements Initializable {
    @FXML
    private Button button_signup;
    @FXML
    private Button button_login;
    @FXML
    private TextField tf_username;
    @FXML
    private TextField tf_password;
    @FXML
    private RadioButton rb_driver;
    @FXML
    private RadioButton rb_client;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /**
         * this part makes sure that only one role can be selected
         * if the user selects the driver role => the client role is deselected automatically and vice-versa
         */
        ToggleGroup toggleGroup = new ToggleGroup();
        rb_driver.setToggleGroup(toggleGroup);
        rb_client.setToggleGroup(toggleGroup);

        rb_client.setSelected(true);

        button_signup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String toggleName = ((RadioButton) toggleGroup.getSelectedToggle()).getText();

                /**
                 * check if username and password contain no whitespace
                 */
                if(!tf_username.getText().trim().isEmpty() && !tf_password.getText().trim().isEmpty())
                {
                    DBUtils.registerUser(event, tf_username.getText(), tf_password.getText(), toggleName);
                }else
                {
                    System.out.println("Please fill in all information");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in the empty fields!");
                    alert.show();
                }
            }
        });

        button_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "main.fxml", "Login", null, null);
            }
        });
    }
}
