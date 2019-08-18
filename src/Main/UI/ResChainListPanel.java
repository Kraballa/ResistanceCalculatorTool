package Main.UI;

import Main.Calc;
import Main.Export.ExportToSpice;
import Main.Logic.ResistanceChain;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.util.List;

/*
 * Project by Vinzent Brömauer
 * vinz.corno@web.de
 *
 * This file is licensed under the GPL license Version 3.
 * See https://www.gnu.org/licenses/ for the full license.
 */
public class ResChainListPanel implements ChangeListener<ResistanceChain> {

    private ListView<ResistanceChain> LeftPanel;
    private TextArea RightPanel;
    private Canvas Canvas;

    private String warningStyle = "-fx-control-inner-background: #ffa1a1;";
    private String suboptimalStyle = "-fx-control-inner-background: #ffe0c7;";
    private String defaultStyle = "";

    public ResChainListPanel(ListView<ResistanceChain> list, TextArea text) {
        this(list, text, null);
    }

    public ResChainListPanel(ListView<ResistanceChain> list, TextArea text, Canvas canvas) {
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

                MenuItem exportItem = new MenuItem("Export");
                exportItem.setOnAction(event -> ExportToSpice.exportResChain(item));
                ContextMenu contextMenu = new ContextMenu(exportItem);
                LeftPanel.setContextMenu(contextMenu);

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
    protected void displayInfo(ResistanceChain chain) {
        StringBuilder comparisons = new StringBuilder();

        double totalRes = Calc.roundWithComma(Calc.sumup(chain.getResistances()), 2);
        String totalResText = "total resistance:  " + totalRes + " Ω";
        if (chain.getVoltIn() != 0 && chain.getAmpere() != 0) {
            double optimalTotalRes = chain.getVoltIn() / chain.getAmpere();
            double ratio = Calc.roundWithComma(100 * totalRes / optimalTotalRes, 4);
            optimalTotalRes = Calc.roundWithComma(optimalTotalRes, 2);
            comparisons.append("total desired resistance: " + optimalTotalRes + " Ω\n");
            totalResText += " (" + ratio + "%)";
        }
        totalResText += "\n";
        comparisons.append(totalResText);

        if (chain.getAmpere() != 0 && chain.getVoltIn() != 0) {
            comparisons.append("optimal ampere: " + Calc.roundWithComma(chain.getAmpere(), 10) + " A\n\n");

            for (int i = 0; i < chain.getOptimalOutputs().length; i++) {
                double optOutput = Calc.roundWithComma(chain.getOptimalOutputs()[i], 4);
                double actOutput = Calc.roundWithComma(chain.getOutputs()[i], 4);
                double ratio = Calc.roundWithComma(100 * optOutput / actOutput, 2);
                comparisons.append("desired: " + optOutput +
                        " V, actual: " + actOutput
                        + " V (" + ratio + "%)\n");
            }
        } else {
            double[] resistances = chain.getResistances();
            double ratio = resistances[0] / resistances[1] / chain.getRatio();
            String ratioText = "actual ratio: " + Calc.roundWithComma(ratio * 100, 2) + "%\n";
            comparisons.append(ratioText);
        }
        comparisons.append("\n");
        if (chain.getDeviation() >= 0.5) {
            comparisons.append("Warning! extreme deviation\n");
            RightPanel.setStyle(warningStyle);
        } else if (chain.getDeviation() >= 0.08) {
            comparisons.append("suboptimal deviation\n");
            RightPanel.setStyle(suboptimalStyle);
        } else {
            RightPanel.setStyle(defaultStyle);
        }

        comparisons.append("deviation coefficient: " + Calc.roundWithComma(chain.getDeviation(), 6) + "\n");

        RightPanel.setText(comparisons.toString());
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
        gc.strokeLine(pos[0], pos[1], pos[0], pos[1] - 15);
        gc.strokeLine(pos[0], pos[1] - 15, pos[0] - 6, pos[1] - 9);
        gc.strokeLine(pos[0], pos[1] - 15, pos[0] + 6, pos[1] - 9);
        if (chain.getVoltIn() != 0) {
            gc.setLineWidth(1);
            gc.strokeText(Calc.roundWithComma(chain.getVoltIn(), 2) + " V", pos[0] - 8, pos[1] - 20);
            gc.setLineWidth(2);
        }

        if (chain.getAmpere() != 0) {
            gc.setLineWidth(1);
            gc.strokeText(Calc.roundWithComma(chain.getAmpere(), 10) + " A", pos[0] + 64, pos[1] - 32);
            gc.setLineWidth(2);
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
        gc.strokeLine(pos[0], pos[1], pos[0], pos[1] + 15);
        gc.strokeLine(pos[0] - 5, pos[1] + 15, pos[0] + 5, pos[1] + 15);
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
