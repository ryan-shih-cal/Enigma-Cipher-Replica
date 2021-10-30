package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Ryan Shih
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initially in its 0 setting (first character of its
     *  alphabet).
     */
    private final char[] _notches;
    private final String _name;
    private Permutation _permutation;

    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _name = name;
        _permutation = perm;
        _notches = notches.toCharArray();
    }

    @Override
    boolean atNotch() {
        for (char notch : _notches) {
            if (alphabet().toInt(notch) == _setting) {
                return true;
            }
        }
        return false;
    }

    @Override
    void advance() {
        if (rotates()) {
            _setting = (_setting + 1) % alphabet().size();
        }
    }

    @Override
    boolean rotates() {
        return true; //only for rightmost rotor
    }

    @Override
    public String toString() {
        return "Moving Rotor " + _name;
    }
}
