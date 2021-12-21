package enigma;

import afu.org.checkerframework.checker.oigj.qual.O;

import static enigma.EnigmaException.*;

/** Class that represents a reflector in the enigma.
 *  @author Connor Bernard
 */
class Reflector extends FixedRotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is PERM. */
    Reflector(String name, Permutation perm) {
        super(name, perm);
        _perm = perm;
    }

    @Override
    boolean reflecting() {
        return true;
    }

    @Override
    void set(int posn) {
        if (posn != 0) {
            throw error("reflector has only one position");
        }
    }
    /** The permutation being used. **/
    private Permutation _perm;

}
