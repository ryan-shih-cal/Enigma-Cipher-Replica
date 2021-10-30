package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotor that has no ratchet and does not advance.
 *  @author Ryan Shih
 */
class FixedRotor extends Rotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is given by PERM. */
    private final String _name;
    private Permutation _permutation;

    FixedRotor(String name, Permutation perm) {
        super(name, perm);
        _name = name;
        _permutation = perm;
    }

    @Override
    public String toString() {
        return "Fixed Rotor " + _name;
    }
}
