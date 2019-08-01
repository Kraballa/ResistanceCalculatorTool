package Main;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import java.util.List;

public class ResChainListPanel extends SplitPane implements ChangeListener<ResistorChain> {

    ListView<ResistorChain> LeftPanel;
    TextArea RightPanel;

    @FXML
    protected void initialize() {
        this.getChildren().add(new AnchorPane(LeftPanel = new ListView<ResistorChain>()));
        this.getChildren().add(new AnchorPane(RightPanel = new TextArea()));
    }

    /**
     * Setup a List of ResistorChains as well as their more detailed description in a textarea.
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

    private void DisplayInfo(ResistorChain chain) {
        String text = "total resistance: \n";
        for (int i = 0; i < chain.resistances.length; i++) {
            text += "resistance: " + chain.resistances[i] + "\n";
        }
        RightPanel.setText(text);
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
        DisplayInfo(newValue);
    }
}
