package enigma;

import static enigma.EnigmaException.*;
import java.util.Scanner;
import java.util.ArrayList;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author
 */
class Permutation {

    /**
     * Set this Permutation to that specified by CYCLES, a string in the
     * form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     * is interpreted as a permutation in cycle notation.  Characters in the
     * alphabet that are not included in any cycle map to themselves.
     * Whitespace is ignored.
     */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles;
        // FIXME
    }

    /**
     * Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     * c0c1...cm.
     */
    private void addCycle(String cycle) {
        // FIXME
        _cycles += cycle;
        // gotta fix the whitespace in here and possibly throw exception if anything other than
        // characters and brackets is in here, the other methods assume that cycles is the right input
        //or maybe add it in method before(??) hmm
    }

    /**
     * Return the value of P modulo the size of this permutation.
     */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /**
     * Returns the size of the alphabet I permute.
     */
    int size() {
        return _alphabet.size(); // FIXME

    }

    /**
     * Return the result of applying this permutation to P modulo the
     * alphabet size.
     */
    int permute(int p) {
        int pInt = wrap(p);
        char pChar = _alphabet.toChar(pInt);
        int outputInt = _alphabet.toInt(permute(pChar));
        return outputInt;  // FIXME
    }

    /**
     * Return the result of applying the inverse of this permutation
     * to  C modulo the alphabet size.
     */
    int invert(int c) {
        int cInt = wrap(c);
        char cChar = _alphabet.toChar(cInt);
        int outputInt = _alphabet.toInt(invert(cChar));
        return outputInt;  // FIXME
    }

    /**
     * Returns character for permute or invert
     */
    private char findChar(String a, char p, int direction) {
        ArrayList<String> cycleArray1 = new ArrayList<String>();
        ArrayList<String> cycleArray2 = new ArrayList<String>();
        String withWhitespace = a.replaceAll("\\)\\(","\\) (");
        Scanner S = new Scanner(withWhitespace);
        int i = 0;
        while (S.hasNext()) {
            cycleArray1.add(i, S.next());
            i += 1;
        }
        int k = 0;
        for (String j : cycleArray1) {
            String M = j.replaceAll("\\(", "");
            String K = M.replaceAll("\\)", "");
            cycleArray2.add(k, K);
            k += 1;
        }
        if (direction == 0) {
            for (String o : cycleArray2) {
                int lengthOneCycle = o.length();
                if (o.indexOf(p) >=0) {
                    if (o.indexOf(p) != lengthOneCycle - 1) {
                        return o.charAt(o.indexOf(p) + 1);
                    } else if (lengthOneCycle == 1) {
                        return o.charAt(o.indexOf(p));
                    } else {
                        return o.charAt(0);
                    }
                }
            }
            return p;
        } else if (direction == 1) {
            for (String o : cycleArray2) {
                int lengthOneCycle = o.length();
                if (o.indexOf(p) >= 0) {
                    if (o.indexOf(p) != 0) {
                        return o.charAt(o.indexOf(p) - 1);
                    } else {
                        return o.charAt(lengthOneCycle - 1);
                    }
                }
            }
        } return p;
    }


    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        if (_cycles == "") {
            return p;
        } else {
            return findChar(_cycles, p, 0);
        }
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        if (_cycles == "") {
            return c;
        } else {
            return findChar(_cycles, c, 1);
        }
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        String One = _cycles.replaceAll("\\s","");
        String Two = One.replaceAll("\\(","");
        String Three = Two.replaceAll("\\)","");

        if (Three.length() == _alphabet.size()) {
            return true;
        } else {
            return false;
        }

        } //FIXME

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Cycles of this permutation. */
    private String _cycles;


    // FIXME: ADDITIONAL FIELDS HERE, AS NEEDED
}
