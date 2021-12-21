package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Connor Bernard
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles;
        int openParens = 0;
        int closeParens = 0;
        String letters = "";
        for (int i = 0; i < cycles.length(); i++) {
            if (cycles.charAt(i) != ' ') {
                if (cycles.charAt(i) == '(') {
                    if (cycles.charAt(i + 1) == ')') {
                        throw new EnigmaException("Provided cycles imporperly"
                               + " formatted");
                    }
                    openParens += 1;
                } else if (cycles.charAt(i) == ')') {
                    closeParens += 1;
                } else {
                    String letter = "" + cycles.charAt(i);
                    if (letters.contains(letter)) {
                        throw new EnigmaException("Letter repeated in cycle");
                    } else if (alphabet.toInt(cycles.charAt(i)) == -1) {
                        char a = (char) cycles.charAt(i);
                        throw new EnigmaException("Letter in cycle not in"
                               + " Alphabet");
                    } else {
                        letters += letter;
                    }
                }
            }
            if (openParens - closeParens == 2
                    || openParens - closeParens == -2) {
                throw new EnigmaException("Provided cycles imporperly"
                       + " formatted");
            }
        }
        if (openParens - closeParens != 0
                || (openParens == 0 && !cycles.isEmpty())) {
            throw new EnigmaException("Provided cycles imporperly formatted");
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        _cycles += "( " + cycle + ")";
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        p = wrap(p);
        int returnIndex = 0;
        String parsed = "";
        for (int i = 0; i < _cycles.length(); i++) {
            if (_cycles.charAt(i) == '(') {
                returnIndex = _alphabet.toInt(_cycles.charAt(i + 1));
            }
            if (_alphabet.toInt(_cycles.charAt(i)) == p) {
                if (_cycles.charAt(i + 1) == ')') {
                    return returnIndex;
                } else {
                    return _alphabet.toInt(_cycles.charAt(i + 1));
                }
            }
        }
        return p;
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        c = wrap(c);
        int returnIndex = ' ';
        for (int i = _cycles.length() - 1; i > 0; i--) {
            if (_cycles.charAt(i) == ')') {
                returnIndex = _alphabet.toInt(_cycles.charAt(i - 1));
            }
            if (_alphabet.toInt(_cycles.charAt(i)) == c) {
                if (_cycles.charAt(i - 1) == '(') {
                    return returnIndex;
                } else {
                    return _alphabet.toInt(_cycles.charAt(i - 1));
                }
            }
        }
        return c;
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        return _alphabet.toChar(permute(_alphabet.toInt(p)));
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        return _alphabet.toChar(invert(_alphabet.toInt(c)));
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        int letterCount = 0;
        for (int i = 0; i < _cycles.length(); i++) {
            if (alphabet().contains(_cycles.charAt(i))) {
                letterCount += 1;
                if (permute(_cycles.charAt(i)) == _cycles.charAt(i)) {
                    return false;
                }
            }
        }
        return letterCount == size();
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;
    /** The list of mappings of this permutation. */
    private String _cycles;
}
