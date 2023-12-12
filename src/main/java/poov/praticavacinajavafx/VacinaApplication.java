package poov.praticavacinajavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class VacinaApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(VacinaApplication.class.getResource("home-window.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Gerenciador de vacinas!");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icon.png"))));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}