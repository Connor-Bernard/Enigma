package enigma;

import org.junit.Test;
import org.junit.Rule;
import java.util.ArrayList;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

public class MachineTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);
    private Alphabet alphabet = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    private ArrayList<Rotor> allRotors() {
        ArrayList<Rotor> rotors = new ArrayList<Rotor>();
        rotors.add(new MovingRotor("I",
                new Permutation("(AELTPHQXRU) (BKNW) (CMOY) "
                       + "(DFG) (IV) (JZ) (S)",
                        alphabet), "Q"));
        rotors.add(new MovingRotor("II",
                new Permutation("(FIXVYOMW) (CDKLHUP) (ESZ) "
                       + "(BJ) (GR) (NT) (A) (Q)",
                        alphabet), "E"));
        rotors.add(new MovingRotor("III",
                new Permutation("(ABDHPEJT) (CFLVMZOYQIRWUKXSG) (N)",
                        alphabet), "V"));
        rotors.add(new MovingRotor("IV",
                new Permutation("(AEPLIYWCOXMRFZBSTGJQNH) (DV) (KU)",
                        alphabet), "J"));
        rotors.add(new MovingRotor("V",
                new Permutation("(AVOLDRWFIUQ)(BZKSMNHYC) (EGTJPX)",
                        alphabet), "Z"));
        rotors.add(new MovingRotor("VI",
                new Permutation("(AJQDVLEOZWIYTS) (CGMNHFUX) (BPRK)",
                        alphabet), "ZM"));
        rotors.add(new MovingRotor("VII",
                new Permutation("(ANOUPFRIMBZTLWKSVEGCJYDHXQ)",
                        alphabet), "ZM"));
        rotors.add(new MovingRotor("VIII",
                new Permutation("(AFLSETWUNDHOZVICQ) (BKJ) (GXY) (MPR)",
                        alphabet), "ZM"));
        rotors.add(new FixedRotor("BETA",
                new Permutation("(ALBEVFCYODJWUGNMQTZSKPR) (HIX)",
                        alphabet)));
        rotors.add(new FixedRotor("GAMMA",
                new Permutation("(AFNIRLBSQWVXGUZDKMTPCOYJHE)",
                        alphabet)));
        rotors.add(new Reflector("B",
                new Permutation("(AE) (BN) (CK) (DQ) (FU) (GY) (HW) "
                       + "(IJ) (LO) (MP) (RX) (SZ) (TV)", alphabet)));
        rotors.add(new Reflector("C",
                new Permutation("(AR) (BD) (CO) (EJ) (FN) (GT) (HK) "
                       + "(IV) (LM) (PW) (QZ) (SX) (UY)", alphabet)));
        return rotors;
    }
    private Machine enigmaGen() {
        Machine enigma = new Machine(alphabet, 5, 3, allRotors());
        enigma.insertRotors(new String[]{"B", "BETA", "III", "IV", "I"});
        return enigma;
    }
    @Test
    public void insertRotorsTest() {
        Machine enigma = enigmaGen();
        Rotor[] newRotors = enigma.getRotors();
        assertEquals("B", newRotors[0].name());
        assertEquals("BETA", newRotors[1].name());
        assertEquals("III", newRotors[2].name());
        assertEquals("IV", newRotors[3].name());
        assertEquals("I", newRotors[4].name());
    }
    @Test
    public void setRotorsTest() {
        Machine enigma = enigmaGen();
        enigma.setRotors("AXLE");
        Rotor[] newRotors = enigma.getRotors();
        assertEquals(0, newRotors[1].setting());
        assertEquals(23, newRotors[2].setting());
        assertEquals(11, newRotors[3].setting());
        assertEquals(4, newRotors[4].setting());
    }
    @Test
    public void convertTest() {
        Machine enigma = enigmaGen();
        enigma.setRotors("AXLE");
        enigma.setPlugboard(new Permutation("(HQ) (EX) (IP) (TR) (BY)",
                alphabet));
        assertEquals("QVPQ", enigma.convert("FROM"));
    }
    @Test
    public void invertTest() {
        Machine enigma = enigmaGen();
        enigma.setRotors("AXLE");
        enigma.setPlugboard(new Permutation("(HQ) (EX) (IP) (TR) (BY)",
                alphabet));
        assertEquals("FROM", enigma.convert("QVPQ"));
    }
    @Test
    public void convertTrivial() {
        Machine enigma = new Machine(alphabet, 5, 3, allRotors());
        enigma.insertRotors(new String[]{"B", "BETA", "I", "II", "III"});
        enigma.setPlugboard(new Permutation("", alphabet));
        enigma.setRotors("AAAA");
        assertEquals("ILBDA", enigma.convert("HELLO"));
    }
    @Test
    public void convertLong() {
        Machine enigma = new Machine(alphabet, 5, 3, allRotors());
        enigma.insertRotors(new String[]{"B", "BETA", "I", "II", "III"});
        enigma.setPlugboard(new Permutation("", alphabet));
        enigma.setRotors("AAAA");
        assertEquals("", enigma.convert(""));
    }
}
