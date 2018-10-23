package Main;

/*
 * Project by Vinzent Brömauer
 * vinz.corno@web.de
 *
 * This file is licensed under the GPL license Version 3.
 * See https://www.gnu.org/licenses/ for the full license.
 */

public class ResistorChain implements Comparable<ResistorChain>{

    public final double widerstaende[];
    private double deviation = 99999;

    private double[] ist;

    private Berechner berechner;

    public ResistorChain(){
        this(new double[]{});
    }

    public ResistorChain(double[] widerstaende){
        this.widerstaende = widerstaende;
    }

    public ResistorChain add(double resistance){
        double[] widerstNew = new double[widerstaende.length+1];
        for(int i = 0; i < widerstaende.length; i++){
            widerstNew[i] = widerstaende[i];
        }
        widerstNew[widerstaende.length] = resistance;
        ResistorChain ret = new ResistorChain(widerstNew);
        ret.setIst(this.getIst());
        if(getBerechner()!=null){
            ret.setBerechner(getBerechner());
        }
        return ret;
    }

    public void calcGlobalDeviation(){
        double[] soll = getBerechner().getVoltOut();
        if(widerstaende.length != soll.length + 1){
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
            actual[i] = Calc.getAbsoluteOutput(Calc.sumupTo(widerstaende,i), Calc.sumupFrom(widerstaende,i+1),voltIn);
        }
        setIst(actual);
    }

    public void calcResistanceDeviation(double[] best){
        if(best.length == widerstaende.length){
            double dev = 0;
            for(int i = 0; i < widerstaende.length; i++){
                dev += Math.pow(Math.abs((widerstaende[i]-best[i])/best[i]),2);
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
        for (int i = 0; i < widerstaende.length; i++){
            ret.append(Calc.roundWithComma(widerstaende[i], 3)).append("Ω  ");
        }
        ret.append("= ").append(Calc.roundWithComma(deviation, 5));
        return ret.toString();
    }

    public double getDeviation() {
        return deviation;
    }

    public Berechner getBerechner() {
        return berechner;
    }

    public void setBerechner(Berechner berechner) {
        this.berechner = berechner;
    }

    public double[] getSoll() {
        return getBerechner().getVoltOut();
    }

    public void setSoll(double[] soll) {
        getBerechner().setVoltOut(soll);
    }

    public double[] getIst() {
        return ist;
    }

    public void setIst(double[] ist) {
        this.ist = ist;
    }

    public double getCurrent() {
        return getBerechner().getCurrentBorder()[0];
    }

    public void setCurrent(double current) {
        this.getBerechner().setCurrent(current);
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof ResistorChain){
            ResistorChain kette = (ResistorChain) o;
            if(this.widerstaende.length == kette.widerstaende.length){
                for(int i = 0; i < widerstaende.length; i++){
                    if(widerstaende[i] != kette.widerstaende[i]){
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
