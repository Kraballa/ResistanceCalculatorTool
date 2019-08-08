package Main;

import Main.Logic.ResistanceChain;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;

import java.util.List;

/*
 * Project by Vinzent Brömauer
 * vinz.corno@web.de
 *
 * This file is licensed under the GPL license Version 3.
 * See https://www.gnu.org/licenses/ for the full license.
 */
public class ResChainListPanel extends SplitPane implements ChangeListener<ResistanceChain> {

    private ListView<ResistanceChain> LeftPanel;
    private ListView<String> RightPanel;

    private String warningStyle = "-fx-control-inner-background: #ffa1a1;";
    private String suboptimalStyle = "-fx-control-inner-background: #ffe0c7;";
    private String defaultStyle = "";

    public ResChainListPanel(ListView<ResistanceChain> list, ListView<String> text) {
        LeftPanel = list;
        RightPanel = text;
    }

    /**
     * Setup a List of ResistorChains as well as their more detailed description in a second list.
     * Also handles ActionListener.
     *
     * @param resList list of resistances to display
     */
    public void DisplayResistorList(List<ResistanceChain> resList) {
        if (!LeftPanel.getSelectionModel().isEmpty()) {
            LeftPanel.getSelectionModel().selectedItemProperty().removeListener(this);
        }

        ObservableList<ResistanceChain> items = FXCollections.observableArrayList();
        items.addAll(resList);

        LeftPanel.setItems(items);

        LeftPanel.setCellFactory(param -> new ListCell<ResistanceChain>() {
            @Override
            protected void updateItem(ResistanceChain item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle(null);
                } else {
                    setText(item.toString());
                    if (item.getVoltIn() != 0 && item.getAmpere() != 0) {
                        if (item.getDeviation() >= 0.5) {
                            setStyle(warningStyle);
                        } else if (item.getDeviation() >= 0.08) {
                            setStyle(suboptimalStyle);
                        } else {
                            setStyle(defaultStyle);
                        }
                    } else {
                        setStyle(defaultStyle);
                    }
                }
            }
        });

        LeftPanel.getSelectionModel().selectedItemProperty().addListener(this);
        LeftPanel.getSelectionModel().selectFirst();
    }

    /**
     * Describes what information the detailed description contains. Mainly serves the purpose of only displaying information
     * that is available.
     *
     * @param chain the chain that stores the information
     */
    private void displayInfo(ResistanceChain chain) {
        ObservableList<String> comparisons = FXCollections.observableArrayList();

        double totalRes = Calc.sumup(chain.getResistances());
        String totalResText = "total resistance:  " + totalRes + "Ω";
        if (chain.getVoltIn() != 0 && chain.getAmpere() != 0) {
            double optimalTotalRes = chain.getVoltIn() / chain.getAmpere();
            double ratio = 100 * totalRes / optimalTotalRes;
            comparisons.add("total desired resistance: " + optimalTotalRes + "Ω");
            totalResText += " (" + ratio + "%)";
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
