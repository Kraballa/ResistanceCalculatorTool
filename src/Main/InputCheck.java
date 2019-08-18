package Main;

import javafx.scene.control.TextField;

import java.util.IllegalFormatException;

/*
 * Project by Vinzent Brömauer
 * vinz.corno@web.de
 *
 * This file is licensed under the GPL license Version 3.
 * See https://www.gnu.org/licenses/ for the full license.
 */
public abstract class InputCheck {

    private static final String errorStyle = "-fx-control-inner-background: #FF4136;";
    private static final String defaultStyle = "";

    public static double parseDoubleGreaterZero(TextField field) {
        try {
            double value = parseDoubleGreaterZero(field.getText().trim());
            field.setStyle(defaultStyle);
            return value;
        } catch (NumberFormatException e) {
            field.setStyle(errorStyle);
            throw e;
        } catch (IllegalArgumentException e) {
            field.setStyle(errorStyle);
            throw e;
        }
    }

    public static double parseDoubleGreaterZero(String value) throws NumberFormatException, IllegalArgumentException {
        value = value.trim();

        if (value.equals("")) {
            throw new IllegalArgumentException("value cannot be empty");
        }

        double multiplier = getMultiplier(value.charAt(value.length() - 1));
        if (multiplier != 1) {
            value = value.substring(0, value.length() - 1);
        }

        double ret = Double.parseDouble(value) * multiplier;
        if (ret <= 0) {
            throw new IllegalArgumentException("value can't be <= zero");
        }
        return ret;
    }

    public static int parseIntGreaterZero(TextField field) {
        try {
            int value = parseIntGreaterZero(field.getText().trim());
            field.setStyle(defaultStyle);
            return value;
        } catch (NumberFormatException e) {
            field.setStyle(errorStyle);
            throw e;
        } catch (IllegalArgumentException e) {
            field.setStyle(errorStyle);
            throw e;
        }
    }

    public static int parseIntGreaterZero(String value) throws NumberFormatException, IllegalFormatException {
        value = value.trim();
        double multiplier = getMultiplier(value.charAt(value.length() - 1));
        if (multiplier != 1) {
            value = value.substring(0, value.length() - 1);
        }

        int ret = (int) Math.round(Double.parseDouble(value) * multiplier);
        if (ret <= 0) {
            throw new IllegalArgumentException("value can't be <= zero");
        }
        return ret;
    }

    public static int parseESeries(String input) {
        int series = Integer.parseInt(input.trim().substring(1));
        switch (series) {
            case 3:
            case 6:
            case 12:
            case 24:
            case 48:
            case 96:
            case 192:
                return series;
            default:
                throw new IllegalArgumentException("input not an e series identifier");
        }
    }

    public static double[] parseDoubleArray(TextField field) {
        return parseDoubleArray(field, 0);
    }

    public static double[] parseDoubleArray(TextField field, int numValues) {
        try {
            double[] value = parseDoubleArray(field.getText().trim(), numValues);
            field.setStyle(defaultStyle);
            return value;
        } catch (NumberFormatException e) {
            field.setStyle(errorStyle);
            throw e;
        } catch (IllegalArgumentException e) {
            field.setStyle(errorStyle);
            throw e;
        }
    }

    public static double[] parseDoubleArray(String string) throws NumberFormatException, IllegalFormatException {
        return parseDoubleArray(string, 0);
    }

    @SuppressWarnings("UnusedAssignment")
    public static double[] parseDoubleArray(String string, int length) throws NumberFormatException, IllegalFormatException {
        string = string.replaceAll(",", ".");
        String[] split = string.trim().split("[ ]+");
        double[] ret = new double[split.length];
        if (length == 0) {
            length = split.length;
        } else if (length != split.length) {
            throw new IllegalArgumentException(length + " values expected, but got " + split.length);
        }
        for (int i = 0; i < split.length; i++) {
            ret[i] = parseDoubleGreaterZero(split[i]);
        }
        return ret;
    }

    private static double getMultiplier(char lastChar) {
        switch (lastChar) {
            case 'M':
                return 1000000;
            case 'k':
                return 1000;
            default:
                return 1;
            case 'm':
                return 0.001;
            case 'μ':
                return 0.000001;

        }
    }
}
