package Main;

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

public class Controller implements ChangeListener<ResistorChain>{

    private Berechner berechner;

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
        if(checkSyntax()){
            double voltIn = parseInputString(inputVoltage.getText(),1)[0];
            System.out.println("voltin successfully parsed");
            double[] voltOut = parseInputString(outputVoltages.getText(),0);
            System.out.println("voltout successfully parsed");
            double[] ampere = parseInputString(current.getText(),2);
            System.out.println("ampere successfully parsed");

            if(checkSemantics(voltIn,voltOut,ampere)){
                if(!listChains.getSelectionModel().isEmpty()){
                    listChains.getSelectionModel().selectedItemProperty().removeListener(this);
                    listChains.setItems(FXCollections.observableArrayList());
                    listDeviation.setItems(FXCollections.observableArrayList());
                    listChains.getItems().clear();
                }
                berechner = new Berechner(voltIn,voltOut, ampere);
                berechner.setResistorGroup(Integer.parseInt(choiceGroup.getValue().replaceAll("[^0123456789]","")));
                ObservableList<ResistorChain> items = FXCollections.observableArrayList();

                if(ampere.length == 1){
                    items.add(berechner.calculateChain(ampere[0]));
                }else{
                    items.addAll(berechner.calculateChains());
                    items = removeDuplicates(items);
                }
                listChains.setItems(items);
                listChains.getSelectionModel().selectFirst();
                setListDeviation(listChains.getSelectionModel().getSelectedItem());
                listChains.getSelectionModel().selectedItemProperty().addListener(this);

            }
        }
    }

    public void OnConfirmRatioCalculate(){
        if(checkSyntax2()){
            double ratioo = parseInputString(ratio.getText(),1)[0];
            double[] resistBorder = parseInputString(resistance.getText(),2);

            if(!listAmplifierChain.getSelectionModel().isEmpty()){
                listAmplifierChain.getSelectionModel().selectedItemProperty().removeListener(this);
                listAmplifierChain.setItems(FXCollections.observableArrayList());
                listAmplifierChain.setItems(FXCollections.observableArrayList());
                listAmplifierChain.getItems().clear();
            }

            ObservableList<ResistorChain> items = FXCollections.observableArrayList();
            int group = Integer.parseInt(choiceGroup2.getValue().replaceAll("[^0123456789]",""));
            items.addAll(Berechner.getBestRatioChain(resistBorder,ratioo,group));
            listAmplifierChain.setItems(items);
        }
    }

    private boolean checkSyntax(){
        boolean ret = true;
        if(inputVoltage.getText().trim().equals("")){
            inputVoltage.setStyle("-fx-background-color: #FF4136;");
            ret = false;
        }else{
            inputVoltage.setStyle("-fx-background-border-color: default;");
        }

        if(outputVoltages.getText().trim().equals("")){
            outputVoltages.setStyle("-fx-background-color: #FF4136;");
            ret = false;
        }else{
            outputVoltages.setStyle("-fx-background-border-color: default;");
        }

        if(current.getText().trim().equals("")){
            current.setStyle("-fx-background-color: #FF4136;");
            ret = false;
        }else{
            current.setStyle("-fx-background-border-color: default ;");
        }

        return ret;
    }

    private boolean checkSyntax2(){
        boolean ret = true;
        if(ratio.getText().trim().equals("")){
            ratio.setStyle("-fx-background-color: #FF4136;");
            ret = false;
        }else{
            ratio.setStyle("-fx-background-border-color: default;");
        }

        if(resistance.getText().trim().equals("")){
            resistance.setStyle("-fx-background-color: #FF4136;");
            ret = false;
        }else{
            resistance.setStyle("-fx-background-border-color: default;");
        }

        return ret;
    }

    private boolean checkSemantics(double voltIn, double[] voltOut, double[] ampere){
        boolean ret = true;
        //check voltIn
        if(voltIn <= 0){
            System.out.println("voltIn <= 0");
            ret = false;
        }else{
            for (double aVoltOut : voltOut) {
                if (voltIn < aVoltOut) {
                    System.out.println("voltIn (" + voltIn + ") is not bigger than atleast one voltOut(" + aVoltOut + ")");
                    ret = false;
                }
            }
        }

        //check voltOut
        for (double aVoltOut : voltOut) {
            if (aVoltOut < 0) {
                System.out.println("voltOut cannot have values < 0");
                ret = false;
            }
        }

        //check ampere
        for (double anAmpere : ampere) {
            if (anAmpere < 0) {
                System.out.println("ampere cannot have values < 0");
                ret = false;
            }
        }
        return ret;
    }

    private ObservableList<ResistorChain> removeDuplicates(ObservableList<ResistorChain> chainList){
        ObservableList<ResistorChain> uniqueList = FXCollections.observableArrayList();

        uniqueList.add(chainList.get(0));
        if(chainList.size() > 1){
            for(int i = 1; i < chainList.size(); i++){
                if(!uniqueList.contains(chainList.get(i))){
                    uniqueList.add(chainList.get(i));
                }
            }
        }

        return uniqueList;
    }

    private void setListDeviation(ResistorChain chain){
        ObservableList<String> comparisons = FXCollections.observableArrayList();
        double totalRes = 0;
        for (int i = 0; i < chain.resistances.length; i++) {
            totalRes += chain.resistances[i];
        }
        int optimalRes = (int)Math.round(berechner.getVoltIn() / chain.getCurrent());
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
            listDeviation.setStyle("-fx-control-inner-background: #ff7777;");
        }else if(chain.getDeviation() >= 0.08){
            comparisons.add("suboptimal deviation");
            listDeviation.setStyle("-fx-control-inner-background: #ffd6d6;");
        }else{
            listDeviation.setStyle("-fx-control-inner-background: #ffffff;");
        }

        comparisons.add("deviation coefficient: " + chain.getDeviation());
        listDeviation.setItems(comparisons);
    }

    private double[] parseInputString(String string, int length){
        int actualLength = length;
        String[] split = string.trim().split("[ ]+");
        double ret[] = new double[split.length];
        if(length <= 0){
            actualLength = ret.length;
        }
        for(int i = 0; i < Math.min(ret.length,actualLength); i++){
            ret[i] = Double.parseDouble(split[i].replaceAll(",","."));
        }
        return ret;
    }

    public void OnOpenAbout(){
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("About.fxml"));
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
