package Main.Controller;

import Main.Export.ExportToSpice;
import Main.InputCheck;
import Main.Logic.ResistanceCalculator;
import Main.Logic.ResistanceChain;
import Main.UI.ResChainListPanel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/*
 * Project by Vinzent Brömauer
 * vinz.corno@web.de
 *
 * This file is licensed under the GPL license Version 3.
 * See https://www.gnu.org/licenses/ for the full license.
 */
public class RatioCalculationController {

    private ResChainListPanel resListPanel;

    @FXML
    TextField ratio;
    @FXML
    TextField resistance;
    @FXML
    ChoiceBox<String> eSeries;
    @FXML
    ListView<ResistanceChain> chainList;
    @FXML
    TextArea detailArea;
    @FXML
    Button export;

    @FXML
    public void initialize() {
        eSeries.setValue("e96");

        ratio.setTooltip(new Tooltip("the ratio between the first and second resistor"));
        resistance.setTooltip(new Tooltip("the combined resistance of the two resistors"));
        eSeries.setTooltip(new Tooltip("e series to look in for available resistors"));

        resListPanel = new ResChainListPanel(chainList, detailArea);
    }

    public void OnConfirmRatioCalculate() {
        double parsedRatio;
        double[] resistBorder;
        int eSeries;

        try {
            parsedRatio = InputCheck.parseDoubleGreaterZero(ratio);
            resistBorder = InputCheck.parseDoubleArray(resistance, 2);
            eSeries = InputCheck.parseESeries(this.eSeries.getValue());
        } catch (NumberFormatException e) {
            System.out.println("one or more inputs are not numbers");
            e.printStackTrace();
            return;
        } catch (IllegalArgumentException e) {
            System.out.println("one or more inputs doesn't match the expected values");
            e.printStackTrace();
            return;
        }
        ObservableList<ResistanceChain> resistorChains = FXCollections.observableArrayList();
        resistorChains.addAll(ResistanceCalculator.calcChainsFromRatio(parsedRatio, resistBorder, eSeries));
        resListPanel.DisplayResistorList(resistorChains);

        export.setDisable(false);
    }

    public void OnExport() {
        ResistanceChain selected = resListPanel.getSelected();
        if (selected != null) {
            ExportToSpice.exportResChain(selected);
        }
    }
}
