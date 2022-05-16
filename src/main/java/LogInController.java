
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class LogInController implements Initializable {
    @FXML
    private Button buttonLogout;

    @FXML
    private Label labelWelcome;
    @FXML
    private Label labelRole;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttonLogout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "main.fxml", "Login", null, null, null, 0, null, null, null);
            }
        });
    }

    public void setUserInfo(String username, String role, String name, int age, String gender, String email, String licensePlate)
    {
        labelWelcome.setText("Welcome, "+username+"!");
        labelRole.setText("Role: "+role);
    }
}
