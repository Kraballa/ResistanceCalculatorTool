package Main.UI;

import Main.Calc;
import Main.Logic.CompHyst;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.util.List;

public class CompHystListPanel implements ChangeListener<CompHyst> {

    private ListView<CompHyst> LeftPanel;
    private TextArea RightPanel;

    private String warningStyle = "-fx-control-inner-background: #ffa1a1;";
    private String suboptimalStyle = "-fx-control-inner-background: #ffe0c7;";
    private String defaultStyle = "";

    public CompHystListPanel(ListView<CompHyst> list, TextArea text) {
        LeftPanel = list;
        RightPanel = text;
    }

    /**
     * Setup a List of ResistorChains as well as their more detailed description in a second list.
     * Also handles ActionListener.
     *
     * @param resList list of resistances to display
     */
    public void displayCompHyst(List<CompHyst> resList) {
        if (!LeftPanel.getSelectionModel().isEmpty()) {
            LeftPanel.getSelectionModel().selectedItemProperty().removeListener(this);
        }

        ObservableList<CompHyst> items = FXCollections.observableArrayList();
        items.addAll(resList);

        LeftPanel.setItems(items);

        LeftPanel.setCellFactory(param -> new ListCell<CompHyst>() {
            @Override
            protected void updateItem(CompHyst item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle(null);
                } else {
                    setText(item.toString());
                    if (item.getDeviation() >= 0.5) {
                        setStyle(warningStyle);
                    } else if (item.getDeviation() >= 0.08) {
                        setStyle(suboptimalStyle);
                    } else {
                        setStyle(defaultStyle);
                    }
                }
            }
        });

        LeftPanel.getSelectionModel().selectedItemProperty().addListener(this);
        LeftPanel.getSelectionModel().selectFirst();
    }

    public CompHyst getSelected() {
        return LeftPanel.getSelectionModel().getSelectedItem();
    }

    /**
     * Describes what information the detailed description contains. Mainly serves the purpose of only displaying information
     * that is available.
     *
     * @param chain the chain that stores the information
     */
    protected void displayInfo(CompHyst chain) {
        StringBuilder comparisons = new StringBuilder();
        double highRatio = Calc.roundWithComma(100 * chain.getuOutH() / chain.getuOutHDesired(), 4);
        double lowRatio = Calc.roundWithComma(100 * chain.getuOutL() / chain.getuOutLDesired(), 4);
        comparisons.append("desired high: " + chain.getuOutHDesired() + " V actual: " + Calc.roundWithComma(chain.getuOutH(), 6) + " V (" + highRatio + "%)\n");
        comparisons.append("desired low: " + chain.getuOutLDesired() + " V actual: " + Calc.roundWithComma(chain.getuOutL(), 6) + " V (" + lowRatio + "%)\n\n");
        comparisons.append("current through voltage divider: " + Calc.roundWithComma(chain.getAmps()[0], 10) + "\n\n");
        comparisons.append("deviation coefficient: " + Calc.roundWithComma(chain.getDeviation(), 10));

        RightPanel.setText(comparisons.toString());
    }

    @Override
    public void changed(ObservableValue<? extends CompHyst> observable, CompHyst oldValue, CompHyst newValue) {
        if (newValue != null) {
            displayInfo(newValue);
        }
    }

}
