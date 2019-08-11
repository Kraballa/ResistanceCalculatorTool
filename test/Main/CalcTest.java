package Main;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalcTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void sortDescending() {
    }

    @Test
    void sumupSegment() {
    }

    @Test
    void sumupFrom() {
    }

    @Test
    void sumupTo() {
    }

    @Test
    void sumup() {
    }

    @Test
    void roundWithComma() {
        assertEquals(3.3,Calc.roundWithComma(3.342,1),0.0001);
        assertEquals(3.3,Calc.roundWithComma(3.256,1),0.0001);

        assertEquals(50.4,Calc.roundWithComma(50.4436981,1),0.0001);
        assertEquals(3.456,Calc.roundWithComma(3.455679,3),0.0001);
    }

    @Test
    void getOptimalResistor() {
        double res = Calc.getOptimalResistor(1000,10,5);
        assertEquals(res,1000.0,0.00001);
        res = Calc.getOptimalResistor(6000,20,8);
        assertEquals(res,4000,0.0001);
    }

    @Test
    void getAbsoluteOutput() {
        assertEquals(1.0,Calc.getAbsoluteOutput(5.0,5.0,2.0),0.0001);
    }

    @Test
    void getBestResistance() {
        assertEquals(2.2,Calc.getBestResistance(3,3),0.0001);
        assertEquals(4.7,Calc.getBestResistance(4,3),0.0001);
        assertEquals(33,Calc.getBestResistance(30,6),0.0001);
        assertEquals(680,Calc.getBestResistance(600,6),0.0001);
        assertEquals(1200,Calc.getBestResistance(1200,12),0.0001);
        assertEquals(2700,Calc.getBestResistance(2500,12),0.0001);

    }

    @Test
    void getResAroundTest() {
        String s = "";
        for (double cur : Calc.getResistancesAround(1500, 96, 100)) {
            s += Calc.roundWithComma(cur, 2) + "_";
        }
        System.out.println(s);
    }
}