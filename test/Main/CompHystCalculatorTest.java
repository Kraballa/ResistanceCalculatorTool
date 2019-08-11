package Main;

import Main.Logic.CompHystCalculator;
import org.junit.jupiter.api.Test;

public class CompHystCalculatorTest {
    @Test
    void testCompHystRuntime() {
        CompHystCalculator.calcCompHyst(10, 10, 1.954, 1.61, 0.01, 96);
    }
}
