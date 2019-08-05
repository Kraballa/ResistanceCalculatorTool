package Main.Controller;

import Main.ResistorChain;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class RatioCalculationController {

    @FXML
    TextField ratio;
    @FXML
    TextField resistance;
    @FXML
    ChoiceBox<String> eSeries;
    @FXML
    Button calculate;
    @FXML
    ListView<ResistorChain> listAmplifierChain;
}
