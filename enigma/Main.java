package enigma;

import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Connor Bernard
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
        Machine enigma = readConfig();
        String initialSetting = _input.nextLine();
        if (!(initialSetting.charAt(0) == '*')) {
            throw new EnigmaException(
                    "Incorrect input format - first line not a setting");
        }
        setUp(enigma, initialSetting);
        while (_input.hasNextLine()) {
            String line = _input.nextLine();
            if (line.isEmpty()) {
                printMessageLine(" ");
            }
            if (line.length() > 1 && line.charAt(0) == '*') {
                setUp(enigma, line);
            } else {
                String[] words = line.split(" ");
                String convertedLine = "";
                for (String word : words) {
                    convertedLine += word;
                }
                printMessageLine(enigma.convert(convertedLine));
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _alphabet = new Alphabet(_config.next());
            if (_alphabet.contains('*')
                    || _alphabet.contains('(') || _alphabet.contains(')')) {
                throw new EnigmaException(
                        "Incorrect config format or illegal"
                                + "character in alphabet declaration");
            }
            int numRotors = _config.nextInt();
            int pawls = _config.nextInt();
            while (_config.hasNext()) {
                rotors.add(readRotor());
            }
            return new Machine(_alphabet, numRotors, pawls, rotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String name = _config.next();
            String notches = _config.next();
            String cycles = "";
            while (_config.hasNext("\\s*\\(.+\\)\\s*")) {
                cycles += _config.next();
            }
            Permutation perm = new Permutation(cycles, _alphabet);
            if (notches.equals("N")) {
                return new FixedRotor(name, perm);
            } else if (notches.equals("R")) {
                return new Reflector(name, perm);
            } else {
                return new MovingRotor(name, perm, notches.substring(1));
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        String setting = "";
        String plugs = "";
        String alphabetRotation = "";
        String[] rotorArr = new String[M.numRotors()];
        int index = 0;
        Scanner set = new Scanner(settings.substring(2));
        while (set.hasNext()) {
            String curr = set.next();
            if (!(M.getAllRotorHM().containsKey(curr))) {
                if (curr.charAt(0) != '(' && setting.isEmpty()) {
                    setting = curr;
                } else if (curr.charAt(0) != '(') {
                    alphabetRotation = curr;
                } else {
                    plugs += curr + " ";
                }
            } else {
                try {
                    rotorArr[index] = curr;
                    index += 1;
                } catch (IndexOutOfBoundsException e) {
                    throw new EnigmaException("Wrong size array provided.");
                }
            }
        }
        ArrayList<String> names = new ArrayList<String>();
        for (String s : rotorArr) {
            if (s == null) {
                throw new EnigmaException("Wrong size array provided.");
            }
            if (names.contains(s)) {
                throw new EnigmaException("Duplicate rotor name.");
            } else {
                names.add(s);
            }
        }
        M.insertRotors(rotorArr);
        if (!alphabetRotation.isEmpty()) {
            M.setAlphabets(alphabetRotation);
        }
        M.setRotors(setting);
        M.setPlugboard(new Permutation(plugs, _alphabet));
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        if (msg.equals(" ")) {
            _output.println();
        } else {
            for (int i = 0; i < msg.length(); i += 5) {
                if (msg.length() - i <= 5) {
                    _output.println(msg.substring(i, msg.length()));
                } else {
                    _output.print(msg.substring(i, i + 5) + " ");
                }
            }
        }
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
    /** ArrayList of all rotors. **/
    private ArrayList<Rotor> rotors = new ArrayList<Rotor>();
}
