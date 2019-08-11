package Main;

import Main.Logic.CompHystCalculator;
import org.junit.jupiter.api.Test;

public class CompHystCalculatorTest {
    @Test
    void testMatrixCalc() {
        double[] res = CompHystCalculator.calc(1000, 200, 200, 10, 10);
        System.out.println("res: " + res[0] + " " + res[1] + " " + res[2]);
        System.out.println("line1: " + Calc.roundWithComma(-10 + res[0] * 1000 + res[1] * 200, 6));
        System.out.println("line2: " + Calc.roundWithComma(-10 + res[2] * 200 + res[1] * 200, 6));
        System.out.println("line3: " + Calc.roundWithComma(-res[0] + res[1] - res[2], 6));

    }

    @Test
    void testCompHystRuntime() {
        CompHystCalculator.calcCompHyst(10, 10, 2.5, 2, 0.01, 48);
    }
}
