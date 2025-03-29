package vue;
import javafx.application.Application;
import javafx.stage.Stage;

public class ApplicationLoto extends Application {

    public void start(Stage stage)   {
        stage.setTitle("Hello JavaFX");
        stage.setWidth(400);
        stage.setHeight(150);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        ApplicationLoto.launch();
    }

}