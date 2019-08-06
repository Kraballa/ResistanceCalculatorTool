package Main.Logic;

import Main.Calc;

import java.util.LinkedList;
import java.util.List;

/*
 * Project by Vinzent Brömauer
 * vinz.corno@web.de
 *
 * This file is licensed under the GPL license Version 3.
 * See https://www.gnu.org/licenses/ for the full license.
 */
public class ResistanceChain implements Comparable<ResistanceChain> {

    private List<Double> resistances;
    private List<Double> desired;

    private double[] optimalOutputs;
    private double[] outputs;

    private double ampere;
    private double voltIn;
    private double deviationCoefficient;

    public ResistanceChain() {
        resistances = new LinkedList<>();
        desired = new LinkedList<>();
        ampere = 0;
        voltIn = 0;
    }

    public void addResistance(double res, double target) {
        resistances.add(res);
        desired.add(target);
    }

    public void setOptimalOutputs(double[] optimalOutputs) {
        this.optimalOutputs = optimalOutputs;
    }

    public void setAmpere(double ampere) {
        this.ampere = ampere;
    }

    public void setVoltIn(double voltIn) {
        this.voltIn = voltIn;
    }

    private void calculateOutputs() {
        outputs = new double[resistances.size() - 1];

        double[] resistanceArray = getResistances();

        for (int i = 0; i < outputs.length; i++) {
            outputs[i] = Calc.getAbsoluteOutput(Calc.sumupTo(resistanceArray, i), Calc.sumupFrom(resistanceArray, i + 1), voltIn);
        }
    }

    public double[] getResistances() {
        return resistances.stream().mapToDouble(i -> i).toArray();
    }

    public double[] getDesired() {
        return desired.stream().mapToDouble(i -> i).toArray();
    }

    public double[] getOutputs() {
        calculateOutputs();
        return outputs;
    }

    public double[] getOptimalOutputs() {
        return optimalOutputs;
    }

    public double getDeviation() {
        calculateDeviationCoefficient();
        return deviationCoefficient;
    }

    public double getAmpere() {
        return ampere;
    }

    public double getVoltIn() {
        return voltIn;
    }

    private void calculateDeviationCoefficient() {
        if (resistances.size() != optimalOutputs.length + 1) {
            return;
        }
        double dev = 0;
        calculateOutputs();
        for (int i = 0; i < optimalOutputs.length; i++) {
            double currentDeviation = optimalOutputs[i] - outputs[i];
            currentDeviation = Math.abs(currentDeviation);
            dev += Math.pow(currentDeviation / optimalOutputs[i], 2);
        }
        deviationCoefficient = Math.sqrt(dev);
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < resistances.size(); i++) {
            ret.append(Calc.roundWithComma(getResistances()[i], 3)).append("Ω  ");
        }
        return ret.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ResistanceChain) {
            ResistanceChain chain = (ResistanceChain) o;
            if (this.resistances.size() == chain.resistances.size()) {
                for (int i = 0; i < resistances.size(); i++) {
                    if (getResistances()[i] != chain.getResistances()[i]) {
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(ResistanceChain o) {
        return Double.compare(this.getDeviation(), o.getDeviation());
    }
}
