package enigma;

import java.util.HashMap;
import java.util.Collection;

/** Class that represents a complete enigma machine.
 *  @author Connor Bernard
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;
        _rotors = new Rotor[numRotors];
        Object[] rotors = allRotors.toArray();
        allRotorHM = new HashMap<String, Rotor>();
        int numMovingRotors = 0;
        for (Object rotor : rotors) {
            allRotorHM.put(((Rotor) rotor).name(), (Rotor) rotor);
        }
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        int numMovingRotors = 0;
        for (int i = 0; i < rotors.length; i++) {
            if (allRotorHM.get(rotors[i]).reflecting() && i != 0) {
                throw new EnigmaException("Reflector in wrong place.");
            }
            if (allRotorHM.get(rotors[i]).rotates()) {
                numMovingRotors += 1;
            }
            _rotors[i] = allRotorHM.get(rotors[i]);
        }
        if (numMovingRotors != _pawls) {
            throw new EnigmaException("Wrong number of arguments");
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        for (int i = 1; i < numRotors(); i++) {
            _rotors[i].set(setting.charAt(i - 1));
        }
    }
    /** Sets the alphabet to the string passed in.
     * @param alphabets = string of characters representing a new alphabet */
    void setAlphabets(String alphabets) {
        for (int i = 1; i < numRotors(); i++) {
            _rotors[i].rotateAlphabet(alphabets.charAt(i - 1));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        c = _plugboard.permute(c);
        boolean[] notches = new boolean[numRotors()];
        for (int i = numRotors() - 1; i >= 0; i--) {
            if (_rotors[i].atNotch() && _rotors[i - 1].rotates()) {
                notches[i] = true;
                notches[i - 1] = true;
            }
        }
        notches[numRotors() - 1] = true;
        for (int i = 0; i < numRotors(); i++) {
            if (notches[i]) {
                _rotors[i].advance();
            }
        }
        for (int i = numRotors() - 1; i >= 0; i--) {
            c = _rotors[i].convertForward(c);
        }
        for (int i = 1; i < numRotors(); i++) {
            c = _rotors[i].convertBackward(c);
        }
        c = _plugboard.permute(c);
        return c;
    }
    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String converted = "";
        for (int i = 0; i < msg.length(); i++) {
            converted += _alphabet.toChar(convert(
                    _alphabet.toInt(msg.charAt(i))));
        }
        return converted;
    }
    /** Getter method for allRotorsHM.
     * @return hashmap of all rotors
     **/
    public HashMap<String, Rotor> getAllRotorHM() {
        return allRotorHM;
    }
    /** Getter method for rotors.
     * @return array of rotors being used
     **/
    public Rotor[] getRotors() {
        return _rotors;
    }
    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;
    /** number of rotors. **/
    private int _numRotors;
    /** number of pawls. **/
    private int _pawls;
    /** Collection of all rotors. **/
    private Collection<Rotor> _allRotors;
    /** Mapping of each rotor name to the rotor itself. **/
    private HashMap<String, Rotor> allRotorHM;
    /** Array of all rotors. **/
    private Rotor[] _rotors;
    /** Permutation of plugboard to run through before and after. **/
    private Permutation _plugboard;
}
