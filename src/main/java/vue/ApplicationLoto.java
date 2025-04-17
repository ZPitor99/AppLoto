package vue;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class ApplicationLoto extends Application {

    public void start(Stage stage) {
        stage.setTitle("AppLoto");
        stage.centerOnScreen();
        VBoxRoot root = new VBoxRoot();
        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.setResizable(false);
        File css = new File("css" + File.separator + "style.css");
        scene.getStylesheets().add(css.toURI().toString());
        stage.show();
    }

    public static void main(String[] args) {
        ApplicationLoto.launch();
    }

}