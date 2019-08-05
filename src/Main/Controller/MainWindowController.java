package Main.Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainWindowController {

    public void OnOpenAbout() {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("Main/fxml/About.fxml"));
            Stage stage = new Stage();
            stage.setTitle("About");
            stage.setScene(new Scene(root, 550, 450));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
