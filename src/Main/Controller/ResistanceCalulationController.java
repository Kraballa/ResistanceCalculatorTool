package Main.Controller;

import Main.ResistorChain;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ResistanceCalulationController {

    @FXML
    TextField outputVoltages;
    @FXML
    TextField inputVoltage;
    @FXML
    Button confirmCalculate;
    @FXML
    ListView<ResistorChain> listChains;
    @FXML
    ListView<String> listDeviation;
    @FXML
    ChoiceBox<String> choiceGroup;
    @FXML
    TextField current;


}
