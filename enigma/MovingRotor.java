package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Dhivyaa N Mailvaganam
 */
class MovingRotor extends Rotor {

    /**
     * A rotor named NAME whose permutation in its default setting is
     * PERM, and whose notches are at the positions indicated in NOTCHES.
     * The Rotor is initally in its 0 setting (first character of its
     * alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _permutation = perm;
        _notches = notches;
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    void advance() {
        int newSetting = _permutation.wrap(super.setting() + 1);
        super.set(newSetting);
    }

    @Override
    boolean atNotch() {
        int i = 0;
        if (_notches.equals("")) {
            return false;
        } else {
            for (i = 0; i < _notches.length(); i++) {
                int aNotch = super.alphabet().toInt(_notches.charAt(i));
                if (super.setting() == aNotch) {
                    return true;
                }
            } return false;
        }
    }

    /** String of notches in the moving rotor. */
    private String _notches;

    /** Permutation of the moving rotor. */
    private Permutation _permutation;

}
