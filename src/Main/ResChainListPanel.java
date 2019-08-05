package Main;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;

import java.util.List;

public class ResChainListPanel extends SplitPane implements ChangeListener<ResistorChain> {

    private ListView<ResistorChain> LeftPanel;
    private ListView<String> RightPanel;
    private ResCalculator resCalculator;

    private String warningStyle = "-fx-control-inner-background: #FF4136;";
    private String suboptimalStyle = "-fx-control-inner-background: #ffd6d6;";
    private String defaultStyle = "";

    public ResChainListPanel(ListView<ResistorChain> list, ListView<String> text, ResCalculator resCalc) {
        LeftPanel = list;
        RightPanel = text;
        resCalculator = resCalc;
    }

    public ResChainListPanel(ListView<ResistorChain> list, ListView<String> text) {
        LeftPanel = list;
        RightPanel = text;
    }

    public void setResCalculator(ResCalculator newVal) {
        resCalculator = newVal;
    }

    /**
     * Setup a List of ResistorChains as well as their more detailed description in a second list.
     *
     * @param resList list of resistances to display
     */
    public void DisplayResistorList(List<ResistorChain> resList) {
        if (!LeftPanel.getSelectionModel().isEmpty()) {
            LeftPanel.getSelectionModel().selectedItemProperty().removeListener(this);
        }

        ObservableList<ResistorChain> items = FXCollections.observableArrayList();
        items.addAll(resList);
        LeftPanel.setItems(items);
        LeftPanel.getSelectionModel().selectedItemProperty().addListener(this);
        LeftPanel.getSelectionModel().selectFirst();
    }

    private void displayInfo(ResistorChain chain) {
        ObservableList<String> comparisons = FXCollections.observableArrayList();
        double totalRes = 0;
        for (int i = 0; i < chain.resistances.length; i++) {
            totalRes += chain.resistances[i];
        }
        if (resCalculator != null) {
            int optimalRes = (int) Math.round(resCalculator.getVoltIn() / chain.getCurrent());
            comparisons.add("total resistance desired: " + optimalRes + "Ω");
        }

        comparisons.add("total resistance actual:  " + totalRes + "Ω");
        comparisons.add("optimal ampere: " + chain.getCurrent() + "A");
        comparisons.add("");

        for (int i = 0; i < chain.getIst().length; i++) {
            comparisons.add("desired: " + chain.getSoll()[i] + "V, actual: " + Calc.roundWithComma(chain.getIst()[i], 5) + "V ("
                    + Calc.roundWithComma(chain.getIst()[i] / chain.getSoll()[i], 3) * 100 + "%)");
        }
        comparisons.add("");
        if (chain.getDeviation() >= 0.5) {
            comparisons.add("Warning! extreme deviation");
            RightPanel.setStyle(warningStyle);
        } else if (chain.getDeviation() >= 0.08) {
            comparisons.add("suboptimal deviation");
            RightPanel.setStyle(suboptimalStyle);
        } else {
            RightPanel.setStyle(defaultStyle);
        }

        comparisons.add("deviation coefficient: " + chain.getDeviation());
        RightPanel.setItems(comparisons);
    }

    /**
     * Gets called whenever an item is selected in the LeftPanel List. Runs a method that changes the text of RightPanel
     * with detailed information about the newly selected ResistorChain.
     *
     * @param observable
     * @param oldValue
     * @param newValue
     */
    @Override
    public void changed(ObservableValue<? extends ResistorChain> observable, ResistorChain oldValue, ResistorChain newValue) {
        displayInfo(newValue);
    }
}
