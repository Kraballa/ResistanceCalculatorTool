package Main.Logic;

public class CompHyst implements Comparable<CompHyst> {

    private double u1, u2high, uOutH, uOutL, uOutHD, uOutLD = 0;
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

    public void setHighLow(double high, double low, double desiredH, double desiredL) {
        uOutH = high;
        uOutL = low;
        uOutHD = desiredH;
        uOutLD = desiredL;
    }

    public double getuOutH() {
        return uOutH;
    }

    public double getuOutL() {
        return uOutL;
    }

    public double getuOutHD() {
        return uOutHD;
    }

    public double getuOutLD() {
        return uOutLD;
    }

    public double getDeviation() {
        return deviation;
    }

    public double[] getResistances() {
        return resistances;
    }

    public double[] getAmps() {
        return amps;
    }

    @Override
    public String toString() {
        return "r1: " + resistances[0] + "Ω  r2: " + resistances[1] + "Ω  r3: " + resistances[2] + "Ω";
    }

    @Override
    public int compareTo(CompHyst o) {
        return Double.compare(this.getDeviation(), o.getDeviation());
    }
}
