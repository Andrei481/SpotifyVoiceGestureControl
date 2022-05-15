import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.sql.*;

public class DBUtils extends Controller{
    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username, String role, String name, int age, String gender, String email, String licensePlate)
    {
        Parent root = null;
        if(username != null && role != null)
        {
            try{
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root = loader.load();
                LogInController logInController = loader.getController();
                logInController.setUserInfo(username, role, name, age, gender, email, licensePlate);
            }catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            try{
                root = FXMLLoader.load(DBUtils.class.getResource(fxmlFile));
            }catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 600, 600));
        stage.show();
    }

    public static void registerUser(ActionEvent event, String username, String password, String role, String name, int age, String gender, String email, String licensePlate)
    {
        /**
         * these variables are the connections to the mysql database
          */

        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserAlreadyExists = null;
        //PreparedStatement psCheckEmailAlreadyUsed = null;
        ResultSet resultSet = null;

        try{
            /**
             * this will attempt to establish a connection with the db
              */

            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/rideshare_upt", "root", "t00r_sef");
            psCheckUserAlreadyExists = connection.prepareStatement("SELECT * FROM user_database WHERE username = ?");
            psCheckUserAlreadyExists.setString(1, username);
            //psCheckEmailAlreadyUsed = connection.prepareStatement("SELECT * FROM user_database WHERE email = ? ");
            //psCheckEmailAlreadyUsed.setString(7, email);
            resultSet = psCheckUserAlreadyExists.executeQuery();

            /**
             * check if the resultSet is empty
             * if true => username already taken => notify user about this
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
                psInsert = connection.prepareStatement("INSERT INTO user_database (username, password, role, name, age, gender, email, license_plate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                psInsert.setString(1, username);
                psInsert.setString(2, password);
                psInsert.setString(3, role);
                psInsert.setString(4, name);
                psInsert.setInt(5, age);
                psInsert.setString(6, gender);
                psInsert.setString(7, email);
                psInsert.setString(8, licensePlate);
                psInsert.executeUpdate();

                if(role.equals("Driver"))
                    changeScene(event, "login_driver.fxml", "Successful login driver", username, role, name, age, gender, email, licensePlate);
                else if(role.equals("Client"))
                    changeScene(event,"login_client.fxml", "Successful login client", username, role, name, age, gender, email, licensePlate);
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }finally {
            /**
             * close db connections to avoid memory leaks
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

    public static void loginUser(ActionEvent event, String username, String password)
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/rideshare_upt", "root", "t00r_sef");
            preparedStatement = connection.prepareStatement("SELECT password, role, name, age, gender, email, license_plate FROM user_database WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            /**
             * if this is false => username not found in the db => notify user
              */

            if(!resultSet.isBeforeFirst())
            {
                System.out.println("User not found!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Incorrect credentials.");
                alert.show();
            }
            /**
             *  if the user is found in the DB => compare the password they wrote with the one originally in the db
             */
            else
            {
                /***
                 *  loop through the result set and retrieve each password and role
                 */
                while(resultSet.next())
                {
                    String retrievedPassword = resultSet.getString("password");
                    String retrievedRole = resultSet.getString("role");
                    String retrievedName = resultSet.getString("name");
                    Integer retrievedAge = resultSet.getInt("age");
                    String retrievedGender = resultSet.getString("gender");
                    String retrievedEmail = resultSet.getString("email");
                    String retrievedPlate = resultSet.getString("license_plate");
                    /**
                     * check each password
                     * if true => login the user
                     * else => alert user
                     */
                    if(retrievedPassword.equals(password))
                    {
                        if(retrievedRole.equals("Driver"))
                            changeScene(event, "login_driver.fxml", "Successful login driver", username, retrievedRole, retrievedName, retrievedAge, retrievedGender, retrievedEmail, retrievedPlate);
                        else if(retrievedRole.equals("Client"))
                            changeScene(event,"login_client.fxml", "Successful login client", username, retrievedRole, retrievedName, retrievedAge, retrievedGender, retrievedEmail, retrievedPlate);
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
            /**
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
}
