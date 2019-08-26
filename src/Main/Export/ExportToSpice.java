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

        fileData.append("WIRE 240 -160 112 -160\n")
                .append("WIRE 384 -160 320 -160\n")
                .append("WIRE -528 -144 -528 -176\n")
                .append("WIRE -416 -144 -416 -176\n")
                .append("WIRE -304 -144 -304 -176\n")
                .append("WIRE 0 -144 0 -176\n")
                .append("WIRE -304 -112 -304 -144\n")
                .append("WIRE 0 -96 0 -144\n")
                .append("WIRE -528 -32 -528 -144\n")
                .append("WIRE -416 -32 -416 -144\n")
                .append("WIRE 160 -16 144 -16\n")
                .append("WIRE 192 -16 160 -16\n")
                .append("WIRE 384 0 384 -160\n")
                .append("WIRE 384 0 256 0\n")
                .append("WIRE 448 0 384 0\n")
                .append("WIRE 0 16 0 -16\n")
                .append("WIRE 112 16 112 -160\n")
                .append("WIRE 112 16 0 16\n")
                .append("WIRE 192 16 112 16\n")
                .append("WIRE 0 48 0 16\n")
                .append("WIRE -528 144 -528 48\n")
                .append("WIRE -416 144 -416 48\n")
                .append("WIRE -304 144 -304 48\n")
                .append("WIRE 0 192 0 128\n")
                .append("FLAG -304 144 0\n")
                .append("FLAG -304 -144 input\n")
                .append("FLAG 224 -32 U2high\n")
                .append("FLAG 0 192 0\n")
                .append("FLAG 224 32 0\n")
                .append("FLAG 0 -144 U1\n")
                .append("FLAG -416 144 0\n")
                .append("FLAG -416 -144 U1\n")
                .append("FLAG 160 -16 input\n")
                .append("FLAG -528 144 0\n")
                .append("FLAG -528 -144 U2high\n")
                .append("SYMBOL res -16 -112 R0\n")
                .append("SYMATTR InstName R1\n")
                .append("SYMATTR Value " + compHyst.getResistances()[0] + "\n")
                .append("SYMBOL res -16 32 R0\n")
                .append("SYMATTR InstName R2\n")
                .append("SYMATTR Value " + compHyst.getResistances()[1] + "\n")
                .append("SYMBOL res 336 -176 R90\n")
                .append("WINDOW 0 0 56 VBottom 2\n")
                .append("WINDOW 3 32 56 VTop 2\n")
                .append("SYMATTR InstName R3\n")
                .append("SYMATTR Value " + compHyst.getResistances()[2] + "\n")
                .append("SYMBOL voltage -304 -48 R0\n")
                .append("WINDOW 123 0 0 Left 0\n")
                .append("WINDOW 39 0 0 Left 0\n")
                .append("SYMATTR InstName V0\n")
                .append("SYMATTR Value PWL(0 0 100ms 10)\n")
                .append("SYMBOL Comparators\\\\LTC6752 224 -64 R0\n")
                .append("SYMATTR InstName U1\n")
                .append("SYMBOL voltage -416 -48 R0\n")
                .append("WINDOW 123 0 0 Left 0\n")
                .append("WINDOW 39 0 0 Left 0\n")
                .append("SYMATTR InstName V1\n")
                .append("SYMATTR Value " + compHyst.getU1() + "\n")
                .append("SYMBOL res -320 -128 R0\n")
                .append("SYMATTR InstName R6\n")
                .append("SYMATTR Value 100\n")
                .append("SYMBOL voltage -528 -48 R0\n")
                .append("WINDOW 123 0 0 Left 0\n")
                .append("WINDOW 39 0 0 Left 0\n")
                .append("SYMATTR InstName V2\n")
                .append("SYMATTR Value " + compHyst.getU2high() + "\n")
                .append("TEXT -496 -248 Left 2 !.tran 100ms\n");

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
