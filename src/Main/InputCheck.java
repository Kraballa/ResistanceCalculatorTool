package Main;

/*
 * Project by Vinzent Brömauer
 * vinz.corno@web.de
 *
 * This file is licensed under the GPL license Version 3.
 * See https://www.gnu.org/licenses/ for the full license.
 */
public abstract class InputCheck {

    public static boolean checkVoltIn(double voltIn, double[] voltOut) {
        if (voltIn <= 0) {
            System.out.println("voltIn <= 0");
            return false;
        } else {
            for (double aVoltOut : voltOut) {
                if (voltIn < aVoltOut) {
                    System.out.println("voltIn (" + voltIn + ") is not bigger than atleast one voltOut(" + aVoltOut + ")");
                    return false;
                }
            }
        }
        return true;
    }

    public static double parseVoltIn(String voltIn) {
        return parseDoubleArray(voltIn, 1)[0];
    }

    public static boolean checkVoltOut(double[] voltOut) {
        for (double aVoltOut : voltOut) {
            if (aVoltOut < 0) {
                System.out.println("voltOut cannot have values < 0");
                return false;
            }
        }
        return true;
    }

    public static double[] parseVoltOut(String voltOut) {
        return parseDoubleArray(voltOut, 0);
    }

    public static boolean checkAmpere(double[] ampere) {
        for (double anAmpere : ampere) {
            if (anAmpere <= 0) {
                System.out.println("ampere cannot have values < 0");
                return false;
            }
        }
        return true;
    }

    public static double[] parseAmpere(String ampere) {
        return parseDoubleArray(ampere, 2);
    }

    public static int parseESeries(String input) {
        return Integer.parseInt(input.replaceAll("[^0123456789]", ""));
    }

    public static double[] parseDoubleArray(String string, int length) {
        string = string.replaceAll("[^0123456789.,]", " ");
        int actualLength = length;
        String[] split = string.trim().split("[ ]+");
        double[] ret = new double[split.length];
        if (length <= 0) {
            actualLength = ret.length;
        }
        for (int i = 0; i < Math.min(ret.length, actualLength); i++) {
            ret[i] = Double.parseDouble(split[i].replaceAll(",", "."));
        }
        return ret;
    }
}
