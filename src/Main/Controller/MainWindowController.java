package Main.Controller;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.io.IOException;

/*
 * Project by Vinzent Brömauer
 * vinz.corno@web.de
 *
 * This file is licensed under the GPL license Version 3.
 * See https://www.gnu.org/licenses/ for the full license.
 */
public class MainWindowController {

    @FXML
    Label messageLabel;
    @FXML
    TabPane tabPane;

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

    public void OnAddResCalc() {
        Tab newTab = new Tab("Resistance Chain");
        Node content = null;
        try {
            content = FXMLLoader.load(getClass().getClassLoader().getResource("Main/fxml/ResistanceCalculation.fxml"));
            newTab.setContent(content);
            tabPane.getTabs().add(newTab);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void OnAddRatioCalc() {
        Tab newTab = new Tab("Resistance Ratio");
        Node content = null;
        try {
            content = FXMLLoader.load(getClass().getClassLoader().getResource("Main/fxml/RatioCalculation.fxml"));
            newTab.setContent(content);
            tabPane.getTabs().add(newTab);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }
}
