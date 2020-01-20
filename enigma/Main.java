package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Dhivyaa N Mailvaganam
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
        Machine machine1 = readConfig();
        String input = _input.nextLine();
        inputLine1 = input.split(" ");
        if (inputLine1[0].charAt(0) != '*') {
            throw error("There is no configuration");
        } else if (input.contains("(")) {
            int firstBracket = input.indexOf("(");
            _plug = input.substring(firstBracket);
            input = input.substring(0, firstBracket);
            inputLine1 = input.split(" ");
            if (inputLine1.length - 2 != _configRotors) {
                throw error("Incorrect number of rotors specified");
            } else {
                _inputRotor = new String[inputLine1.length - 2];
                for (int o = 1; o < inputLine1.length - 1; o++) {
                    _inputRotor[o - 1] = inputLine1[o]; }
                machine1.insertRotors(_inputRotor);
                setUp(machine1, inputLine1[inputLine1.length - 1]);
                machine1.setPlugboard(new Permutation(_plug, _alphabet));
            }
        } else {
            _plug = "";
            if (inputLine1.length - 2 != _configRotors) {
                throw error("Incorrect number of rotors specified");
            } else {
                _inputRotor = new String[inputLine1.length - 2];
                for (int o = 1; o < inputLine1.length - 1; o++) {
                    _inputRotor[o - 1] = inputLine1[o]; }
                machine1.insertRotors(_inputRotor);
                setUp(machine1, inputLine1[inputLine1.length - 1]);
                Permutation _plugInput =  new Permutation(_plug, _alphabet);
                machine1.setPlugboard(_plugInput);
            }
        }
        while (_input.hasNextLine()) {
            _message = _input.nextLine();
            if (_message.equals("")) {
                printMessageLine(_message);
            } else if (_message.contains("*")) {
                _message2 = _message;
                inputLine1 = _message2.split(" ");
                if (inputLine1[0].charAt(0) != '*') {
                    throw error("There is no configuration");
                } else if (_message2.contains("(")) {
                    int firstBracket = _message2.indexOf("(");
                    _plug = _message2.substring(firstBracket);
                    _message2 = _message2.substring(0, firstBracket);
                    inputLine1 = _message2.split(" ");
                    if (inputLine1.length - 2 != _configRotors) {
                        throw error("Incorrect number of rotors specified");
                    } else {
                        _inputRotor = new String[inputLine1.length - 2];
                        for (int p = 1; p < inputLine1.length - 1; p++) {
                            _inputRotor[p - 1] = inputLine1[p];
                        }
                        machine1.insertRotors(_inputRotor);
                        setUp(machine1, inputLine1[inputLine1.length - 1]);
                        Permutation perm2 = new Permutation(_plug, _alphabet);
                        machine1.setPlugboard(perm2);
                    }
                }
                } else {
                    _toPrint = machine1.convert(_message.toUpperCase());
                    printMessageLine(_toPrint);
                }
            }
        }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _charAlphabet = _config.next();
            _configRotors = _config.nextInt();
            _configPawls = _config.nextInt();
            Collection<Rotor> allRoto = new ArrayList<>();
            rotorLengths();
            for (String each : _everyRotor) {
                allRoto.add(readRotor(each));
            }
            _alphabet = new Alphabet(_charAlphabet);
            return new Machine(_alphabet, _configRotors, _configPawls, allRoto);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Makes an array that tells us how many lines rotor permutations span. */
    private void rotorLengths() {
        _config.nextLine();
        _rotorLines = new ArrayList<>();
        _everyRotor = new ArrayList<>();
        while (_config.hasNextLine()) {
            _rotorLines.add(_config.nextLine());
        }
        int j = 0;
        for (int i = 0; i < _rotorLines.size(); i += 1) {
            Scanner M = new Scanner(_rotorLines.get(i));
            if (M.next().charAt(0) != '(') {
                _everyRotor.add(_rotorLines.get(i));
            } else {
                j += 1;
                String permAdd = _rotorLines.get(i);
                String before  = _rotorLines.get(i-1);
                String newString = before + permAdd;
                _everyRotor.set(i-j, newString);
            }
        }
    }

    /** Return a rotor, reading its description from _config.
     * LINENUM and CURRENTLINE are parameters. */
    private Rotor readRotor(String rotorDescription) {
        try {
            aRotorNotches = "";
            ArrayList<String> aRotor = new ArrayList<>();
            Scanner rotorRead = new Scanner(rotorDescription);
            aRotor.add(rotorRead.next());
            aRotor.add(rotorRead.next());
            int bracketOne = rotorDescription.indexOf("(");
            String rotorPerm = rotorDescription.substring(bracketOne);
            if (aRotor.get(1).length() > 1) {
                int k;
                for (k = 1; k < aRotor.get(1).length(); k++) {
                    aRotorNotches += aRotor.get(1).charAt(k);
                }
            }
            String rotorName = aRotor.get(0);
            Alphabet rotorAlpha = new Alphabet(_charAlphabet);
            Permutation aRotorPerm = new Permutation(rotorPerm, rotorAlpha);
            if (((aRotor.get(1)).charAt(0)) == 'M') {
                Rotor M = new MovingRotor(rotorName, aRotorPerm, aRotorNotches);
                return M;
            } else if (((aRotor.get(1)).charAt(0)) == 'N') {
                Rotor M = new FixedRotor(rotorName, aRotorPerm);
                return M;
            } else if (((aRotor.get(1)).charAt(0)) == 'R') {
                Rotor M = new Reflector(rotorName, aRotorPerm);
                return M;
            } else {
                throw error("bad rotor description");
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        M.setRotors(settings);
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        int a;
        Scanner A = new Scanner(msg);
        String toPrint = "";
        while (A.hasNext()) {
            toPrint += A.next();
        }
        for (a = 0; a < toPrint.length(); a++) {
            if (a % 6 == 0) {
                msg = msg.substring(0, a) + " " + msg.substring(a);
            }
        }
        String outputMessage = msg.trim();
        _output.println(outputMessage);
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** String of alphabet used in the machine. */
    private String _charAlphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /**Number of rotors from configuration file. */
    private int _configRotors;

    /**Number of pawls from configuration file. */
    private int _configPawls;

    /**Notches for a Rotor. */
    private String aRotorNotches;

    /** Int Array describing number of rotors and number of lines they span. */
    private ArrayList<String> _rotorLines;

    /** Array of each rotor String Line */
    private ArrayList<String> _everyRotor;

    /**Which line am I on in config. */
    private int _line;

    /**First input line. */
    private String[] inputLine1;

    /**Plugboard permutation. */
    private String _plug;

    /**String of Rotors from input file. */
    private String[] _inputRotor;

    /**Temporary message to be encoded/decoded. */
    private String _message;

    /**A message within a messages' string. */
    private String _message2;

    /**Message that is actually getting encoded/decoded. */
    private String _toPrint;



}
