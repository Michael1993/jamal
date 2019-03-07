package javax0.jamal.tools;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static javax0.jamal.tools.FileTools.absolute;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestFileTools {

    @Test
    @DisplayName("returns the file name when it is absolute starting with ~")
    void testAbsoluteFileNameStartingWithTilde() {
        String result = absolute("/refer ence/notUsed", "~absolute");
        assertEquals("~absolute", result);
    }

    @Test
    @DisplayName("returns the file name when it is absolute starting with /")
    void testAbsoluteFileNameStartingWithSlash() {
        String result = absolute("/refer ence/notUsed", "/absolute");
        assertEquals("/absolute", result);
    }

    @Test
    @DisplayName("returns the file name when it is absolute starting with backslash ")
    void testAbsoluteFileNameStartingWithBackSlash() {
        String result = absolute("/refer ence/notUsed", "\\absolute");
        assertEquals("\\absolute", result);
    }

    @Test
    @DisplayName("returns the file name when it is absolute starting with c: ")
    void testAbsoluteFileNameStartingWithDriveLetter() {
        String result = absolute("/refer ence/notUsed", "c:absolute");
        assertEquals("c:absolute", result);
    }

    @Test
    @DisplayName("returns the calculated file name when it is simple concatenation")
    void testSimpleRelativeFileName() {
        String result = absolute("a/", "b");
        assertEquals("a/b", result);
    }

    @Test
    @DisplayName("returns the calculated file name when reference contains \\")
    void testWindowsStyleFilesAreHandled() {
        String result = absolute("a\\", "b");
        assertEquals("a/b", result);
    }

    @Test
    @DisplayName("returns the calculated file name when relative file name starts with ./")
    void testSimpleRelativeFileNameWithDotSlash() {
        String result = absolute("a/", "./b");
        assertEquals("a/b", result);
    }

    @Test
    @DisplayName("returns the calculated file name when it is simple concatenation with file name in reference")
    void testSimpleRelativeFileNameWithFileNameReference() {
        String result = absolute("a/z", "b");
        assertEquals("a/b", result);
    }


    @Test
    @DisplayName("returns the calculated file name when it is simple concatenation with file name only in reference")
    void testSimpleRelativeFileNameWithFileNameOnlyReference() {
        String result = absolute("a", "b");
        assertEquals("b", result);
    }

    @Test
    @DisplayName("returns the calculated file name in complex case, reference is directory")
    void testComplexRelativeFileNameWithReference() {
        String result = absolute("a/b/c/d/", "e/f/g");
        assertEquals("a/b/c/d/e/f/g", result);
    }

    @Test
    @DisplayName("returns the calculated file name in complex case, reference is file")
    void testComplexRelativeFileNameWithFileNameReference() {
        String result = absolute("a/b/c/d/z", "e/f/g");
        assertEquals("a/b/c/d/e/f/g", result);
    }

    @Test
    @DisplayName("one .. in file name")
    void testComplexRelativeFileNameWithFileNameReferenceWalkUpInFileName() {
        String result = absolute("a/b/c/d/z", "../e/f/g");
        assertEquals("a/b/c/e/f/g", result);
    }

    @Test
    @DisplayName("multiple .. in file name")
    void testComplexRelativeFileNameWithFileNameReferenceWalkUpTwiceInFileName() {
        String result = absolute("a/b/c/d/z", "../../e/f/g");
        assertEquals("a/b/e/f/g", result);
    }

    @Test
    @DisplayName("/./ in file name")
    void testComplexRelativeFileNameWithFileNameReferenceWalkUpTwiceInFileNameMultipleDots() {
        String result = absolute("a/b/c/d/z", "../../e/./f/g");
        assertEquals("a/b/e/f/g", result);
    }

    @Test
    @DisplayName(".. off the reference structure")
    void testComplexRelativeFileNameWithTooManyDotDots() {
        String result = absolute("a/b/c/d/z", "../../../../../e/f/g");
        assertEquals("../e/f/g", result);
    }
}
