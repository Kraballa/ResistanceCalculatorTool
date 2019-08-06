package Main.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MainWindowController {

    @FXML
    Label messageLabel;

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

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
