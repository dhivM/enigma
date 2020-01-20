package enigma;

import java.util.HashMap;
import java.util.Collection;
import java.util.Scanner;
import java.util.ArrayList;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Dhivyaa N Mailvaganam
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _pawls = pawls;
        _allRotors = allRotors;
        _numRotors = numRotors;
        _rotorMapping = new HashMap<>();
        _rotorArray = new Rotor [_numRotors];
        for (Rotor i : allRotors) {
            _rotorMapping.put(i.name(), i);
        }
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
        for (i = 0; i < rotors.length; i++) {
            _rotorArray[i] = _rotorMapping.get(rotors[i]);
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        int j;
        for (j = 1; j < _rotorArray.length; j++) {
            _rotorArray[j].set(setting.charAt(j - 1));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        boolean[] toAdvance = new boolean[numRotors()];
        toAdvance[numRotors() - 1] = true;
        int m;
        int o;
        for (m = 2; m != numRotors(); m++) {
            if ((_rotorArray[m].atNotch()) && _rotorArray[m - 1].rotates()) {
                toAdvance[m - 1] = true;
            }
        }
        for (o = 2; o != numRotors(); o++) {
            if ((_rotorArray[o].atNotch()) && (toAdvance[o - 1])) {
                if (_rotorArray[o].rotates()) {
                    toAdvance[o] = true;
                }
            }
        }
        int n;
        for (n = 0; n < numRotors(); n++) {
            if (toAdvance[n]) {
                _rotorArray[n].advance();
            }
        }
        output = _plugboard.permute(c);


        for (m = _rotorArray.length - 1; m > -1; m -= 1) {
            output = _rotorArray[m].convertForward(output);
        }

        for (m = 1; m < _rotorArray.length; m += 1) {
            output = _rotorArray[m].convertBackward(output);
        }

        output = _plugboard.invert(output);

        return output;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        int k;
        int m = 0;
        Scanner S = new Scanner(msg);
        ArrayList<String> inputMessage = new ArrayList<>();
        ArrayList<String> outputMessage = new ArrayList<>();
        int i = 0;
        while (S.hasNext()) {
            inputMessage.add(i, S.next());
            i += 1;
        }
        for (String j : inputMessage) {
            String word = "";
            int numToConvert = j.length();
            for (k = 0; k < numToConvert; k++) {
                word += _alphabet.toChar(convert(_alphabet.toInt(j.charAt(k))));
            }
            outputMessage.add(m, word);
            m += 1;
        }
        int n;
        String outputWord = "";
        for (n = 0; n < outputMessage.size() - 1; n++) {
            outputWord = outputWord + outputMessage.get(n) + " ";
        }
        return outputWord + outputMessage.get(outputMessage.size() - 1);
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /**Number of pawls. */
    private int _pawls;

    /**Number of rotors.*/
    private int _numRotors;

    /** Collection of all the rotors in my Machine. */
    private Collection _allRotors;

    /**A Hashmap of rotor names and rotor objects. */
    private HashMap<String, Rotor> _rotorMapping;

    /**An array of rotors in my machine. */
    private Rotor[] _rotorArray;

    /**The permutation of the plugboard. */
    private Permutation _plugboard;

    /**The resultant encoding and decoding of a message. */
    private int output;
}
