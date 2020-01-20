package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;
import java.util.Collection;
import java.util.ArrayList;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Dhivyaa N Mailvaganam
 */

public class MachineTests {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTS ***** */

    @Test
    public void testMachine1() {
        String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Alphabet alphabet = new Alphabet(alpha);

        String cycleB = "(AE)(BN)(CK)(DQ)(FU)(GY)(HW)(IJ)(LO)(MP)(RX)(SZ)(TV)";
        String cycleBeta = "(ALBEVFCYODJWUGNMQTZSKPR) (HIX)";
        String cycle1 = "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)";
        String cycle2 = "(FIXVYOMW) (CDKLHUP) (ESZ) (BJ) (GR) (NT) (A) (Q)";
        String cycle3 = "(ABDHPEJT) (CFLVMZOYQIRWUKXSG) (N)";


        Permutation permB = new Permutation(cycleB, alphabet);
        Permutation permBeta = new Permutation(cycleBeta, alphabet);
        Permutation perm1 = new Permutation(cycle1, alphabet);
        Permutation perm2 = new Permutation(cycle2, alphabet);
        Permutation perm3 = new Permutation(cycle3, alphabet);
        Permutation plugboard1 = new Permutation("", alphabet);

        Rotor B = new Reflector("B", permB);
        Rotor beta = new FixedRotor("BETA", permBeta);
        Rotor one = new MovingRotor("I", perm1, "Q");
        Rotor two = new MovingRotor("II", perm2, "E");
        Rotor three = new MovingRotor("III", perm3, "V");

        Collection<Rotor> rotorCollection = new ArrayList<Rotor>();
        rotorCollection.add(B);
        rotorCollection.add(beta);
        rotorCollection.add(one);
        rotorCollection.add(two);
        rotorCollection.add(three);

        Machine testMachine1 = new Machine(alphabet, 5, 4, rotorCollection);
        String [] allRotors = new String[]{"B", "BETA", "I", "II", "III"};
        testMachine1.insertRotors(allRotors);
        testMachine1.setRotors("AAAA");
        testMachine1.setPlugboard(plugboard1);

        assertEquals(testMachine1.convert("HELLO WORLD"), "ILBDA AMTAZ");
    }

    @Test
    public void testMachine2() {
        String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Alphabet alphabet = new Alphabet(alpha);

        String cycleB = "(AE)(BN)(CK)(DQ)(FU)(GY)(HW)(IJ)(LO)(MP)(RX)(SZ)(TV)";
        String cycleBeta = "(ALBEVFCYODJWUGNMQTZSKPR) (HIX)";
        String cycle1 = "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)";
        String cycle2 = "(FIXVYOMW) (CDKLHUP) (ESZ) (BJ) (GR) (NT) (A) (Q)";
        String cycle3 = "(ABDHPEJT) (CFLVMZOYQIRWUKXSG) (N)";

        Permutation permB = new Permutation(cycleB, alphabet);
        Permutation permBeta = new Permutation(cycleBeta, alphabet);
        Permutation perm1 = new Permutation(cycle1, alphabet);
        Permutation perm2 = new Permutation(cycle2, alphabet);
        Permutation perm3 = new Permutation(cycle3, alphabet);
        Permutation plugboard2 = new Permutation("(AQ)(EP)", alphabet);

        Rotor B = new Reflector("B", permB);
        Rotor beta = new FixedRotor("BETA", permBeta);
        Rotor one = new MovingRotor("I", perm1, "Q");
        Rotor two = new MovingRotor("II", perm2, "E");
        Rotor three = new MovingRotor("III", perm3, "V");

        Collection<Rotor> rotorCollection = new ArrayList<Rotor>();
        rotorCollection.add(B);
        rotorCollection.add(beta);
        rotorCollection.add(one);
        rotorCollection.add(two);
        rotorCollection.add(three);

        Machine testMachine2 = new Machine(alphabet, 5, 4, rotorCollection);
        String [] allRotors = new String[]{"B", "BETA", "I", "II", "III"};
        testMachine2.insertRotors(allRotors);
        testMachine2.setRotors("AAAA");
        testMachine2.setPlugboard(plugboard2);

        assertEquals(testMachine2.convert("HELLO WORLD"), "IHBDQ QMTQZ");
    }
}
