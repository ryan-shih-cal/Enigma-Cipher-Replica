package enigma;

import java.util.Objects;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Ryan Shih
 */
class Alphabet {

    /** A new alphabet containing CHARS. The K-th character has index
     *  K (numbering from 0). No character may be duplicated. */
    private String _chars;
    char[] _charList;

    Alphabet(String chars) {
        if (Objects.equals(chars, "")) {
            throw new EnigmaException("Empty alphabet");
        }
        _chars = chars;
        _charList = _chars.toCharArray();
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _chars.length();
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        for (char c : _charList) {
            if (c == ch) {
                return true;
            }
        }
        return false;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        return (char) (_charList[index]);
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        int ind = 0;
        for (int i = 0; i < size(); i += 1) {
            if (toChar(i) == ch) {
                ind = i;
                break;
            }
        }
        return ind;
    }

}
