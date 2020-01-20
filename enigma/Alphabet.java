package enigma;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Dhivyaa N Mailvaganam
 */
class Alphabet {

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        _chars = chars;
        _charsArray = new char[_chars.length()];
        int i;
        for (i = 0; i < _chars.length(); i++) {
            _charsArray[i] = _chars.charAt(i);
        }
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _charsArray.length;
    }

    /** Returns true if preprocess(CH) is in this alphabet. */
    boolean contains(char ch) {
        for (char i: _charsArray) {
            if (i == ch) {
                return true;
            }
        }
        return false;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        char j = _charsArray[index];
        return j;
    }

    /** Returns the index of character preprocess(CH), which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        int j = 0;
        int i;
        for (i = 0; i < _charsArray.length; i++) {
            if (_charsArray[i] == ch) {
                j = i;
            }
        }
        return j;
    }

    /** Characters in the Alphabet. */
    private String _chars;

    /**An array containing each character in the Alphabet.*/
    private char[] _charsArray;

}
