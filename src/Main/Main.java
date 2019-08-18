package Main;

import Main.Controller.MainWindowController;
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
        //load main fxml. it contains references to the fxml for the actual program
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/MainWindow.fxml"));
        Parent root = loader.load();
        MainWindowController controller = loader.getController();
        //pass over hostServices so the controller can open the web browser
        controller.setHostServices(getHostServices());
        primaryStage.setTitle("Resistance Calculator Tool 1.1.1");
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.setMinHeight(300);
        primaryStage.setMinWidth(380);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
