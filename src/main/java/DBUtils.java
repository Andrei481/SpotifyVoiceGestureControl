import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import org.mindrot.jbcrypt.BCrypt;
import util.Ride;

import javax.management.Query;
import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class DBUtils extends LoginController {
    private static int currentLoggedInUserID;

    private static ObservableList<String> rides = FXCollections.observableArrayList();

    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username, String role, String name, int age, String gender, String email)
    {
        Parent root = null;
        if(username != null && role != null)
        {
            try{
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root = loader.load();
                ClientController clientController = loader.getController();
                clientController.setUserInfo(username, role, name, age, gender, email);
            }catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            try{
                root = FXMLLoader.load(Objects.requireNonNull(DBUtils.class.getResource(fxmlFile)));
            }catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(Objects.requireNonNull(root), 800, 600));
        stage.show();
    }

    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username, String role, String name, int age, String gender, String email, String licensePlate)
    {
        Parent root = null;
        if(username != null && role != null)
        {
            try{
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root = loader.load();
                DriverController driverController = loader.getController();
                driverController.setUserInfo(username, role, name, age, gender, email, licensePlate);
            }catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            try{
                root = FXMLLoader.load(Objects.requireNonNull(DBUtils.class.getResource(fxmlFile)));
            }catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(Objects.requireNonNull(root), 800, 600));
        stage.show();
        //checkAvailableRides(event);
    }

    public static void changeScene(ActionEvent event, String fxmlFile, String title){
        try {
            Parent root = null;
            FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
            root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(Objects.requireNonNull(root), 800, 600));
            stage.show();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void registerClient(ActionEvent event, String username, String password, String role, String name, int age, String gender, String email)
    {
        /*
          these variables are the connections to the mysql database
          */

        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psInsertClient = null;
        PreparedStatement psCheckUserAlreadyExists = null;
        //PreparedStatement psCheckEmailAlreadyUsed = null;
        ResultSet resultSet = null;

        try{
            /*
              this will attempt to establish a connection with the db
              */

            connection = DriverManager.getConnection("jdbc:mariadb://lazarov.go.ro:3306/RideShare", "root", "chocolate");
            psCheckUserAlreadyExists = connection.prepareStatement("SELECT * FROM database_user WHERE username = ?");
            psCheckUserAlreadyExists.setString(1, username);
            //psCheckEmailAlreadyUsed = connection.prepareStatement("SELECT * FROM user_database WHERE email = ? ");
            //psCheckEmailAlreadyUsed.setString(7, email);
            resultSet = psCheckUserAlreadyExists.executeQuery();

            /*
              check if the resultSet is empty
              if true => username already taken => notify user about this
             */

            if(resultSet.isBeforeFirst())
            {
                System.out.println("Username already taken!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("User already exists!");
                alert.showAndWait();
            }
            else
            {
                psInsert = connection.prepareStatement("INSERT INTO database_user (username, password, role, name, age, gender, email) VALUES (?, ?, ?, ?, ?, ?, ?)");;
                psInsert.setString(1, username);
                psInsert.setString(2, password);
                psInsert.setString(3, role);
                psInsert.setString(4, name);
                psInsert.setInt(5, age);
                psInsert.setString(6, gender);
                psInsert.setString(7, email);
                psInsert.executeUpdate();

                PreparedStatement st = connection.prepareStatement("SELECT MAX(user_id) from database_user");
                ResultSet rs = st.executeQuery();
                rs.next();
                int uid = rs.getInt(1);
                System.out.println(uid);
                insertIntoClientDatabase(event, uid, false, null, null, 0);
                System.out.println("Client registered successfully!");
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Registration completed! Welcome to Rideshare!");
                alert.showAndWait();
                changeScene(event, "login.fxml", "RideShare");
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }finally {
            /*
              close db connections to avoid memory leaks
              */

            if(resultSet != null)
            {
                try{
                    resultSet.close();
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(psCheckUserAlreadyExists != null)
            {
                try{
                    psCheckUserAlreadyExists.close();
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(psInsert != null)
            {
                try {
                    psInsert.close();
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(psInsertClient != null)
            {
                try {
                    psInsertClient.close();
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(connection != null)
            {
                try{
                    connection.close();
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void registerDriver(ActionEvent event, String username, String password, String role, String name, int age, String gender, String email, String licensePlate)
    {
        /*
          these variables are the connections to the mysql database
          */

        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psInsertDriver = null;
        PreparedStatement psCheckUserAlreadyExists = null;
        //PreparedStatement psCheckEmailAlreadyUsed = null;
        ResultSet resultSet = null;

        try{
            /*
              this will attempt to establish a connection with the db
              */

            connection = DriverManager.getConnection("jdbc:mariadb://lazarov.go.ro:3306/RideShare", "root", "chocolate");
            psCheckUserAlreadyExists = connection.prepareStatement("SELECT * FROM database_user WHERE username = ?");
            psCheckUserAlreadyExists.setString(1, username);
            //psCheckEmailAlreadyUsed = connection.prepareStatement("SELECT * FROM user_database WHERE email = ? ");
            //psCheckEmailAlreadyUsed.setString(7, email);
            resultSet = psCheckUserAlreadyExists.executeQuery();

            /*
              check if the resultSet is empty
              if true => username already taken => notify user about this
             */

            if(resultSet.isBeforeFirst())
            {
                System.out.println("Username already taken!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("User already exists!");
                alert.showAndWait();
            }
            else
            {
                psInsert = connection.prepareStatement("INSERT INTO database_user (username, password, role, name, age, gender, email) VALUES (?, ?, ?, ?, ?, ?, ?)");
                psInsert.setString(1, username);
                psInsert.setString(2, password);
                psInsert.setString(3, role);
                psInsert.setString(4, name);
                psInsert.setInt(5, age);
                psInsert.setString(6, gender);
                psInsert.setString(7, email);
                psInsert.executeUpdate();

                // driver specific

                //Connection connectionDriver = null;


                PreparedStatement st = connection.prepareStatement("SELECT MAX(user_id) from database_user");
                ResultSet rs = st.executeQuery();
                rs.next();
                int uid = rs.getInt(1);
                System.out.println(uid);
                insertIntoDriverDatabase(event, uid, licensePlate, false, 0);
                System.out.println("Driver registered successfully!");
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Registration completed! Welcome to Rideshare!");
                alert.showAndWait();
                changeScene(event, "login.fxml", "RideShare");
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }finally {
            /*
              close db connections to avoid memory leaks
              */

            if(resultSet != null)
            {
                try{
                    resultSet.close();
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(psCheckUserAlreadyExists != null)
            {
                try{
                    psCheckUserAlreadyExists.close();
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(psInsert != null)
            {
                try {
                    psInsert.close();
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(psInsertDriver != null)
            {
                try {
                    psInsertDriver.close();
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(connection != null)
            {
                try{
                    connection.close();
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void loginUser(ActionEvent event, String username, String password)
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            connection = DriverManager.getConnection("jdbc:mariadb://lazarov.go.ro:3306/RideShare", "root", "chocolate");
            preparedStatement = connection.prepareStatement("SELECT user_id, password, role, name, age, gender, email FROM database_user WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            /*
              if this is false => username not found in the db => notify user
              */

            if(!resultSet.isBeforeFirst())
            {
                System.out.println("User not found!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Incorrect credentials.");
                alert.show();
            }
            /*
             *  if the user is found in the DB => compare the password they wrote with the one originally in the db
             */
            else
            {
                /*
                 loop through the result set and retrieve each password and role
                 */
                while(resultSet.next())
                {
                    int retrievedUserID = resultSet.getInt("user_id");
                    String retrievedPassword = resultSet.getString("password");
                    String retrievedRole = resultSet.getString("role");
                    String retrievedName = resultSet.getString("name");
                    int retrievedAge = resultSet.getInt("age");
                    String retrievedGender = resultSet.getString("gender");
                    String retrievedEmail = resultSet.getString("email");

                    currentLoggedInUserID = retrievedUserID;

                    if(BCrypt.checkpw(password, retrievedPassword)) {
                        System.out.println("Current user id: "+currentLoggedInUserID);
                        if (retrievedRole.equals("Client"))
                            changeScene(event, "client.fxml", "RideShare - Client", username, retrievedRole, retrievedName, retrievedAge, retrievedGender, retrievedEmail);
                        else if (retrievedRole.equals("Driver")) {
                            PreparedStatement psDriver = connection.prepareStatement("SELECT license_plate FROM database_driver WHERE user_id = ?");
                            psDriver.setInt(1, retrievedUserID);
                            ResultSet rs = psDriver.executeQuery();
                            if (!rs.isBeforeFirst()) {
                                System.out.println("Driver not found in the database");
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setContentText("Driver not found.");
                                alert.show();
                            } else {
                                while (rs.next()) {
                                    String retrievedLicensePlate = rs.getString("license_plate");
                                    changeScene(event, "driver.fxml", "RideShare - Driver", username, retrievedRole, retrievedName, retrievedAge, retrievedGender, retrievedEmail, retrievedLicensePlate);
                                    //checkAvailableRides(event);
                                }
                            }
                        }
                    }
                    else
                    {
                        System.out.println("Incorrect password");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Invalid credentials");
                        alert.show();
                    }
                }
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }finally {
            /*
             * close all connections to the db
             */

            if(resultSet != null)
            {
                try{
                    resultSet.close();
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(preparedStatement != null)
            {
                try{
                    preparedStatement.close();
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(connection != null)
            {
                try {
                    connection.close();
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void insertIntoClientDatabase(ActionEvent event, int userId, boolean rideRequested, String location, String destination, int correspondingDriverID)
    {
        /*
          these variables are the connections to the mysql database
          */

        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserAlreadyExists = null;
        //PreparedStatement psCheckEmailAlreadyUsed = null;
        ResultSet resultSet = null;

        try{
            /*
              this will attempt to establish a connection with the db
              */

            connection = DriverManager.getConnection("jdbc:mariadb://lazarov.go.ro:3306/RideShare", "root", "chocolate");
            psCheckUserAlreadyExists = connection.prepareStatement("SELECT * FROM database_client WHERE user_id = ?");
            psCheckUserAlreadyExists.setInt(1, userId);
            //psCheckEmailAlreadyUsed = connection.prepareStatement("SELECT * FROM user_database WHERE email = ? ");
            //psCheckEmailAlreadyUsed.setString(7, email);
            resultSet = psCheckUserAlreadyExists.executeQuery();

            /*
              check if the resultSet is empty
              if true => username already taken => notify user about this
             */

            if(resultSet.isBeforeFirst())
            {
                System.out.println("Username already taken!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("ID already exists!");
                alert.showAndWait();
            }
            else
            {
                psInsert = connection.prepareStatement("INSERT INTO database_client (user_id, ride_requested, location, destination, corresponding_driver_id) VALUES (?, ?, ?, ?, ?)");
                psInsert.setInt(1, userId);
                psInsert.setBoolean(2, rideRequested);
                psInsert.setString(3, location);
                psInsert.setString(4, destination);
                psInsert.setInt(5, correspondingDriverID);
                psInsert.executeUpdate();

                //if(role.equals("Client"))
                // changeScene(event, "client.fxml", "RideShare - Client", username, role, name, age, gender, email);
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }finally {
            /*
              close db connections to avoid memory leaks
              */

            if(resultSet != null)
            {
                try{
                    resultSet.close();
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(psCheckUserAlreadyExists != null)
            {
                try{
                    psCheckUserAlreadyExists.close();
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(psInsert != null)
            {
                try {
                    psInsert.close();
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(connection != null)
            {
                try{
                    connection.close();
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void insertIntoDriverDatabase(ActionEvent event, int userId, String licensePlate, boolean rideStarted, int correspondingClientID)
    {
        /*
          these variables are the connections to the mysql database
          */

        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserAlreadyExists = null;
        //PreparedStatement psCheckEmailAlreadyUsed = null;
        ResultSet resultSet = null;

        try{
            /*
              this will attempt to establish a connection with the db
              */

            connection = DriverManager.getConnection("jdbc:mariadb://lazarov.go.ro:3306/RideShare", "root", "chocolate");
            psCheckUserAlreadyExists = connection.prepareStatement("SELECT * FROM database_driver WHERE user_id = ?");
            psCheckUserAlreadyExists.setInt(1, userId);
            //psCheckEmailAlreadyUsed = connection.prepareStatement("SELECT * FROM user_database WHERE email = ? ");
            //psCheckEmailAlreadyUsed.setString(7, email);
            resultSet = psCheckUserAlreadyExists.executeQuery();

            /*
              check if the resultSet is empty
              if true => username already taken => notify user about this
             */

            if(resultSet.isBeforeFirst())
            {
                System.out.println("Username already taken!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("ID already exists!");
                alert.showAndWait();
            }
            else
            {
                psInsert = connection.prepareStatement("INSERT INTO database_driver (user_id, license_plate, ride_started, corresponding_client_id) VALUES (?, ?, ?, ?)");
                psInsert.setInt(1, userId);
                psInsert.setString(2, licensePlate);
                psInsert.setBoolean(3, rideStarted);
                psInsert.setInt(4, correspondingClientID);
                psInsert.executeUpdate();

                //if(role.equals("Client"))
                // changeScene(event, "client.fxml", "RideShare - Client", username, role, name, age, gender, email);
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }finally {
            /*
              close db connections to avoid memory leaks
              */

            if(resultSet != null)
            {
                try{
                    resultSet.close();
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(psCheckUserAlreadyExists != null)
            {
                try{
                    psCheckUserAlreadyExists.close();
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(psInsert != null)
            {
                try {
                    psInsert.close();
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(connection != null)
            {
                try{
                    connection.close();
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void requestRideClient(ActionEvent event, int user_id, String location, String destination)
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement psUpdate = null;
        PreparedStatement psInsert = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mariadb://lazarov.go.ro:3306/RideShare", "root", "chocolate");
            preparedStatement = connection.prepareStatement("SELECT ride_requested, location, destination FROM database_client WHERE user_id = ?");
            preparedStatement.setInt(1, user_id);
            resultSet = preparedStatement.executeQuery();

            if(!resultSet.isBeforeFirst())
            {
                System.out.println("Client not found!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Client not registered.");
                alert.show();
            }
            else
            {
                psUpdate= connection.prepareStatement("UPDATE database_client SET ride_requested = ?, location = ?, destination = ? WHERE user_id = ?");
                psUpdate.setBoolean(1, true);
                psUpdate.setString(2, location);
                psUpdate.setString(3, destination);
                psUpdate.setInt(4, user_id);
                psUpdate.executeUpdate();

                psInsert = connection.prepareStatement("INSERT INTO database_rides (location, destination, requesting_client_id, accepted_driver_id, ride_cancelled) VALUES (?, ?, ?, ?, ?)");
                psInsert.setString(1, location);
                psInsert.setString(2, destination);
                psInsert.setInt(3, user_id);
                psInsert.setInt(4, 0);
                psInsert.setBoolean(5, false);
                psInsert.executeUpdate();
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }finally {
            if(resultSet != null)
            {
                try{
                    resultSet.close();
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(preparedStatement != null)
            {
                try{
                    preparedStatement.close();
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(psUpdate != null)
            {
                try {
                    psUpdate.close();
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(psInsert != null)
            {
                try {
                    psInsert.close();
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(connection != null)
            {
                try{
                    connection.close();
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void acceptRideDriver(ActionEvent event, int client_id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement psUpdate = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mariadb://lazarov.go.ro:3306/RideShare", "root", "chocolate");
            preparedStatement = connection.prepareStatement("SELECT accepted_driver_id FROM database_rides WHERE requesting_client_id = ?");
            preparedStatement.setInt(1, client_id);
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                System.out.println("Request from this client not found!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("The client with the user id: " + client_id + " has not requested any rides!");
                alert.showAndWait();
            } else {
                psUpdate = connection.prepareStatement("UPDATE database_rides SET accepted_driver_id = ? WHERE requesting_client_id = ?");
                psUpdate.setInt(1, getCurrentLoggedInUserID());
                psUpdate.setInt(2, client_id);
                psUpdate.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psUpdate != null) {
                try {
                    psUpdate.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void checkAvailableRides() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mariadb://lazarov.go.ro:3306/RideShare", "root", "chocolate");
            preparedStatement = connection.prepareStatement("SELECT location, destination, requesting_client_id FROM database_rides WHERE accepted_driver_id = ?");
            preparedStatement.setInt(1, 0); // check rides that have driver_id 0
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("No available rides");
               // return null;
            }
            else
            {
                rides.clear();
                while(resultSet.next())
                {
                    String retrievedLocation = resultSet.getString("location");
                    String retrievedDestination = resultSet.getString("destination");
                    int retrievedClientId = resultSet.getInt("requesting_client_id");

                    Ride ride = new Ride(retrievedClientId, retrievedLocation, retrievedDestination);
                    rides.add(ride.toString());

                }
                //return rides;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            /*
             * close all connections to the db
             */

            if(resultSet != null)
            {
                try{
                    resultSet.close();
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(preparedStatement != null)
            {
                try{
                    preparedStatement.close();
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(connection != null)
            {
                try {
                    connection.close();
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
        //return null;
    }

    public static ObservableList<String> getAvailableRidesList()
    {
        return rides;
    }

    public static int getCurrentLoggedInUserID()
    {
        return currentLoggedInUserID;
    }
}
