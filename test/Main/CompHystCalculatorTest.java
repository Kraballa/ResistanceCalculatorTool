package Main;

import Main.Logic.CompHyst;
import Main.Logic.CompHystCalculator;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class CompHystCalculatorTest {
    @Test
    void testMatrixCalc() {
        double[] res = CompHystCalculator.solveForAmpere(1000, 200, 50000, 10, 10);
        System.out.println("res: " + res[0] + " " + res[1] + " " + res[2]);
        System.out.println("line1: " + Calc.roundWithComma(-10 + res[0] * 1000 + res[1] * 200, 6));
        System.out.println("line2: " + Calc.roundWithComma(-10 + res[2] * 50000 + res[1] * 200, 6));
        System.out.println("line3: " + Calc.roundWithComma(-res[0] + res[1] - res[2], 6));

    }

    @Test
    void testCompHystRuntime() {
        List<CompHyst> list = CompHystCalculator.calcCompHyst(10, 5, 2.5, 2, 0.01, 48, 60);
        CompHyst first = list.iterator().next();

        System.out.println(first.toString());
        System.out.println(Arrays.toString(first.getResistances()));
    }
}
