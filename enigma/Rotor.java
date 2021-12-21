package enigma;

import static enigma.EnigmaException.*;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Connor Bernard
 */
class Rotor {

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        if (name.split(" ").length > 1 || name.contains("(")
                || name.contains(")")) {
            throw new EnigmaException("Name improperly formatted");
        }
        _permutation = perm;
        _alphabet = perm.alphabet();
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return _setting;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        _setting = permutation().wrap(posn);
    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        set(alphabet().toInt(cposn));
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        return permutation().wrap(
                permutation().permute(p + setting()) - setting());
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        return permutation().wrap(
                permutation().invert(e + setting()) - setting());
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }
    /** Rotates the alphabet to the specified setting.
     * @param c = first character of the new alphabet */
    public void rotateAlphabet(char c) {
        String currAlphabet = _alphabet.getAlphabetString();
        int letterIndex;
        for (int i = 0; i < currAlphabet.length(); i++) {
            if (currAlphabet.charAt(i) == c) {
                letterIndex = i;
                currAlphabet = currAlphabet.substring(i)
                        + currAlphabet.substring(0, i);
                break;
            }
        }
        _alphabet = new Alphabet(currAlphabet);
    }
    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** My name. */
    private final String _name;
    /** My alphabet. */
    private Alphabet _alphabet;
    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;
    /** the setting for this rotor. **/
    private int _setting;

}
