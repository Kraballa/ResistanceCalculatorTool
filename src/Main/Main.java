package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
 * Project by Vinzent Brömauer
 * vinz.corno@web.de
 *
 * This file is licensed under the GPL license Version 3.
 * See https://www.gnu.org/licenses/ for the full license.
 */

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("fxml/MainWindow.fxml"));
        primaryStage.setTitle("Resistor Chain Calculator 1.0.3");
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.setMinHeight(300);
        primaryStage.setMinWidth(380);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
