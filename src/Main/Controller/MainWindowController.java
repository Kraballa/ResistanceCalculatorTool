package Main.Controller;

import javafx.application.HostServices;
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

    private HostServices hostServices;

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    public void OnViewGitHubRepo() {
        try {
            hostServices.showDocument("https://github.com/Kraballa/ResistanceCalculatorTool");
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }
}
