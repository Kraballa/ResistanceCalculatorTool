package Main.UI;

import Main.Logic.CompHyst;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.util.LinkedList;
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
    public void DisplayResistorList(List<CompHyst> resList) {
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

    /**
     * Describes what information the detailed description contains. Mainly serves the purpose of only displaying information
     * that is available.
     *
     * @param chain the chain that stores the information
     */
    protected void displayInfo(CompHyst chain) {
        LinkedList<String> comparisons = new LinkedList<>();


        RightPanel.setText(String.join("\n", comparisons));
    }

    @Override
    public void changed(ObservableValue<? extends CompHyst> observable, CompHyst oldValue, CompHyst newValue) {
        if (newValue != null) {
            displayInfo(newValue);
        }
    }

}
