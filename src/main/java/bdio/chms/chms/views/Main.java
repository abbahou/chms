package bdio.chms.chms.views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/bdio/chms/chms/fxml/Login.fxml"));
        BorderPane xA = fxmlLoader.load();

        Scene scene = new Scene(xA, 900, 600);

        Image icon = new Image(getClass().getResourceAsStream("/bdio/chms/chms/images/heartbeats_logo.png"));
        stage.getIcons().add(icon);
        stage.setTitle("CarePoint");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        launch();
    }


}
