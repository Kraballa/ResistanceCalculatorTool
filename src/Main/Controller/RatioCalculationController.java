package Main.Controller;

import Main.InputCheck;
import Main.Logic.ResistanceCalculator;
import Main.Logic.ResistanceChain;
import Main.ResChainListPanel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

public class RatioCalculationController {

    @FXML
    TextField ratio;
    @FXML
    TextField resistance;
    @FXML
    ChoiceBox<String> eSeries;
    @FXML
    ListView<ResistanceChain> chainList;
    @FXML
    ListView<String> detailList;

    private ResChainListPanel resListPanel;

    private String warningStyle = "-fx-control-inner-background: #FF4136;";
    private String defaultStyle = "";

    @FXML
    public void initialize() {
        eSeries.setItems(FXCollections.observableArrayList("e3", "e6", "e12", "e24", "e48", "e96", "e192"));
        eSeries.setValue("e48");

        eSeries.setTooltip(new Tooltip("e series"));
    }

    public void OnConfirmRatioCalculate() {
        if (!inputExists()) {
            return;
        }

        double parsedRatio = InputCheck.parseDoubleArray(ratio.getText(), 1)[0];
        double[] resistBorder = InputCheck.parseDoubleArray(resistance.getText(), 2);

        ObservableList<ResistanceChain> resistorChains = FXCollections.observableArrayList();
        int eSeries = Integer.parseInt(this.eSeries.getValue().replaceAll("[^0123456789]", ""));
        resistorChains.addAll(ResistanceCalculator.getBestRatioChain(resistBorder, parsedRatio, eSeries));
        resListPanel = new ResChainListPanel(chainList, detailList);
        resListPanel.DisplayResistorList(resistorChains);
    }

    private boolean inputExists() {
        boolean ret = true;
        if (ratio.getText().trim().equals("")) {
            ratio.setStyle(warningStyle);
            ret = false;
        } else {
            ratio.setStyle(defaultStyle);
        }

        if (resistance.getText().trim().equals("")) {
            resistance.setStyle(warningStyle);
            ret = false;
        } else {
            resistance.setStyle(defaultStyle);
        }

        return ret;
    }
}
