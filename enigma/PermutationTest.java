package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import java.io.PrintStream;
import java.util.*;

import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Ryan Shih
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void checkInstances() {
        String cycle = "(ABC) (DE) (FGH)(IJ)(K)";
        Alphabet alpha = new Alphabet("ABCDEFGHIJK");
        Permutation p = new Permutation(cycle, alpha);
        PrintStream stream = new PrintStream(System.out);
        for (char[] c : p._permList) {
            stream.println(c);
        }
        stream.flush();
    }

    @Test
    public void testSize() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCDE"));
        assertEquals(5, p.size());
        assertNotEquals(1, p.size());
        Permutation p1 = new Permutation("(ABCD)(EFG)(HI)(J)", new Alphabet("ABCDEFGHIJ"));
        assertEquals(10, p1.size());
        Permutation p2 = new Permutation("(PERMUTA)(IONS)", new Alphabet("IONSPERMUTA"));
        assertEquals(11, p2.size());
    }

    @Test
    public void testPermute() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCDE"));
        assertEquals(0, p.permute(1));
        assertEquals(1, p.permute(3));
        assertEquals(4, p.permute(4));
        assertEquals('A', p.permute('B'));
        assertEquals('B', p.permute('D'));
        assertEquals('E', p.permute('E'));
        Permutation p1 = new Permutation("(ABCD)(EFG)(HI)(J)", new Alphabet("ABCDEFGHIJ"));
        assertEquals(5, p1.permute(4));
        assertEquals(7, p1.permute(8));
        assertEquals(9, p1.permute(9));
        assertEquals('F', p1.permute('E'));
        assertEquals('H', p1.permute('I'));
        assertEquals('J', p1.permute('J'));
        p1 = new Permutation("(ABCD) (EFG) (HI) (J)", new Alphabet("ABCDEFGHIJ"));
        assertEquals(5, p1.permute(4));
        assertEquals(7, p1.permute(8));
        assertEquals(9, p1.permute(9));
        assertEquals('F', p1.permute('E'));
        assertEquals('H', p1.permute('I'));
        assertEquals('J', p1.permute('J'));
        Permutation p2 = new Permutation("(PERMUTA)(IONS)", new Alphabet("IONSPERMUTA"));
        assertEquals(2, p2.permute(1));
        assertEquals(4, p2.permute(10));
        assertEquals('N', p2.permute('O'));
        assertEquals('P', p2.permute('A'));
    }

    @Test
    public void testInvert() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCDE"));
        assertEquals(1, p.invert(0));
        assertEquals(3, p.invert(1));
        assertEquals(4, p.invert(4));
        assertEquals('B', p.invert('A'));
        assertEquals('D', p.invert('B'));
        assertEquals('E', p.invert('E'));
        Permutation p1 = new Permutation("(ABCD)(EFG)(HI)(J)", new Alphabet("ABCDEFGHIJ"));
        assertEquals(4, p1.invert(5));
        assertEquals(8, p1.invert(7));
        assertEquals(9, p1.invert(9));
        assertEquals('E', p1.invert('F'));
        assertEquals('I', p1.invert('H'));
        assertEquals('J', p1.invert('J'));
        Permutation p2 = new Permutation("(PERMUTA)(IONS)", new Alphabet("IONSPERMUTA"));
        assertEquals(1, p2.invert(2));
        assertEquals(10, p2.invert(4));
        assertEquals('O', p2.invert('N'));
        assertEquals('A', p2.invert('P'));
    }

    @Test
    public void testAlphabet() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCDE"));
        assertEquals(5, p.alphabet().size());
        assertTrue(p.alphabet().contains('E'));
        assertEquals('E', p.alphabet().toChar(4));
        assertEquals(4, p.alphabet().toInt('E'));
        Permutation p1 = new Permutation("(ABCD)(EFG)(HI)(J)", new Alphabet("ABCDEFGHIJ"));
        assertEquals(10, p1.alphabet().size());
        assertTrue(p1.alphabet().contains('J'));
        assertEquals('J', p1.alphabet().toChar(9));
        assertEquals(9, p1.alphabet().toInt('J'));
        Permutation p2 = new Permutation("(PERMUTA)(IONS)", new Alphabet("IONSPERMUTA"));
        assertEquals(11, p2.alphabet().size());
        assertTrue(p2.alphabet().contains('A'));
        assertEquals('A', p2.alphabet().toChar(10));
        assertEquals(10, p2.alphabet().toInt('A'));
    }

    @Test
    public void testDerangement() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCDE"));
        assertFalse(p.derangement());
        Permutation p1 = new Permutation("(ABCD)(EFG)(HI)(J)", new Alphabet("ABCDEFGHIJ"));
        assertFalse(p1.derangement());
        Permutation p2 = new Permutation("(PERMUTA)(IONS)", new Alphabet("IONSPERMUTA"));
        assertTrue(p2.derangement());
    }

    @Test
    public void testNullPermutation() {
        Permutation p3 = new Permutation("", new Alphabet(""));
        //Test Size
        assertEquals(0, p3.size());
        //Test Alphabet Call
        assertEquals(0, p3.alphabet().size());
        assertFalse(p3.alphabet().contains('A'));
    }

    @Test(expected = EnigmaException.class)
    public void testNotInAlphabet() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCDE"));
        p.permute(5);
        p.permute('F');
        p.invert(5);
        p.invert('F');
        Permutation p3 = new Permutation("", new Alphabet(""));
        p3.permute(0);
        p3.permute('A');
        p3.invert(0);
        p3.invert('A');
        p3.derangement();
    }
}
