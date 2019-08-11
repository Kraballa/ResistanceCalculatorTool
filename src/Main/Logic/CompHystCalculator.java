package Main.Logic;

import Main.Calc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompHystCalculator {

    public static List<CompHyst> calcCompHyst(double u1, double u2high, double uOutH, double uOutL, double ampere, int eSeries) {
        //STEP 1: Calculate all relevant values
        //Step 1.1
        double windowSize = uOutH - uOutL;
        double windowAvg = uOutL + windowSize / 2;
        //Step 1.2: approximate r1 and r2
        ResistanceChain resChain = ResistanceCalculator.calcResistanceChain(u1, new double[]{windowAvg}, ampere, eSeries);
        //Step 1.3: calculate r3
        double[] res = new double[]{resChain.getResistances()[0], resChain.getResistances()[1], 0};
        res[2] = (res[0] * (u1 - windowAvg)) / windowSize;

        //STEP 2: extrapolate calculated values
        List<CompHyst> circuits = new ArrayList<>();

        double[] r1variants = Calc.getResistancesAround(res[0], eSeries, 30);
        double[] r3variants = Calc.getResistancesAround(res[2], eSeries, 30);

        //Step 2.1: calculate their proposed ampere values and their quadratic distance from the desired values
        for (int r1i = 0; r1i < r1variants.length; r1i++) {
            for (int r3i = 0; r3i < r3variants.length; r3i++) {
                double[] matrixSolution1 = calc(r1variants[r1i], res[1], r3variants[r3i], u1, u2high);
                double[] matrixSolution2 = calc(r1variants[r1i], res[1], r3variants[r3i], u1, 0);

                CompHyst circuit = new CompHyst(u1, u2high, uOutH, uOutL, matrixSolution1);

                circuits.add(circuit);

                System.out.println("calculated for r1 = " + r1variants[r1i] + " and r3 = " + r3variants[r3i]);
            }
        }
        Collections.sort(circuits);
        return new ArrayList<>(circuits.subList(0, Math.min(30, circuits.size())));
    }

    private static double[] calc(double r1, double r2, double r3, double u1, double u2) {
        double[] ampere = new double[]{0, 0, 0};

        //calculate i2. this formula was derived from an equation system
        ampere[1] = (u1 / r1 - u2 / r3) / (r2 / r1 + 1 - r2 / r3);
        ampere[2] = (u2 - r2 * ampere[1]) / r3;
        ampere[0] = (u1 - r2 * ampere[1]) / r1;

        return ampere;
    }
}
