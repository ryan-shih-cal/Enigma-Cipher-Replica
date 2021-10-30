package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import java.util.HashMap;

import static enigma.TestUtils.*;

public class RotorTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    @Test
    public void testConvertForward() {
        Permutation p = new Permutation("(ABC)(DE)(F)", new Alphabet("ABCDEF"));
        Rotor r = new Rotor("test 1", p);
        assertEquals(p.alphabet().toInt('B'), r.convertForward(p.alphabet().toInt('A')));
        r.set(4);
        assertEquals(p.alphabet().toInt('F'), r.convertForward(p.alphabet().toInt('A')));
        r.set('F');
        assertEquals(p.alphabet().toInt('A'), r.convertForward(p.alphabet().toInt('A')));
        assertEquals(p.alphabet().toInt('B'), r.convertForward(p.alphabet().toInt('D')));
    }


    @Test
    public void testConvertBackward() {
        Permutation p = new Permutation("(ABC)(DE)(F)", new Alphabet("ABCDEF"));
        Rotor r = new Rotor("test 1", p);
        assertEquals(p.alphabet().toInt('A'), r.convertBackward(p.alphabet().toInt('B')));
        r.set(4);
        assertEquals(p.alphabet().toInt('A'), r.convertBackward(p.alphabet().toInt('F')));
        r.set('F');
        assertEquals(p.alphabet().toInt('A'), r.convertBackward(p.alphabet().toInt('A')));
        assertEquals(p.alphabet().toInt('D'), r.convertBackward(p.alphabet().toInt('B')));
    }
}
