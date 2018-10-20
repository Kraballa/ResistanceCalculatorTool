package Main;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResistorChainTest {

    ResistorChain chain;

    @BeforeEach
    void setUp() {
        chain = new ResistorChain();
    }

    @AfterEach
    void tearDown() {
        chain = null;
    }

    @Test
    void add() {
        chain.add(1.0);
        assertEquals(0,chain.widerstaende.length);
        chain = chain.add(1.0);
        assertEquals(1.0,chain.widerstaende[0],0.00001);
    }

    @Test
    void calcGlobalDeviation() {
    }

    @Test
    void calcIndividualDeviation() {
    }

    @Test
    void getDeviation() {
    }

    @Test
    void compareTo() {
    }
}