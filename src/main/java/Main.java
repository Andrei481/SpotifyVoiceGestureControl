import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends javafx.application.Application{
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
        stage.setTitle("RideShare");
        stage.setScene(new Scene(root, 800, 600));
        stage.getIcons().add(new Image("/images/logo177.png"));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
    //  Reminder: Delete "target" directory before compiling a different branch
}
