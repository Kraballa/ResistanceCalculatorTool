package Main;

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
        return parseInputString(voltIn, 1)[0];
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
        return parseInputString(voltOut, 0);
    }

    public static boolean checkAmpere(double[] ampere) {
        for (double anAmpere : ampere) {
            if (anAmpere < 0) {
                System.out.println("ampere cannot have values < 0");
                return false;
            }
        }
        return true;
    }

    public static double[] parseAmpere(String ampere) {
        return parseInputString(ampere, 2);
    }

    public static double[] parseInputString(String string, int length) {
        int actualLength = length;
        String[] split = string.trim().split("[ ]+");
        double ret[] = new double[split.length];
        if (length <= 0) {
            actualLength = ret.length;
        }
        for (int i = 0; i < Math.min(ret.length, actualLength); i++) {
            ret[i] = Double.parseDouble(split[i].replaceAll(",", "."));
        }
        return ret;
    }
}
