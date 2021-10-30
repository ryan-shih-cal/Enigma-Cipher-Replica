package enigma;

import java.util.HashMap;
import java.util.Collection;
import java.util.Objects;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Ryan Shih
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    private final int _numRotors;
    private final int _pawls;
    public Rotor[] _rotors;
    private Rotor[] _allRotors;
    private Permutation _plugboard;
    public int[] settings;

    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        assert(numRotors > 1);
        _numRotors = numRotors;
        assert(0 <= pawls && pawls < numRotors);
        _pawls = pawls;
        _rotors = new Rotor[numRotors];
        _allRotors = new Rotor[allRotors.size()];
        _allRotors = allRotors.toArray(_allRotors);

        settings = new int[numRotors - 1];
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
        int i = 0;
        for (String rName : rotors) {
            for (Rotor r : _allRotors) {
                if (Objects.equals(r.name(), rName)) {
                    _rotors[i] = r;
                    i += 1;
                }
            }
        }
        if (!(rotors.length == 0 || (_rotors[0] instanceof Reflector))) {
            throw new EnigmaException("Reflector not in correct place");
        }
        for (int i1 = 0; i1 < _rotors.length; i1 += 1) {
            if (_rotors[i1] instanceof FixedRotor) {
                for (int i2 = 0; i2 < i1; i2 += 1) {
                    if (_rotors[i2] instanceof MovingRotor) {
                        throw new EnigmaException("Moving rotor found left of fixed rotor");
                    }
                }
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        char[] newSettings = setting.toCharArray();
        int i = -1;
        for (Rotor r : _rotors) {
            if (i != -1) {
                r.set(newSettings[i]);
            }
            i += 1;
        }

        for (int iString = 0; iString < setting.length(); iString += 1) {
            settings[iString] = _rotors[iString+1].setting();
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    void advSetting() {
        int i = 0;
        while (i < _numRotors) {
            if (_rotors[i] instanceof MovingRotor) {
                if (i == _numRotors - 1) {
                    _rotors[i].advance();
                    settings[i - 1] += 1;
                } else if (_rotors[i+1].atNotch()) {
                    for (int iRest = i; iRest < _numRotors; iRest += 1) {
                        _rotors[iRest].advance();
                        settings[iRest - 1] += 1;
                    }
                    break;
                }
            }
            i += 1;
        }
    }
    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        if (!_alphabet.contains(_alphabet.toChar(c))) {
            throw new EnigmaException("Character not in alphabet");
        }
        advSetting();
        c = _plugboard.permute(c);
        for (int iRotor = _numRotors - 1; iRotor >= 0; iRotor -= 1) {
            c = _rotors[iRotor].convertForward(c);
        }
        for (int iRotor = 1; iRotor < _numRotors; iRotor += 1) {
            c = _rotors[iRotor].convertBackward(c);
        }
        c = _plugboard.permute(c);
        return c;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        char[] msgArray = msg.toCharArray();
        char[] convertedArray = new char[msgArray.length];
        for (int i = 0; i < msgArray.length; i += 1) {
            int charInt = _alphabet.toInt(msgArray[i]);
            int convertInt = convert(charInt);
            convertedArray[i] = _alphabet.toChar(convertInt);
        }
        return new String(convertedArray);
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;
}
