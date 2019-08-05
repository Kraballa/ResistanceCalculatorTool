package Main.Logic;

import Main.Calc;

import java.util.List;

public class ResistanceChain {

    private List<Double> resistances;
    private List<Double> desired;

    private double[] optimalOutputs;
    private double[] outputs;


    private double ampere;
    private double voltIn;
    private double deviationCoefficient;

    public ResistanceChain(double[] optimalOutputs) {
        this.optimalOutputs = optimalOutputs;
    }

    public void addResistance(double res, double target) {
        resistances.add(res);
        resistances.add(target);
    }

    public void calculateOutputs() {
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

    public double getDeviation() {
        calculateDeviationCoefficient();
        return deviationCoefficient;
    }

    public double getAmpere() {
        return ampere;
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
}
