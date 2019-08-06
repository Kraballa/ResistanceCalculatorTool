package Main.Logic;

import Main.Calc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class ResistanceCalculator {

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

                newResistance = Calc.getBestResistance((remainingRes + difference) - ratio * remainingRes, eSeries);
                difference = (remainingRes - ratio * remainingRes) - newResistance;
            } else {
                newResistance = Calc.getBestResistance(remainingRes + difference, eSeries);
            }
            remainingRes -= newResistance;
            chain.addResistance(newResistance, remainingRes + difference);
        }
        return chain;
    }

    public static ArrayList<ResistanceChain> calcChainsFromRatio(double[] range, double ratio, int group) {
        ArrayList<ResistanceChain> list = new ArrayList<>();
        if (range.length == 1) {
            list.add(calcChainFromRatio(ratio, range[0], group));
        } else if (range.length == 2) {
            for (double i = range[0]; i <= range[1]; i += ((range[1] - range[0]) / 1000)) {
                ResistanceChain chain = calcChainFromRatio(ratio, i, group);
                if (!list.contains(chain)) {
                    list.add(chain);
                }
            }
        }
        //sorting doesn't work, there's no basis for comparison
        return new ArrayList<>(list.subList(0, Math.min(30, list.size())));
    }

    private static ResistanceChain calcChainFromRatio(double ratio, double totalRes, int group) {
        ResistanceChain chain = new ResistanceChain();
        double targetRes = (ratio * totalRes / (ratio + 1));
        double actualRes = Calc.getBestResistance(targetRes, group);
        chain.addResistance(actualRes, targetRes);
        targetRes = totalRes - actualRes;
        actualRes = Calc.getBestResistance(targetRes, group);
        chain.addResistance(actualRes, targetRes);
        return chain;
    }
}
