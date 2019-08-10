package Main.Logic;

import Main.Calc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/*
 * Project by Vinzent Brömauer
 * vinz.corno@web.de
 *
 * This file is licensed under the GPL license Version 3.
 * See https://www.gnu.org/licenses/ for the full license.
 */
public abstract class ResistanceCalculator {

    /**
     * Calculate 1000 resistance chains within a given range of ampere
     *
     * @param voltIn   input voltage
     * @param voltOut  desired output voltages between each consecutive resistor
     * @param currents range of currents
     * @param eSeries  the number  of the eSeries that is to be used
     * @return a sorted List of 30 ResistanceChains that feature the lowest deviation, marking them the best out of the 1000 calculated
     */
    public static List<ResistanceChain> calcResistanceChains(double voltIn, double[] voltOut, double[] currents, int eSeries) {
        List<ResistanceChain> chains = new LinkedList<>();
        for (double current = currents[0]; current <= currents[1]; current += ((currents[1] - currents[0]) / 1000)) {
            ResistanceChain chain = calcResistanceChain(voltIn, voltOut, current, eSeries);
            if (!chains.contains(chain)) {
                chains.add(chain);
            }
        }
        Collections.sort(chains);
        return new ArrayList<>(chains.subList(0, Math.min(30, chains.size())));
    }

    /**
     * Calculate a ResistanceChain using a homemade algorithm
     *
     * @param voltIn  input voltage
     * @param voltOut desired output voltages between each consecutive resistor
     * @param ampere  the current that is being used
     * @param eSeries the number  of the eSeries that is to be used
     * @return the closest possible ResistanceChain that features the desired outputs
     */
    public static ResistanceChain calcResistanceChain(double voltIn, double[] voltOut, double ampere, int eSeries) {
        ResistanceChain chain = new ResistanceChain();
        chain.setVoltIn(voltIn);
        chain.setAmpere(ampere);
        chain.setOptimalOutputs(voltOut);
        double remainingRes = voltIn / ampere;
        double difference = 0;
        double newResistance;

        for (int i = 0; i < voltOut.length + 1; i++) {
            if (i < voltOut.length) {
                //ratio between the desired output and remaining input voltage
                double ratio;
                if (i == 0) {
                    ratio = voltOut[i] / voltIn;
                } else {
                    ratio = voltOut[i] / voltOut[i - 1];
                }

                newResistance = Calc.getBestResistance((remainingRes + difference) - ratio * (remainingRes + difference), eSeries);
                difference = (remainingRes - ratio * remainingRes) - newResistance;
            } else {
                newResistance = Calc.getBestResistance(remainingRes + difference, eSeries);
            }
            remainingRes -= newResistance;
            chain.addResistance(newResistance, remainingRes + difference);
        }
        return chain;
    }

    /**
     * Calculates 1000 ResistanceChains based on a ratio and a range of total resistances
     *
     * @param ratio    the ratio between the first and second (which is also the last) resistor
     * @param resRange range of total resistances
     * @param eSeries  the eSeries that is being used
     * @return a sorted list of the 30 best (out of 1000) ResistanceChains as calculated using the calcChainFromRatio method
     */
    public static ArrayList<ResistanceChain> calcChainsFromRatio(double ratio, double[] resRange, int eSeries) {
        ArrayList<ResistanceChain> list = new ArrayList<>();
        if (resRange.length == 1) {
            list.add(calcChainFromRatio(ratio, resRange[0], eSeries));
        } else if (resRange.length == 2) {
            for (double i = resRange[0]; i <= resRange[1]; i += ((resRange[1] - resRange[0]) / 1000)) {
                ResistanceChain chain = calcChainFromRatio(ratio, i, eSeries);
                if (!list.contains(chain)) {
                    chain.setRatio(ratio);
                    list.add(chain);
                }
            }
        }
        Collections.sort(list);
        return new ArrayList<>(list.subList(0, Math.min(30, list.size())));
    }

    /**
     * Calculates a ResistanceChain of length 2 based on a given ratio and a desired total resistance
     *
     * @param ratio    the ratio between the first and second (which is also the last) resistor
     * @param totalRes the total resistance of the chain
     * @param eSeries  the eSeries that is being used
     * @return the ResistanceChain with the closest ratio and total resistance to the desired values
     */
    private static ResistanceChain calcChainFromRatio(double ratio, double totalRes, int eSeries) {
        ResistanceChain chain = new ResistanceChain();
        double targetRes = (ratio * totalRes / (ratio + 1));
        double actualRes = Calc.getBestResistance(targetRes, eSeries);
        chain.addResistance(actualRes, targetRes);
        targetRes = totalRes - actualRes;
        actualRes = Calc.getBestResistance(targetRes, eSeries);
        chain.addResistance(actualRes, targetRes);
        return chain;
    }
}
