
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
    private Button button_logout;

    @FXML
    private Label label_welcome;
    @FXML
    private Label label_role;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        button_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "main.fxml", "Login", null, null);
            }
        });
    }

    public void setUserInfo(String username, String role)
    {
        label_welcome.setText("Welcome, "+username+"!");
        label_role.setText("Role: "+role);
    }
}
