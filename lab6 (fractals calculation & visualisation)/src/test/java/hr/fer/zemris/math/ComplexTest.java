package hr.fer.zemris.math;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class ComplexTest {

//    Complex z, u;

    @BeforeEach
    public void init() {

    }

    @Test
    public void testMultiply() {
        assertEquals(new Complex(13, 34), new Complex(7, 2).multiply(new Complex(3, 4)));
    }

    @Test
    public void testDivide() {
        assertEquals(new Complex(-18. / 25, 51. / 25), new Complex(6, 9)
                .divide(new Complex(3, -4)));
    }

}
