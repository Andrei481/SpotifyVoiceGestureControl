import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventDispatchChain;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

public class DriverRideController extends DriverController implements Initializable {

    @FXML
    public Label labelWaitsAt;
    @FXML
    private Button buttonCancel, buttonArrived;

    public String username, role, name, gender, email, licensePlate;
    public int age;
    private boolean rideFinishedByDriver = false;
    
    public void initialize(URL location, ResourceBundle resources) {

        buttonCancel.setOnAction(event -> {
            rideFinishedByDriver = true;
            DBUtils.cancelRide(event, DBUtils.getCurrentLoggedInUserID());
            DBUtils.changeScene(event, "driver.fxml", "RideShare - Driver", username, role, name, age, gender, email, licensePlate);
        });
        buttonArrived.setOnAction(event -> {
            rideFinishedByDriver = true;
            DBUtils.driverArrived(event, DBUtils.getCurrentLoggedInUserID());
            DBUtils.changeScene(event, "driver.fxml", "RideShare - Driver", username, role, name, age, gender, email, licensePlate);
        });

        //waitForCancel();
    }

    private void waitForCancel() {

        if (rideFinishedByDriver) {
            return;
        }

        if (checkIfCanceledByClient()) {
            System.out.println("canceled by client");
            // GO TO RIDE LIST PAGE
            // can't get this shit to work for the life of me
            return;
        }

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        waitForCancel();
                    }
                },
                5000
        );
    }

    private boolean checkIfCanceledByClient () {

        Connection connection = null;
        PreparedStatement ps;
        ResultSet rs;

        boolean rideCanceledByClient = false;

        try {
            connection = DriverManager.getConnection("jdbc:mariadb://lazarov.go.ro:3306/RideShare", "root", "chocolate");
            ps = connection.prepareStatement("SELECT ride_started FROM database_driver WHERE user_id = ?");
            ps.setInt(1, DBUtils.getCurrentLoggedInUserID());
            rs = ps.executeQuery();

            if (!rs.isBeforeFirst())
                System.out.println("ride_started not found");
            else {
                while(rs.next())
                    rideCanceledByClient = !rs.getBoolean("ride_started");
            }

            rs.close();
            ps.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(connection != null) {
                try {
                    connection.close();
                }catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return rideCanceledByClient;
    }


    public void cancelRide(ActionEvent event)
    {
        Alert alert = new Alert(Alert.AlertType.WARNING, "This will cancel the current ride. Are you sure you want to proceed?", ButtonType.CANCEL, ButtonType.YES);
        alert.setTitle("Cancel ride - WARNING");
        alert.showAndWait();
    }

}

