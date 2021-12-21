package enigma;

import afu.org.checkerframework.checker.oigj.qual.O;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Connor Bernard
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
    }
    /** Advance me one position, if possible. By default, does nothing. */
    @Override
    void advance() {
        set(permutation().wrap(setting() + 1));
    }
    /** Return true iff I have a ratchet and can move. */
    @Override
    boolean rotates() {
        return true;
    }
    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    @Override
    boolean atNotch() {
        return _notches.contains(
                Character.toString(
                        alphabet().toChar(setting())));
    }
    /** A string of all notches. **/
    private String _notches;

}
