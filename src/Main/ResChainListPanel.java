package Main;

import Main.Logic.ResistanceChain;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.paint.Color;

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
    private Canvas Canvas;

    private String warningStyle = "-fx-control-inner-background: #ffa1a1;";
    private String suboptimalStyle = "-fx-control-inner-background: #ffe0c7;";
    private String defaultStyle = "";

    public ResChainListPanel(ListView<ResistanceChain> list, ListView<String> text) {
        this(list, text, null);
    }

    public ResChainListPanel(ListView<ResistanceChain> list, ListView<String> text, Canvas canvas) {
        LeftPanel = list;
        RightPanel = text;
        Canvas = canvas;
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

        double totalRes = Calc.roundWithComma(Calc.sumup(chain.getResistances()), 2);
        String totalResText = "total resistance:  " + totalRes + " Ω";
        if (chain.getVoltIn() != 0 && chain.getAmpere() != 0) {
            double optimalTotalRes = chain.getVoltIn() / chain.getAmpere();
            double ratio = Calc.roundWithComma(100 * totalRes / optimalTotalRes, 4);
            optimalTotalRes = Calc.roundWithComma(optimalTotalRes, 2);
            comparisons.add("total desired resistance: " + optimalTotalRes + " Ω");
            totalResText += " (" + ratio + "%)";
        }
        comparisons.add(totalResText);

        if (chain.getAmpere() != 0) {
            comparisons.add("optimal ampere: " + Calc.roundWithComma(chain.getAmpere(), 10) + " A");
            comparisons.add("");

            for (int i = 0; i < chain.getOptimalOutputs().length; i++) {
                double optOutput = Calc.roundWithComma(chain.getOptimalOutputs()[i], 4);
                double actOutput = Calc.roundWithComma(chain.getOutputs()[i], 4);
                double ratio = Calc.roundWithComma(100 * optOutput / actOutput, 2);
                comparisons.add("desired: " + optOutput +
                        " V, actual: " + actOutput
                        + " V (" + ratio + "%)");
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

            comparisons.add("deviation coefficient: " + Calc.roundWithComma(chain.getDeviation(), 6));
        }

        RightPanel.setItems(comparisons);
    }

    /**
     * Draws the input voltage (if it exists), the resistances and the outputs (if they exist) onto a canvas.
     *
     * @param chain the chain that should be drawn
     */
    private void drawChainCanvas(ResistanceChain chain) {
        GraphicsContext gc = Canvas.getGraphicsContext2D();
        gc.setFill(Color.grayRgb(244));
        gc.fillRect(0, 0, Canvas.getWidth(), Canvas.getHeight());
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

        int[] pos = new int[]{25, (int) Canvas.getHeight() / 2};
        if (chain.getVoltIn() != 0) {
            gc.setLineWidth(1);
            gc.strokeText(Calc.roundWithComma(chain.getVoltIn(), 2) + " V", pos[0] + 5, pos[1] - 10);

            gc.setLineWidth(2);
            lineOffset(gc, pos, 20);
        }

        double[] outputs = chain.getOutputs();
        for (int i = 0; i < chain.getResistances().length; i++) {
            drawResistor(gc, (int) chain.getResistances()[i], pos);
            gc.setLineWidth(1);
            if (i < chain.getResistances().length - 1) {
                gc.strokeText(Calc.roundWithComma(outputs[i], 2) + " V", pos[0] - 13, pos[1] - 10);
            }
            gc.setLineWidth(2);
        }
    }

    private int[] drawResistor(GraphicsContext gc, int value, int[] pos) {
        lineOffset(gc, pos, 20);
        gc.strokeRect(pos[0], pos[1] - 8, 60, 16);
        gc.setLineWidth(1);
        gc.strokeText(value + " Ω", pos[0] + 8, pos[1] + 27);
        gc.setLineWidth(2);
        pos[0] += 60;
        lineOffset(gc, pos, 20);
        return pos;
    }

    private int[] lineOffset(GraphicsContext gc, int[] pos, int length) {
        gc.strokeLine(pos[0], pos[1], pos[0] + length, pos[1]);
        pos[0] += length;
        return pos;
    }

    @Override
    public void changed(ObservableValue<? extends ResistanceChain> observable, ResistanceChain oldValue, ResistanceChain newValue) {
        if (newValue != null) {
            displayInfo(newValue);
            if (Canvas != null) {
                drawChainCanvas(newValue);
            }
        }
    }
}
