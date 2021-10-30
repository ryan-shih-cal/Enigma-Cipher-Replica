package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a reflector in the enigma.
 *  @author Ryan Shih
 */
class Reflector extends FixedRotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is PERM. */
    private final String _name;
    private Permutation _permutation;

    Reflector(String name, Permutation perm) {
        super(name, perm);
        _name = name;
        _permutation = perm;
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

    @Override
    void set(char cposn) {
        if (_permutation.alphabet().toInt(cposn) != 0) {
            throw error("reflector has only one position");
        }
    }

    @Override
    public String toString() {
        return "Reflector " + _name;
    }
}
