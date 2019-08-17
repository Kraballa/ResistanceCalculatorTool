package Main.Logic;

/*
 * Project by Vinzent Brömauer
 * vinz.corno@web.de
 *
 * This file is licensed under the GPL license Version 3.
 * See https://www.gnu.org/licenses/ for the full license.
 */
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
        return "R1: " + resistances[0] + "Ω  R2: " + resistances[1] + "Ω  R3: " + resistances[2] + "Ω";
    }

    @Override
    public int compareTo(CompHyst o) {
        return Double.compare(this.getDeviation(), o.getDeviation());
    }
}
