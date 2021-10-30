package enigma;

import java.util.*;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Ryan Shih
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    public String _permutation;
    public char[][] _permList;
    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _permutation = cycles;
        _permList = listedPermutation(_permutation);
    }

    private char[][] listedPermutation(String permutation) {
        char[][] segmentCycles = new char[0][0];
        permutation = permutation.replaceAll("\\s+", "");
        if (Objects.equals(permutation, "")) {
            return segmentCycles;
        }
        permutation = permutation.substring(1, permutation.length() - 1);
        String[] arrCycles = permutation.split("\\)\\(");
        int ind1 = 0;
        for (String s : arrCycles) {
            char[] piece = new char[0];
            int ind2 = 0;
            for (char c: s.toCharArray()) {
                char[] temp1 = new char[piece.length + 1];
                System.arraycopy(piece, 0, temp1, 0, piece.length);
                piece = temp1;
                piece[ind2] = c;
                ind2 += 1;
            }
            char[][] temp2 = new char[segmentCycles.length + 1][];
            System.arraycopy(segmentCycles, 0, temp2, 0, segmentCycles.length);
            segmentCycles = temp2;
            segmentCycles[ind1] = piece;
            ind1 += 1;
        }
        return segmentCycles;
    }

    private int[] find(char cTarget) {
        int[] voidLst = new int[]{-1,-1};
        int row = 0;
        for (char[] cLst : _permList) {
            int col = 0;
            for (char c : cLst) {
                if (c == cTarget) {
                    return new int[]{row, col};
                }
                col += 1;
            }
            row += 1;
        }
        return voidLst;
    }

    private int cX(char cTarget) {
        return find(cTarget)[0];
    }

    private int cY(char cTarget) {
        return find(cTarget)[1];
    }

    private char[] getRow(int row) {
        return _permList[row];
    }

    private char getChar(int row, int col) {
        return _permList[row][col];
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        _permutation += cycle;
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        if (p >= _alphabet.size()) {
            throw new EnigmaException("Alphabet does not contain character at index" + p);
        }
        return _alphabet.toInt(permute(_alphabet.toChar(p)));
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        if (c >= _alphabet.size()) {
            throw new EnigmaException("Alphabet does not contain character at index" + c);
        }
        return _alphabet.toInt(invert(_alphabet.toChar(c)));
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        if (!_alphabet.contains(p)) {
            throw new EnigmaException("Alphabet does not contain character" + p);
        }
        if (Arrays.equals(find(p), new int[]{-1, -1})) {
            return p;
        }
        int x = cX(p); int y = cY(p); char[] pRow = getRow(cX(p));
        if (y == pRow.length - 1) {
            return getChar(x, 0);
        } else {
            return getChar(x, y + 1);
        }
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        if (!_alphabet.contains(c)) {
            throw new EnigmaException("Alphabet does not contain character" + c);
        }
        if (Arrays.equals(find(c), new int[]{-1, -1})) {
            return c;
        }
        int x = cX(c); int y = cY(c); char[] pRow = getRow(cX(c));
        if (y == 0) {
            return getChar(x, pRow.length - 1);
        } else {
            return getChar(x, y - 1);
        }
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        boolean result = true;
        for (char c : _alphabet._charList) {
            if (Arrays.equals(find(c), new int[]{-1, -1})) {
                result = false;
                break;
            }
            for (char[] cLst : _permList) {
                if (cLst.length == 1) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }
}
