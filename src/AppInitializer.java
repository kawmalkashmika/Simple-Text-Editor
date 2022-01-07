import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        AnchorPane pane= FXMLLoader.load(getClass().getResource("view/MainForm.fxml"));
        Scene scene=new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
