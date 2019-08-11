package Main.Logic;

public class CompHyst implements Comparable<CompHyst> {

    private double u1, u2high, uOutH, uOutL = 0;
    private double[] amps;
    private double deviation;

    public CompHyst(double u1, double u2high, double uOutH, double uOutL, double[] amps) {
        this.u1 = u1;
        this.u2high = u2high;
        this.uOutH = uOutH;
        this.uOutL = uOutL;
        this.amps = amps;
        calcDeviation();
    }

    public double getDeviation() {
        return deviation;
    }

    private void calcDeviation() {
        //double deviation = Math.sqrt(Math.pow(,2)+Math.pow(,2)+Math.pow(,2));
    }

    @Override
    public int compareTo(CompHyst o) {
        return Double.compare(this.getDeviation(), o.getDeviation());
    }
}
