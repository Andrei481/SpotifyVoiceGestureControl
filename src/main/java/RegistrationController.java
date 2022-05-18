import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationController extends Controller implements Initializable {
    @FXML
    private Button buttonSignup;
    @FXML
    private Button buttonLogin;
    @FXML
    private TextField textFieldUsername, textFieldPassword, textFieldFirstName, textFieldLastName, textFieldAge, textFieldEmail, textFieldLicensePlate;
    @FXML
    private ComboBox<String> comboBoxGender;
    @FXML
    private RadioButton radioButtonDriver;
    @FXML
    private RadioButton radioButtonClient;
    @FXML
    private Label labelLicensePlate;

    private final ObservableList<String> genders = FXCollections.observableArrayList("Male", "Female", "Other");
    private String firstName, lastName, fullName, email, gender, role, username, password, licensePlate = "";
    private int age;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*
         * this part makes sure that only one role can be selected
         * if the user selects the driver role => the client role is deselected automatically and vice-versa
         */
        labelLicensePlate.setVisible(false);
        textFieldLicensePlate.setVisible(false);


        ToggleGroup toggleGroup = new ToggleGroup();
        radioButtonDriver.setToggleGroup(toggleGroup);
        radioButtonClient.setSelected(true);
        radioButtonClient.setToggleGroup(toggleGroup);

        radioButtonDriver.selectedProperty().addListener((observable, wasPreviouslySelected, isNowSelected) -> {
            if(isNowSelected)
            {
                labelLicensePlate.setVisible(true);
                textFieldLicensePlate.setVisible(true);
            }
            else
            {
                labelLicensePlate.setVisible(false);
                textFieldLicensePlate.setVisible(false);
            }
        });

        textFieldAge.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\*d"))
            {
                textFieldAge.setText(newValue.replaceAll("\\D", ""));
            }
        });

        comboBoxGender.setItems(genders);


        buttonSignup.setOnAction(event -> {
            if(!checkEmptyFields())
            {
                firstName = textFieldFirstName.textProperty().getValue();
                lastName = textFieldLastName.textProperty().getValue();
                setName();
                role = ((RadioButton) toggleGroup.getSelectedToggle()).getText();
                age = Integer.parseInt(textFieldAge.textProperty().getValue());
                email = textFieldEmail.textProperty().getValue();
                username = textFieldUsername.textProperty().getValue();
                password = textFieldPassword.textProperty().getValue();
                gender = comboBoxGender.getValue();
                if(role.equals("Driver"))
                {
                    licensePlate = textFieldLicensePlate.textProperty().getValue();
                }
                else
                {
                    licensePlate = "";
                }
                System.out.println("Name: "+fullName);
                System.out.println("Age: "+age);
                System.out.println("Gender: "+gender);
                System.out.println("Email: "+email);
                System.out.println("Username: "+username+"\nPassword: "+password+"\nRole: "+role);
                if(age < 18 && role.equals("Driver"))
                {
                    displayError("You must be at least 18 to register as a driver!");
                    System.out.println("Error: Invalid age!");
                }
                else if(!checkValidEmail(email))
                {
                    displayError("The email address you've entered is invalid!");
                    System.out.println("Error: Invalid email address!");
                }
                else
                {
                    DBUtils.registerUser(event, username, password, role, fullName, age, gender, email, licensePlate);
                }
            }else
            {
                displayError("Please fill all empty fields!");
                System.out.println("Not all fields were completed!");
            }
             // check if username and password contain no whitespace
        });

        buttonLogin.setOnAction(event -> DBUtils.changeScene(event, "main.fxml", "RideShare", null, null, null, 0, null, null, null));
    }

    public boolean checkEmptyFields()
    {
        return textFieldUsername.textProperty().getValue() == null || textFieldPassword.textProperty().getValue() == null || textFieldEmail.textProperty().getValue() == null || textFieldFirstName.textProperty().getValue() == null || textFieldLastName.textProperty().getValue() == null || textFieldAge.textProperty().getValue() == null || comboBoxGender.getValue() == null;
       /* if(role.equals("Driver"))
        {
            if(licensePlate.equals(""))
            {
                return true;
            }
        }*/
    }

    public boolean checkValidEmail(String email)
    {
        boolean result;
        final String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        result = matcher.matches();
        return result;
    }
/*
    public void firstNameEntered(ActionEvent event)
    {
        firstName = textFieldFirstName.textProperty().getValue();
        System.out.println("First name: "+firstName);
    }

    public void lastNameEntered(ActionEvent event)
    {
        lastName = textFieldLastName.textProperty().getValue();
        System.out.println("Last name: "+lastName);
    }
*/
    public void setName()
    {
        if(firstName != null && lastName != null)
        {
            fullName = lastName + " " + firstName;
        }
        System.out.println("Full name: "+fullName);
    }

    /*public void ageEntered(ActionEvent event)
    {
        age = Integer.valueOf(textFieldAge.textProperty().getValue());
        System.out.println("Age: "+age);
    }

    public void emailEntered(ActionEvent event)
    {
        email = textFieldEmail.textProperty().getValue();
        System.out.println("Email address: "+email);
    }

    public void usernameEntered(ActionEvent event)
    {
        username = textFieldUsername.textProperty().getValue();
        System.out.println("Username: "+username);
    }

    public void passwordEntered(ActionEvent event)
    {
        password = textFieldPassword.textProperty().getValue();
        System.out.println("Password: "+password);
    }

    public void setGender(ActionEvent event)
    {
        gender = (String)comboBoxGender.getValue();
        System.out.println("Gender: "+gender);
    }

    public void licensePlateEntered()
    {
        licensePlate = textFieldLicensePlate.textProperty().getValue();
        System.out.println("License plate: "+licensePlate);
    }*/
}
