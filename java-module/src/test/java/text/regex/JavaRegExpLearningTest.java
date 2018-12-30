package text.regex;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;
import org.junit.Test;

public class JavaRegExpLearningTest {

    /**
     * characters test
     */
    @Test
    public void simpleCharacter() {
        assertTrue(Pattern.matches("x", "x"));
    }

    @Test
    public void backSlash() {
        // to escape in regexp \\ mean \
        // to escpae java string in \ we must use double \\
        // so
        assertTrue(Pattern.matches("\\\\", "\\"));
    }

    @Test
    public void tab() {
        assertTrue(Pattern.matches("\\t", "\t"));
    }

    @Test
    public void newLine() {
        assertTrue(Pattern.matches("\\n", "\n"));
    }

    /**
     *  Tests of Character classes
     */
    @Test
    public void simpleClass() {
        assertTrue(Pattern.matches("[abc]", "a"));
        assertTrue(Pattern.matches("[abc]", "b"));
        assertTrue(Pattern.matches("[abc]", "c"));
        assertFalse(Pattern.matches("[abc]", "d"));
        assertFalse(Pattern.matches("[abc]", "abc"));
    }

}
