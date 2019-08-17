package Main.Controller;

import Main.InputCheck;
import Main.Logic.CompHyst;
import Main.Logic.CompHystCalculator;
import Main.UI.CompHystListPanel;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

/*
 * Project by Vinzent Brömauer
 * vinz.corno@web.de
 *
 * This file is licensed under the GPL license Version 3.
 * See https://www.gnu.org/licenses/ for the full license.
 */
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
        eSeries.setValue("e96");

        eSeries.setTooltip(new Tooltip("e series to look in for available resistors"));
        ampere.setTooltip(new Tooltip("ampere value that is used to calculate R1 and R2 with"));
        u1.setTooltip(new Tooltip("input voltage"));
        uOutH.setTooltip(new Tooltip("upper threshold of the hystheresis"));
        uOutL.setTooltip(new Tooltip("lower threshold of the hystheresis"));
        u2high.setTooltip(new Tooltip("high value of the second input voltage"));
        amount.setTooltip(new Tooltip("the amount of resistors to calculate with. choosing values over 100 might crash java since the value is being cubed"));

        compHystListPanel = new CompHystListPanel(compList, detailArea);
    }

    public void OnCalculateCompHyst() {
        double u1V = 0;
        double u2highV = 0;
        double uOutHV = 0;
        double uOutLV = 0;
        double ampereV = 0;
        int eSeries = 0;
        int amountV = 0;

        try {
            eSeries = InputCheck.parseESeries(this.eSeries.getValue());
            ampereV = InputCheck.parseDoubleGreaterZero(ampere);
            u1V = InputCheck.parseDoubleGreaterZero(u1);
            uOutHV = InputCheck.parseDoubleGreaterZero(uOutH);
            uOutLV = InputCheck.parseDoubleGreaterZero(uOutL);
            u2highV = InputCheck.parseDoubleGreaterZero(u2high);
            amountV = InputCheck.parseIntGreaterZero(amount);
        } catch (NumberFormatException e) {
            System.out.println("one or more inputs are not numbers");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println("one or more inputs doesn't match the expected values");
            e.printStackTrace();
        }
        List<CompHyst> compHysts = CompHystCalculator.calcCompHyst(u1V, u2highV, uOutHV, uOutLV, ampereV, eSeries, amountV);
        compHystListPanel.displayCompHyst(compHysts);
    }
}
