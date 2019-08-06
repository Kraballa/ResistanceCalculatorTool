package Main;

/*
 * Project by Vinzent Brömauer
 * vinz.corno@web.de
 *
 * This file is licensed under the GPL license Version 3.
 * See https://www.gnu.org/licenses/ for the full license.
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class Calc {

    private static double[] e3 = new double[]{1, 2.2, 4.7};
    private static double[] e6 = new double[]{1, 1.5, 2.2, 3.3, 4.7, 6.8};
    private static double[] e12 = new double[]{1, 1.2, 1.5, 1.8, 2.2, 2.7, 3.3, 3.9, 4.7, 5.6, 6.8, 8.2};
    private static double[] e24 = new double[]{1, 1.1, 1.2, 1.3, 1.5, 1.6, 1.8, 2, 2.2, 2.4, 2.7, 3, 3.3, 3.6, 3.9, 4.3, 4.7, 5.1, 5.6, 6.2, 6.8, 7.5, 8.2, 9.1};
    private static double[] e48 = new double[]{1, 1.05, 1.1, 1.15, 1.21, 1.27, 1.33, 1.4, 1.47, 1.54, 1.62, 1.69, 1.78, 1.87, 1.96, 2.05, 2.15, 2.26, 2.37, 2.49, 2.61, 2.74, 2.87, 3.01, 3.16, 3.32, 3.48, 3.65, 3.83, 4.02, 4.22, 4.42, 4.64, 4.87, 5.11, 5.36, 5.62, 5.9, 6.19, 6.49, 6.81, 7.15, 7.5, 7.87, 8.25, 8.66, 9.09, 9.53};
    private static double[] e96 = new double[]{1, 1.02, 1.05, 1.07, 1.1, 1.13, 1.15, 1.18, 1.21, 1.24, 1.27, 1.3, 1.33, 1.37, 1.4, 1.43, 1.47, 1.5, 1.54, 1.58, 1.62, 1.65, 1.69, 1.74, 1.78, 1.82, 1.87, 1.91, 1.96, 2, 2.05, 2.1, 2.15, 2.21, 2.26, 2.32, 2.37, 2.43, 2.49, 2.55, 2.61, 2.67, 2.74, 2.8, 2.87, 2.94, 3.01, 3.09, 3.16, 3.24, 3.32, 3.4, 3.48, 3.57, 3.65, 3.74, 3.83, 3.92, 4.02, 4.12, 4.22, 4.32, 4.42, 4.53, 4.64, 4.75, 4.87, 4.99, 5.11, 5.23, 5.36, 5.49, 5.62, 5.76, 5.9, 6.04, 6.19, 6.34, 6.49, 6.65, 6.81, 6.98, 7.15, 7.32, 7.5, 7.68, 7.87, 8.06, 8.25, 8.45, 8.66, 8.87, 9.09, 9.31, 9.53, 9.76};
    private static double[] e192 = new double[]{1, 1.01, 1.02, 1.04, 1.05, 1.06, 1.07, 1.09, 1.1, 1.11, 1.13, 1.14, 1.15, 1.17, 1.18, 1.2, 1.21, 1.23, 1.24, 1.26, 1.27, 1.29, 1.3, 1.32, 1.33, 1.35, 1.37, 1.38, 1.4, 1.42, 1.43, 1.45, 1.47, 1.49, 1.5, 1.52, 1.54, 1.56, 1.58, 1.6, 1.62, 1.64, 1.65, 1.67, 1.69, 1.72, 1.74, 1.76, 1.78, 1.8, 1.82, 1.84, 1.87, 1.89, 1.91, 1.93, 1.96, 1.98, 2, 2.03, 2.05, 2.08, 2.1, 2.13, 2.15, 2.18, 2.21, 2.23, 2.26, 2.29, 2.32, 2.34, 2.37, 2.4, 2.43, 2.46, 2.49, 2.52, 2.55, 2.58, 2.61, 2.64, 2.67, 2.71, 2.74, 2.77, 2.8, 2.84, 2.87, 2.91, 2.94, 2.98, 3.01, 3.05, 3.09, 3.12, 3.16, 3.2, 3.24, 3.28, 3.32, 3.36, 3.4, 3.44, 3.48, 3.52, 3.57, 3.61, 3.65, 3.7, 3.74, 3.79, 3.83, 3.88, 3.92, 3.97, 4.02, 4.07, 4.12, 4.17, 4.22, 4.27, 4.32, 4.37, 4.42, 4.48, 453, 4.59, 4.64, 4.7, 4.75, 4.81, 4.87, 4.93, 4.99, 5.05, 5.11, 5.17, 5.23, 5.3, 5.36, 5.42, 5.49, 5.56, 5.62, 5.69, 5.76, 5.83, 5.9, 5.97, 6.04, 6.12, 6.19, 6.26, 6.34, 6.42, 6.49, 6.57, 6.65, 6.73, 6.81, 6.9, 6.98, 7.06, 7.15, 7.23, 7.32, 7.41, 7.5, 7.59, 7.68, 7.77, 7.87, 7.96, 8.06, 8.16, 8.25, 8.35, 8.45, 8.56, 8.66, 8.76, 8.87, 8.98, 9.09, 9.2, 9.31, 9.42, 9.53, 9.65, 9.76, 9.88};

    public static double[] sortDescending(double[] array){
        for(int i =0; i<array.length; i++) {
            for(int j =0; j<array.length; j++) {
                if(array[i] > array[j]) {
                    double temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                }
            }

        }
        return array;
    }

    private static double sumupSegment(double[] array, int start, int end) {
        double ret = 0.0;
        for(int i = start; i <= end; i++){
            ret += array[i];
        }
        return ret;
    }

    public static double sumupFrom(double[] array, int start){
        return sumupSegment(array,start, array.length-1);
    }

    public static double sumupTo(double[] array, int end){
        return sumupSegment(array, 0, end);
    }

    public static double sumup(double[] array){
        return sumupSegment(array, 0, array.length-1);
    }

    public static double roundWithComma(double number, int commavalues){
        double ret = number;
        double factor = Math.max(1,Math.pow(10,commavalues));
        ret = ((int)Math.round((factor)*ret))/factor;
        return ret;
    }

    /**
     * Calculates the optimal resistor. U->[R1]-|-[R2]-|
     *                                        Uout
     * @param R1    resistor following the input voltage
     * @param U     the input Voltage
     * @param Uout  the desired output voltage
     * @return      returns the resistance value of R2, the resistor following Uout
     */
    public static double getOptimalResistor(double R1, double U, double Uout){
        double R2;
        R2 = (Uout*R1)/(U-Uout);
        return R2;
    }

    /**
     * From given values, calculates the true voltage output. This is used to check the real values after the resistors
     * have been adjusted to
     * @param R1    sum of all resistances before the output
     * @param R2    sum of all resistances after the output
     * @param U     the input voltage
     * @return      true voltage output as calculated by a formula
     */
    public static double getAbsoluteOutput(double R1, double R2, double U){
        double Uout;
        Uout = (U*R2)/(R2+R1);
        return Uout;
    }

    /**
     * From a given optimum resistance, calculates the closest value from a pool of values. This is used to ensure the resistances
     * exist in real life.
     * @param optimum   the desired resistance
     * @param group     the e-row of valid resistances
     * @return          the nearest existing resistance
     */
    public static double getBestResistance(double optimum, int group){
        //check if input group is valid
        int[] allowed = {3,6,12,24,48,96,192};
        boolean valid = false;
        for (int anAllowed : allowed) {
            if (anAllowed == group) {
                valid = true;
                break;
            }
        }
        if(!valid){
            return 0D;
        }

        //calculate the decade and limit the resistance to [1.0,100000)
        int decade = 1;
        while(decade < 10000 && optimum / decade >= 10){
            decade *= 10;
        }
        double[] row;
        switch(group){
            case 6: row = e6; break;
            case 12: row = e12; break;
            case 24: row = e24; break;
            case 48: row = e48; break;
            case 96: row = e96; break;
            case 192: row = e192; break;
            default: row = e3;
        }
        double best = row[0]*decade;
        for (double aRow : row) {
            if (Math.abs(optimum - best) > Math.abs(optimum - aRow*decade)) {
                best = aRow*decade;
            }
        }
        if (Math.abs(optimum - best) > Math.abs(optimum - row[0]*10) && decade < 1000) {
            best = row[0]*10;
        }
        return best;
    }


    @Deprecated
    public static ResistorChain getChainFromRatio(double ratio, double totalRes, int group){
        ResistorChain chain = new ResistorChain();
        double tempRes = (ratio*totalRes/(ratio+1));
        double actualRes = getBestResistance(tempRes,group);
        chain = chain.add(actualRes);
        tempRes = totalRes - actualRes;
        chain = chain.add(getBestResistance(tempRes,group));
        chain.calcResistanceDeviation(new double[]{(ratio*totalRes/(ratio+1)),(totalRes/(ratio+1))});
        return chain;
    }

    @Deprecated
    public static ObservableList<ResistorChain> removeDuplicates(ObservableList<ResistorChain> chainList) {
        ObservableList<ResistorChain> uniqueList = FXCollections.observableArrayList();

        uniqueList.add(chainList.get(0));
        if (chainList.size() > 1) {
            for (int i = 1; i < chainList.size(); i++) {
                if (!uniqueList.contains(chainList.get(i))) {
                    uniqueList.add(chainList.get(i));
                }
            }
        }

        return uniqueList;
    }
}
