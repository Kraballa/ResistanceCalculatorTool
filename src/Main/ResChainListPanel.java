package Main;

import Main.Logic.ResistanceChain;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;

import java.util.List;

public class ResChainListPanel extends SplitPane implements ChangeListener<ResistanceChain> {

    private ListView<ResistanceChain> LeftPanel;
    private ListView<String> RightPanel;

    private String warningStyle = "-fx-control-inner-background: #FF4136;";
    private String suboptimalStyle = "-fx-control-inner-background: #ffd6d6;";
    private String defaultStyle = "";

    public ResChainListPanel(ListView<ResistanceChain> list, ListView<String> text) {
        LeftPanel = list;
        RightPanel = text;
    }
    /**
     * Setup a List of ResistorChains as well as their more detailed description in a second list.
     *
     * @param resList list of resistances to display
     */
    public void DisplayResistorList(List<ResistanceChain> resList) {
        if (!LeftPanel.getSelectionModel().isEmpty()) {
            LeftPanel.getSelectionModel().selectedItemProperty().removeListener(this);
        }

        ObservableList<ResistanceChain> items = FXCollections.observableArrayList();
        items.addAll(resList);
        //items = items.sorted();

        LeftPanel.setItems(items);
        LeftPanel.getSelectionModel().selectedItemProperty().addListener(this);
        LeftPanel.getSelectionModel().selectFirst();
    }

    private void displayInfo(ResistanceChain chain) {
        ObservableList<String> comparisons = FXCollections.observableArrayList();

        double totalRes = Calc.sumup(chain.getResistances());
        String totalResText = "total resistance actual:  " + totalRes + "Ω";
        if (chain.getDesired() != null) {
            double optimalTotalRes = Calc.sumup(chain.getDesired());
            double ratio = 100 * totalRes / optimalTotalRes;
            comparisons.add("total resistance desired: " + optimalTotalRes + "Ω");
            totalResText += "(" + ratio + "%)";
        }


        comparisons.add(totalResText);

        if (chain.getAmpere() != 0) {
            comparisons.add("optimal ampere: " + chain.getAmpere() + "A");
            comparisons.add("");

            for (int i = 0; i < chain.getOptimalOutputs().length; i++) {
                comparisons.add("desired: " + chain.getOptimalOutputs()[i] + "V, actual: " + chain.getOutputs()[i] + "V ("
                        + (chain.getOptimalOutputs()[i] / chain.getOutputs()[i] * 100) + "%)");
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
        }


        RightPanel.setItems(comparisons);
    }

    @Override
    public void changed(ObservableValue<? extends ResistanceChain> observable, ResistanceChain oldValue, ResistanceChain newValue) {
        if (newValue != null) {
            displayInfo(newValue);
        }
    }
}
