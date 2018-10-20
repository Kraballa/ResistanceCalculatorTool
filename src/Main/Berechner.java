package Main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/*
 * Project by Vinzent Brömauer
 * vinz.corno@web.de
 *
 * This file is licensed under the GPL license Version 3.
 * See https://www.gnu.org/licenses/ for the full license.
 */

public class Berechner{

    private double voltIn;
    private double[] voltOut;
    private double[] currentBorder;
    private int resistorGroup;

    public Berechner(double input, double[] outputs, double[] current){
        setVoltIn(input);
        setVoltOut(outputs);
        setCurrent(current);
        resistorGroup = 48;
    }

    public Berechner(double input, double[] outputs, double current){
        this(input,outputs,new double[]{current,current});
    }

    public ArrayList<ResistorChain> calculateChains(){
        ArrayList<ResistorChain> list = new ArrayList<>();
        for(double i = currentBorder[0]; i <= currentBorder[1]; i+=((currentBorder[1]-currentBorder[0])/1000)){
            ResistorChain chain = calculateChain(i);
            if(!list.contains(chain)){
                list.add(chain);
            }
        }
        Collections.sort(list);
        return new ArrayList<>(list.subList(0, Math.min(10,list.size())));
    }

    public ResistorChain calculateChain(double current){
        ResistorChain chain = new ResistorChain();
        double remainingRes = voltIn/current;
        double difference = 0;
        double newResistor;
        for(int i= 0; i < voltOut.length+1; i++){
            if(i < voltOut.length){
                //ratio = ratio between the desired output and remaining input voltage
                double ratio;
                if(i == 0){
                    ratio = voltOut[i]/voltIn;
                }else{
                    ratio = voltOut[i]/voltOut[i-1];
                }

                newResistor = Calc.getBestResistance((remainingRes + difference) - ratio*remainingRes,resistorGroup);
                difference = (remainingRes - ratio*remainingRes) - newResistor;
            }else{
                newResistor = Calc.getBestResistance(remainingRes + difference,resistorGroup);
            }
            remainingRes -= newResistor;
            chain = chain.add(newResistor);
        }

        chain.setBerechner(new Berechner(voltIn,voltOut,current));
        chain.calcIndividualDeviation(voltIn,voltOut);
        chain.calcGlobalDeviation();
        return chain;
    }

    public void setResistorGroup(int resistorGroup) {
        this.resistorGroup = resistorGroup;
    }

    public void setCurrent(double[] border){
        currentBorder = new double[2];
        Arrays.sort(border);
        if(border.length >= 2){
            currentBorder[0] = border[0];
            currentBorder[1] = border[1];
        }else{
            currentBorder[0] = 0.001;
            currentBorder[1] = 0.001;
        }
    }

    public void setCurrent(double value){
        currentBorder[0] = value;
        currentBorder[1] = value;
    }

    public double[] getCurrentBorder() {
        return currentBorder;
    }

    public void setVoltIn(double voltIn) {
        this.voltIn = voltIn;
    }

    public void setVoltOut(double[] voltOut){
        this.voltOut = Calc.sortDescending(voltOut);

    }

    public double getVoltIn() {
        return voltIn;
    }

    public double[] getVoltOut() {
        return voltOut;
    }


}
