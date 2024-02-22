package hr.fer.oprpp1.hw05.crypto;

import hr.fer.oprpp1.hw05.crypto.Util;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class UtilTest {

    String hexArray = "01Ae22";
    byte[] byteArray = {1, -82, 34};

    @Test
    public void testHexToByte() {
        assertArrayEquals(byteArray, Util.hexToByte(hexArray));
    }

    @Test
    public void testByteToHex() {
        assertEquals(hexArray.toLowerCase(), Util.byteToHex(byteArray));
    }
}
