package Main.Controller;

import Main.Export.ExportToSpice;
import Main.InputCheck;
import Main.Logic.ResistanceCalculator;
import Main.Logic.ResistanceChain;
import Main.UI.ResChainListPanel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;

/*
 * Project by Vinzent Brömauer
 * vinz.corno@web.de
 *
 * This file is licensed under the GPL license Version 3.
 * See https://www.gnu.org/licenses/ for the full license.
 */
public class ResistanceCalculationController {

    private ResChainListPanel resListPanel;

    @FXML
    private TextField outputVoltages;
    @FXML
    private TextField inputVoltage;
    @FXML
    private ChoiceBox<String> eSeries;
    @FXML
    private TextField current;
    @FXML
    ListView<ResistanceChain> chainList;
    @FXML
    TextArea detailArea;
    @FXML
    Canvas canvas;

    @FXML
    public void initialize() {
        eSeries.setValue("e96");

        outputVoltages.setTooltip(new Tooltip("desired output voltages"));
        inputVoltage.setTooltip(new Tooltip("input voltage"));
        eSeries.setTooltip(new Tooltip("e series to look in for available resistors"));
        current.setTooltip(new Tooltip("one or two amp values"));

        resListPanel = new ResChainListPanel(chainList, detailArea, canvas);
    }

    public void OnConfirmCalculate() {
        double voltIn;
        double[] voltOut;
        double[] ampere;
        int eSeries;

        try {
            voltIn = InputCheck.parseDoubleGreaterZero(inputVoltage);
            voltOut = InputCheck.parseDoubleArray(outputVoltages);
            ampere = InputCheck.parseDoubleArray(current);
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
        if (ampere.length == 1) {
            resistorChains.add(ResistanceCalculator.calcResistanceChain(voltIn, voltOut, ampere[0], eSeries));
        } else {
            resistorChains.addAll(ResistanceCalculator.calcResistanceChains(voltIn, voltOut, ampere, eSeries));
        }
        resListPanel.DisplayResistorList(resistorChains);
    }

    public void OnExport() {
        ResistanceChain selected = resListPanel.getSelected();
        ExportToSpice.exportResChain(selected);
    }
}
