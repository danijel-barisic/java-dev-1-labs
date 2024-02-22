package hr.fer.oprpp1.hw05.shell;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class UtilTest {

    @Test
    public void testCount(){
        assertEquals(6,Util.countEscapes("\\\\ \\\\\\\\ \\\"f \\\"\\\""));
    }
}
