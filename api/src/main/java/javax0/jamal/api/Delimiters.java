package javax0.jamal.api;

/**
 * Simple macro delimiter string storage interface.
 *
 * The implementing classes store the actual macro open and macro close strings and provide a means to
 * alter the actual values.
 */
public interface Delimiters {
    /**
     *
     * @return the macro opening string.
     */
    String open();

    /**
     *
     * @return the macro closing string
     */
    String close();

    /**
     * Sets the opening and closing delimiter strings.
     *
     * @param openDelimiter the macro opening string to be set. If this parameter is {@code null} then
     *                      the implementation may treat this information as a restore process. For example
     *                      the class {@link MacroRegister} saves the old values of the separators in a stack
     *                      and when {@code openDelimiter} is {@code null} it restores the delimiters from the
     *                      top of the stack.
     * @param closeDelimiter the macro closing string to be set
     * @throws BadSyntax in case the separators can not be set.
     */
    void separators(String openDelimiter, String closeDelimiter) throws BadSyntax;
}
