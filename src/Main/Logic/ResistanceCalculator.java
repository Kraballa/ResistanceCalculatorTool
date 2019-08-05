package Main.Logic;

import Main.Calc;

import java.util.LinkedList;
import java.util.List;

public abstract class ResistanceCalculator {

    public static List<ResistanceChain> calculateResistanceChains(double voltIn, double[] voltOut, double[] currents, int eSeries) {
        List<ResistanceChain> chains = new LinkedList<>();
        for (double current = currents[0]; current <= currents[1]; current += ((currents[1] - currents[0]) / 1000)) {
            ResistanceChain chain = calculateResistanceChain(voltIn, voltOut, current, eSeries);
            if (!chains.contains(chain)) {
                chains.add(chain);
            }
        }
        return chains;
    }

    public static ResistanceChain calculateResistanceChain(double voltIn, double[] voltOut, double current, int eSeries) {
        ResistanceChain chain = new ResistanceChain(voltOut);
        double remainingRes = voltIn / current;
        double difference = 0;
        double newResistor;

        for (int i = 0; i < voltOut.length + 1; i++) {
            if (i < voltOut.length) {
                //ratio = ratio between the desired output and remaining input voltage
                double ratio;
                if (i == 0) {
                    ratio = voltOut[i] / voltIn;
                } else {
                    ratio = voltOut[i] / voltOut[i - 1];
                }

                newResistor = Calc.getBestResistance((remainingRes + difference) - ratio * remainingRes, eSeries);
                difference = (remainingRes - ratio * remainingRes) - newResistor;
            } else {
                newResistor = Calc.getBestResistance(remainingRes + difference, eSeries);
            }
            remainingRes -= newResistor;
            chain.addResistance(newResistor, remainingRes + difference);
        }
        return chain;
    }
}