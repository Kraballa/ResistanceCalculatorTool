package Main.Controller;

import Main.InputCheck;
import Main.Logic.CompHyst;
import Main.Logic.CompHystCalculator;
import Main.UI.CompHystListPanel;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.List;

public class ComparatorHystheresisController {

    private CompHystListPanel compHystListPanel;

    @FXML
    ListView<CompHyst> compList;
    @FXML
    TextArea detailArea;
    @FXML
    ChoiceBox<String> eSeries;

    @FXML
    TextField ampere;
    @FXML
    TextField u1;
    @FXML
    TextField uOutH;
    @FXML
    TextField uOutL;
    @FXML
    TextField u2high;
    @FXML
    TextField amount;

    @FXML
    private void initialize() {
        compHystListPanel = new CompHystListPanel(compList, detailArea);
        eSeries.setValue("e96");
    }

    public void OnCalculateCompHyst() {
        try {
            int series = InputCheck.parseESeries(eSeries.getValue());
            double ampereV = InputCheck.parseAmpere(ampere.getText())[0];
            double u1V = InputCheck.parseVoltIn(u1.getText());
            double uOutHV = InputCheck.parseVoltIn(uOutH.getText());
            double uOutLV = InputCheck.parseVoltIn(uOutL.getText());
            double u2highV = InputCheck.parseVoltIn(u2high.getText());
            int amountV = Integer.parseInt(amount.getText());

            System.out.println(series + " " + ampereV + " " + u1V + " " + uOutHV + " " + uOutLV + " " + u2highV + " " + amountV);

            List<CompHyst> compHysts = CompHystCalculator.calcCompHyst(u1V, u2highV, uOutHV, uOutLV, ampereV, series, amountV);
            compHystListPanel.displayCompHyst(compHysts);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
    }
}
