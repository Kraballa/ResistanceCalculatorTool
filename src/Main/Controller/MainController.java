package Main.Controller;

import Main.Calc;
import Main.InputCheck;
import Main.ResCalculator;
import Main.ResistorChain;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

/*
 * Project by Vinzent Brömauer
 * vinz.corno@web.de
 *
 * This file is licensed under the GPL license Version 3.
 * See https://www.gnu.org/licenses/ for the full license.
 */

public class MainController implements ChangeListener<ResistorChain> {

    private ResCalculator resCalculator;

    @FXML TextField outputVoltages;
    @FXML TextField inputVoltage;
    @FXML Button confirmCalculate;
    @FXML ListView<ResistorChain> listChains;
    @FXML ListView<String> listDeviation;
    @FXML ChoiceBox<String> choiceGroup;
    @FXML TextField current;

    @FXML TextField ratio;
    @FXML TextField resistance;
    @FXML ChoiceBox<String> choiceGroup2;
    @FXML Button calculate;
    @FXML ListView<ResistorChain> listAmplifierChain;

    private String warningStyle = "-fx-control-inner-background: #FF4136;";
    private String defaultStyle = "";

    @FXML
    public void initialize(){
        choiceGroup.setItems(FXCollections.observableArrayList("e3","e6","e12","e24","e48","e96","e192"));
        choiceGroup.setValue("e48");

        choiceGroup2.setItems(FXCollections.observableArrayList("e3","e6","e12","e24","e48","e96","e192"));
        choiceGroup2.setValue("e48");

        outputVoltages.setTooltip(new Tooltip("desired output voltages"));
        inputVoltage.setTooltip(new Tooltip("input voltage"));
        current.setTooltip(new Tooltip("one or two amp values"));
        confirmCalculate.setTooltip(new Tooltip("calculate resistor chains"));
        choiceGroup.setTooltip(new Tooltip("e series"));
    }

    public void OnConfirmCalculate(){
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

        if (!listChains.getSelectionModel().isEmpty()) {
            listChains.getSelectionModel().selectedItemProperty().removeListener(this);
            listChains.setItems(FXCollections.observableArrayList());
            listDeviation.setItems(FXCollections.observableArrayList());
            listChains.getItems().clear();
        }
        resCalculator = new ResCalculator(voltIn, voltOut, ampere);
        resCalculator.setResistorGroup(InputCheck.parseESeries(choiceGroup.getValue()));
        ObservableList<ResistorChain> items = FXCollections.observableArrayList();

        if (ampere.length == 1) {
            items.add(resCalculator.calculateChain(ampere[0]));
        } else {
            items.addAll(resCalculator.calculateChains());
            items = Calc.removeDuplicates(items);
        }

        System.out.println("calculated res-chains");

        listChains.setItems(items);
        listChains.getSelectionModel().selectFirst();
        setListDeviation(listChains.getSelectionModel().getSelectedItem());
        listChains.getSelectionModel().selectedItemProperty().addListener(this);
    }

    public void OnConfirmRatioCalculate(){
        if (!inputExists2()) {
            return;
        }

        double ratioo = InputCheck.parseDoubleArray(ratio.getText(), 1)[0];
        double[] resistBorder = InputCheck.parseDoubleArray(resistance.getText(), 2);

        if (!listAmplifierChain.getSelectionModel().isEmpty()) {
            listAmplifierChain.getSelectionModel().selectedItemProperty().removeListener(this);
            listAmplifierChain.setItems(FXCollections.observableArrayList());
            listAmplifierChain.setItems(FXCollections.observableArrayList());
            listAmplifierChain.getItems().clear();
        }

        ObservableList<ResistorChain> items = FXCollections.observableArrayList();
        int group = Integer.parseInt(choiceGroup2.getValue().replaceAll("[^0123456789]", ""));
        items.addAll(ResCalculator.getBestRatioChain(resistBorder, ratioo, group));
        listAmplifierChain.setItems(items);
    }

    private boolean inputExists() {
        boolean ret = true;

        if(inputVoltage.getText().trim().equals("")){
            inputVoltage.setStyle(warningStyle);
            ret = false;
        }else{
            inputVoltage.setStyle(defaultStyle);
        }

        if(outputVoltages.getText().trim().equals("")){
            outputVoltages.setStyle(warningStyle);
            ret = false;
        }else{
            outputVoltages.setStyle(defaultStyle);
        }

        if(current.getText().trim().equals("")){
            current.setStyle(warningStyle);
            ret = false;
        }else{
            current.setStyle(defaultStyle);
        }

        return ret;
    }

    private boolean inputExists2() {
        boolean ret = true;
        if(ratio.getText().trim().equals("")){
            ratio.setStyle(warningStyle);
            ret = false;
        }else{
            ratio.setStyle(defaultStyle);
        }

        if(resistance.getText().trim().equals("")){
            resistance.setStyle(warningStyle);
            ret = false;
        }else{
            resistance.setStyle(defaultStyle);
        }

        return ret;
    }

    private void setListDeviation(ResistorChain chain){
        ObservableList<String> comparisons = FXCollections.observableArrayList();
        double totalRes = 0;
        for (int i = 0; i < chain.resistances.length; i++) {
            totalRes += chain.resistances[i];
        }
        int optimalRes = (int) Math.round(resCalculator.getVoltIn() / chain.getCurrent());
        comparisons.add("total resistor desired: " + optimalRes + "Ω");
        comparisons.add("total resistor actual:  " + totalRes + "Ω");
        comparisons.add("optimal amp: " + chain.getCurrent() + "A");
        comparisons.add("");

        for (int i = 0; i < chain.getIst().length; i++){
            comparisons.add("desired: " + chain.getSoll()[i] + "V, actual: " + Calc.roundWithComma(chain.getIst()[i],5) + "V ("
                    + Calc.roundWithComma(chain.getIst()[i]/chain.getSoll()[i],3)*100 + "%)");
        }
        comparisons.add("");
        if(chain.getDeviation() >= 0.5){
            comparisons.add("Warning! extreme deviation");
            listDeviation.setStyle(warningStyle);
        }else if(chain.getDeviation() >= 0.08){
            comparisons.add("suboptimal deviation");
            listDeviation.setStyle("-fx-control-inner-background: #ffd6d6;");
        }else{
            listDeviation.setStyle(defaultStyle);
        }

        comparisons.add("deviation coefficient: " + chain.getDeviation());
        listDeviation.setItems(comparisons);
    }


    public void OnOpenAbout(){
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("fxml/About.fxml"));
            Stage stage = new Stage();
            stage.setTitle("About");
            stage.setScene(new Scene(root, 550, 450));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changed(ObservableValue<? extends ResistorChain> observable, ResistorChain oldValue, ResistorChain newValue) {
        setListDeviation(listChains.getSelectionModel().getSelectedItem());
    }
}
