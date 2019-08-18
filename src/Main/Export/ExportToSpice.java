package Main.Export;

import Main.Logic.CompHyst;
import Main.Logic.ResistanceChain;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/*
 * Project by Vinzent Brömauer
 * vinz.corno@web.de
 *
 * This file is licensed under the GPL license Version 3.
 * See https://www.gnu.org/licenses/ for the full license.
 */
public abstract class ExportToSpice {

    public static void exportResChain(ResistanceChain chain) {
        String fileData = buildResChainString(chain);
        saveFile(fileData, "resistance_chain.asc");
    }

    public static void exportCompHyst(CompHyst circuit) {
        String fileData = buildComparatorString(circuit);
        saveFile(fileData, "comparator_hystheresis.asc");
    }

    private static String buildResChainString(ResistanceChain chain) {
        StringBuilder fileData = new StringBuilder();
        fileData.append("Version 4\n")
                .append("SHEET 1 880 680\n");

        //add all wires
        fileData.append("WIRE 32 -256 32 -272\n" +
                "WIRE 32 -208 32 -256\n" +
                "WIRE 32 -96 32 -128\n" +
                "WIRE -128 -48 -128 -64\n" +
                "WIRE -128 16 -128 -48\n" +
                "WIRE -96 16 -128 16\n" +
                "FLAG 32 -96 0\n" +
                "FLAG 32 -256 U1\n" +
                "FLAG -128 -48 U1\n");

        //wire up resistors
        int pos = 0;
        for (int i = 0; i < chain.getResistances().length; i++) {
            pos = -16 + i * 128;
            fileData.append("WIRE " + (pos + 48) + " 16 " + pos + " 16\n");
        }
        //place ground at the end of the resistors
        fileData.append("FLAG " + (pos + 48) + " 16 0\n");

        //add voltage source
        fileData.append("SYMBOL voltage 32 -224 R0\n")
                .append("WINDOW 123 0 0 Left 0\n")
                .append("WINDOW 39 0 0 Left 0\n")
                .append("SYMATTR InstName V1\n")
                .append("SYMATTR Value ").append(chain.getVoltIn()).append("\n");

        //add all resistors
        for (int i = 0; i < chain.getResistances().length; i++) {
            fileData.append("SYMBOL res ").append(i * 128).append(" 0 R90\n");  //position
            fileData.append("WINDOW 0 0 56 VBottom 2\n");
            fileData.append("WINDOW 3 32 56 VTop 2\n");
            fileData.append("SYMATTR InstName R").append(i + 1).append("\n"); //name
            fileData.append("SYMATTR Value ").append(chain.getResistances()[i]).append("R\n"); //resistance value
        }
        return fileData.toString();
    }

    private static String buildComparatorString(CompHyst compHyst) {
        StringBuilder fileData = new StringBuilder();
        fileData.append("Version 4\n")
                .append("SHEET 1 880 680\n");

        return fileData.toString();
    }

    private static void saveFile(String fileContent, String fileName) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter spiceFilter = new FileChooser.ExtensionFilter("LTSpice Circuit", ".asc");
        fileChooser.getExtensionFilters().add(spiceFilter);
        fileChooser.selectedExtensionFilterProperty().setValue(spiceFilter);
        fileChooser.setInitialFileName(fileName);
        fileChooser.setTitle("Save As");
        File selectedDirectory = fileChooser.showSaveDialog(new Stage());

        if (selectedDirectory == null) {
            return;
        }

        try (FileOutputStream fos = new FileOutputStream(selectedDirectory)) {
            fos.write(fileContent.getBytes());
            fos.flush();
        } catch (IOException e) {

        }
    }
}
