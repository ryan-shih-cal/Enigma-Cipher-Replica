package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.*;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Ryan Shih
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine m = readConfig();
        boolean hasSetting = false;

        while (_input.hasNextLine()) {
            String inputLn = _input.nextLine();
            String inputLnCut = inputLn.replaceAll("\\s+", "");
            if (inputLnCut.equals("")) {
                _output.println();
            } else if (inputLn.charAt(0) == '*') {
                setUp(m, inputLn);
                hasSetting = true;
            } else if (hasSetting) {
                String outputLn = m.convert(inputLnCut);
                printMessageLine(outputLn);
            } else {
                throw new EnigmaException("Missing setting");
            }
        }
        int numMRotors = 0;
        for (Rotor r : m._rotors) {
            if (r instanceof MovingRotor) {
                numMRotors += 1;
            }
        }
        if (numMRotors > m.numPawls()) {
            throw new EnigmaException("Pawls and moving rotors don't match");
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _alphabet = new Alphabet(_config.nextLine());
            int numRotors = _config.nextInt();
            int pawls = _config.nextInt();
            _config.nextLine();
            Collection<Rotor> rotorList = new LinkedList<Rotor>();
            while (_config.hasNextLine()) {
                rotorList.add(readRotor());
                if (_config.hasNextLine()) {
                    _config.nextLine();
                }
            }
            return new Machine(_alphabet, numRotors, pawls, rotorList);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String name = _config.next();
            String typeNotch = _config.next();
            char type = typeNotch.charAt(0);
            String notch = typeNotch.substring(1);
            String cycles = "";
            while (_config.hasNext("\\(.+\\)")) {
                cycles += _config.next();
            }
            if (_config.hasNext("\\(.+")) {
                throw new EnigmaException("Incomplete cycle");
            }
            switch (type) {
                case 'M':
                    return new MovingRotor(name, new Permutation(cycles, _alphabet), notch);
                case 'N':
                    return new FixedRotor(name, new Permutation(cycles, _alphabet));
                case 'R':
                    return new Reflector(name, new Permutation(cycles, _alphabet));
                default:
                    throw new EnigmaException("Rotor type is invalid");
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        Scanner sc = new Scanner(settings);
        sc.next();
        String[] rotors = new String[M.numRotors()];
        for (int i = 0; i < M.numRotors(); i += 1) {
            rotors[i] = sc.next();
        }
        M.insertRotors(rotors);
        if (!sc.hasNext()) {
            throw new EnigmaException("No setting");
        }
        String setting = sc.next();
        if (setting.length() > M.numRotors() - 1) {
            throw new EnigmaException("Setting does not match numRotors");
        }
        M.setRotors(setting);
        String plugboard = "";
        while (sc.hasNext("\\(..\\)")) {
            plugboard += sc.next();
        }
        M.setPlugboard(new Permutation(plugboard, _alphabet));
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        msg =  msg.replaceAll("\\s+", "");
        int msgInd = 0;
        StringBuilder msgNew = new StringBuilder();
        char[] msgArray = msg.toCharArray();
        for (char c : msgArray) {
            if (msgInd % 5 == 0 && msgInd != 0) {
                msgNew.append(" ");
            }
            msgNew.append(c);
            msgInd += 1;
        }
        _output.println(msgNew);
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
}
