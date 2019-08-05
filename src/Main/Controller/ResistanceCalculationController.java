package Main.Controller;

import Main.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

public class ResistanceCalculationController {

    @FXML
    private TextField outputVoltages;
    @FXML
    private TextField inputVoltage;
    @FXML
    private ChoiceBox<String> choiceGroup;
    @FXML
    private TextField current;
    @FXML
    ListView<ResistorChain> chainList;
    @FXML
    ListView<String> detailList;

    private ResChainListPanel resListPanel;

    private String warningStyle = "-fx-control-inner-background: #FF4136;";
    private String defaultStyle = "";

    @FXML
    public void initialize() {
        choiceGroup.setItems(FXCollections.observableArrayList("e3", "e6", "e12", "e24", "e48", "e96", "e192"));
        choiceGroup.setValue("e48");

        outputVoltages.setTooltip(new Tooltip("desired output voltages"));
        inputVoltage.setTooltip(new Tooltip("input voltage"));
        current.setTooltip(new Tooltip("one or two amp values"));
        choiceGroup.setTooltip(new Tooltip("e series"));
    }

    public void OnConfirmCalculate() {
        if (!inputExists()) {
            return;
        }

        //parse input
        double voltIn = InputCheck.parseVoltIn(inputVoltage.getText());
        double[] voltOut = InputCheck.parseVoltOut(outputVoltages.getText());
        double[] ampere = InputCheck.parseAmpere(current.getText());

        System.out.println("parsed input");

        if (!(InputCheck.checkAmpere(ampere) && InputCheck.checkVoltOut(voltOut) && InputCheck.checkVoltIn(voltIn, voltOut))) {
            return;
        }

        System.out.println("inputs okay");

        ResCalculator resCalculator = new ResCalculator(voltIn, voltOut, ampere);
        resCalculator.setResistorGroup(InputCheck.parseESeries(choiceGroup.getValue()));
        ObservableList<ResistorChain> resistorChains = FXCollections.observableArrayList();

        if (ampere.length == 1) {
            resistorChains.add(resCalculator.calculateChain(ampere[0]));
        } else {
            resistorChains.addAll(resCalculator.calculateChains());
            resistorChains = Calc.removeDuplicates(resistorChains);
        }
        resListPanel = new ResChainListPanel(chainList, detailList, resCalculator);
        resListPanel.DisplayResistorList(resistorChains);

        System.out.println("calculated res-chains");
    }

    private boolean inputExists() {
        boolean ret = true;

        if (inputVoltage.getText().trim().equals("")) {
            inputVoltage.setStyle(warningStyle);
            ret = false;
        } else {
            inputVoltage.setStyle(defaultStyle);
        }

        if (outputVoltages.getText().trim().equals("")) {
            outputVoltages.setStyle(warningStyle);
            ret = false;
        } else {
            outputVoltages.setStyle(defaultStyle);
        }

        if (current.getText().trim().equals("")) {
            current.setStyle(warningStyle);
            ret = false;
        } else {
            current.setStyle(defaultStyle);
        }
        return ret;
    }
}
