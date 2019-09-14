package selenium.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JavaFxMain extends Application {

    public JavaFxMain() {
        System.out.println(Thread.currentThread().getName() + ": AppMain() 호출");
    }

    @Override
    public void init() {
        System.out.println(Thread.currentThread().getName() + ": init() 호출");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("main_layout.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @Override
    public void stop() {
        System.out.println(Thread.currentThread().getName() + ": stop() 호출");
    }

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + ": main() 호출");
        launch(args);
    }

}
