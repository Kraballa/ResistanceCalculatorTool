package Main;

/*
 * Project by Vinzent Brömauer
 * vinz.corno@web.de
 *
 * This file is licensed under the GPL license Version 3.
 * See https://www.gnu.org/licenses/ for the full license.
 */

public class ResistorChain implements Comparable<ResistorChain>{

    public final double resistances[];
    private double deviation = 99999;

    private double[] ist;

    private ResCalculator resCalculator;

    public ResistorChain(){
        this(new double[]{});
    }

    public ResistorChain(double[] resistances) {
        this.resistances = resistances;
    }

    public ResistorChain add(double resistance){
        double[] widerstNew = new double[resistances.length + 1];
        for (int i = 0; i < resistances.length; i++) {
            widerstNew[i] = resistances[i];
        }
        widerstNew[resistances.length] = resistance;
        ResistorChain ret = new ResistorChain(widerstNew);
        ret.setIst(this.getIst());
        if (getResCalculator() != null) {
            ret.setResCalculator(getResCalculator());
        }
        return ret;
    }

    public void calcGlobalDeviation(){
        double[] soll = getResCalculator().getVoltOut();
        if (resistances.length != soll.length + 1) {
            return;
        }
        double dev = 0;
        double[] actual = getIst();
        for(int i = 0; i < soll.length; i++){
            double currentDeviation = soll[i] - actual[i];
            currentDeviation = Math.abs(currentDeviation);
            dev += Math.pow(currentDeviation/soll[i],2);
        }
        deviation = Math.sqrt(dev);
    }

    public void calcIndividualDeviation(double voltIn, double[] voltOut){
        double[] actual = new double[voltOut.length];
        for(int i = 0; i < voltOut.length; i++){
            actual[i] = Calc.getAbsoluteOutput(Calc.sumupTo(resistances, i), Calc.sumupFrom(resistances, i + 1), voltIn);
        }
        setIst(actual);
    }

    public void calcResistanceDeviation(double[] best){
        if (best.length == resistances.length) {
            double dev = 0;
            for (int i = 0; i < resistances.length; i++) {
                dev += Math.pow(Math.abs((resistances[i] - best[i]) / best[i]), 2);
            }
            dev = Math.sqrt(dev);
            deviation = dev;
        }else{
            deviation = 99999;
        }
    }

    @Override
    public String toString(){
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < resistances.length; i++) {
            ret.append(Calc.roundWithComma(resistances[i], 3)).append("Ω  ");
        }
        ret.append("= ").append(Calc.roundWithComma(deviation, 5));
        return ret.toString();
    }

    public double getDeviation() {
        return deviation;
    }

    public ResCalculator getResCalculator() {
        return resCalculator;
    }

    public void setResCalculator(ResCalculator resCalculator) {
        this.resCalculator = resCalculator;
    }

    public double[] getSoll() {
        return getResCalculator().getVoltOut();
    }

    public void setSoll(double[] soll) {
        getResCalculator().setVoltOut(soll);
    }

    public double[] getIst() {
        return ist;
    }

    public void setIst(double[] ist) {
        this.ist = ist;
    }

    public double getCurrent() {
        return getResCalculator().getCurrentBorder()[0];
    }

    public void setCurrent(double current) {
        this.getResCalculator().setCurrent(current);
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof ResistorChain){
            ResistorChain kette = (ResistorChain) o;
            if (this.resistances.length == kette.resistances.length) {
                for (int i = 0; i < resistances.length; i++) {
                    if (resistances[i] != kette.resistances[i]) {
                        return false;
                    }
                }
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    @Override
    public int compareTo(ResistorChain o) {
        return Double.compare(this.getDeviation(), o.getDeviation());
    }
}
