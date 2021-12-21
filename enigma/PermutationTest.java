package enigma;

import org.checkerframework.checker.units.qual.A;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Connor Bernard
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
    public void checkA() {
        Alphabet alphabet = new Alphabet("abcdefghijklmnopqrstuvwxyz");
        Permutation newPerm = new Permutation("(abc)", alphabet);
        assertEquals('b', newPerm.permute('a'));
        assertEquals('c', newPerm.invert('a'));
    }
    @Test
    public void checkB() {
        Alphabet alphabet = new Alphabet("abcdefghijklmnopqrstuvwxyz");
        Permutation newPerm = new Permutation("(abc)", alphabet);
        assertEquals('c', newPerm.permute('b'));
        assertEquals('a', newPerm.invert('b'));
    }
    @Test
    public void checkC() {
        Alphabet alphabet = new Alphabet("abcdefghijklmnopqrstuvwxyz");
        Permutation newPerm = new Permutation("(abc)", alphabet);
        assertEquals('a', newPerm.permute('c'));
        assertEquals('b', newPerm.invert('c'));
    }
    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }
    @Test
    public void checkIdentity() {
        String toCheck = "test";
        String converted = "";
        Permutation testPerm = new Permutation("",
                new Alphabet("abcdefghijklmnopqrstuvwxyz"));
        for (int i = 0; i < toCheck.length(); i++) {
            converted += testPerm.permute(toCheck.charAt(i));
        }
        assertEquals(converted, toCheck);
    }
    @Test
    public void checkChanging() {
        String toCheck = "testcb";
        String converted = "";
        Permutation testPerm = new Permutation("(tes) (abc)",
                new Alphabet("abcdefghijklmnopqrstuvwxyz"));
        for (int i = 0; i < toCheck.length(); i++) {
            converted += testPerm.permute(toCheck.charAt(i));
        }
        assertEquals("esteac", converted);
    }
    @Test
    public void checkChangingHilfinger() {
        String toCheck = "hignflr";
        String converted = "";
        Permutation testPerm = new Permutation("(hig) (nf) (l)",
                new Alphabet("hilfngr"));
        for (int i = 0; i < toCheck.length(); i++) {
            converted += testPerm.permute(toCheck.charAt(i));
        }
        assertEquals("ighfnlr", converted);
    }

}
