package Main.Logic;

public class CompHyst implements Comparable<CompHyst> {

    private double u1, u2high, uOutH, uOutL = 0;
    private double[] amps;
    private double[] resistances;
    private double deviation;

    public CompHyst(double u1, double u2high, double[] resistances, double[] amps) {
        this.u1 = u1;
        this.u2high = u2high;
        this.resistances = resistances;
        this.amps = amps;
    }

    public void setDeviation(double deviation) {
        this.deviation = deviation;
    }

    public double getDeviation() {
        return deviation;
    }

    @Override
    public int compareTo(CompHyst o) {
        return Double.compare(this.getDeviation(), o.getDeviation());
    }
}
