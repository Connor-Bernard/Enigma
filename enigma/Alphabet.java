package enigma;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Connor Bernard
 */
class Alphabet {
    /**
     * the array of characters in the alphabet.
     **/
    private char[] _alphabet;
    /** The string of characters that represents the alphabet. */
    private String _alphabetString;

    /** A new alphabet containing CHARS. The K-th character has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        if (chars.isEmpty() || chars.equals(" ")) {
            throw new EnigmaException("Empty Alphabet");
        }
        _alphabet = new char[chars.length()];
        for (int i = 0; i < chars.length(); i++) {
            _alphabet[i] = chars.charAt(i);
        }
        _alphabetString = chars;
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _alphabet.length;
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        for (char a : _alphabet) {
            if (a == ch) {
                return true;
            }
        }
        return false;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        if (index < 0 || index > _alphabet.length - 1) {
            throw new EnigmaException("Invalid alphabet index");
        }
        return _alphabet[index];
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        for (int i = 0; i < _alphabet.length; i++) {
            if (_alphabet[i] == ch) {
                return i;
            }
        }
        return -1;
    }
    /** Returns the alphabet as a string of chars. **/
    public String getAlphabetString() {
        return _alphabetString;
    }

}
