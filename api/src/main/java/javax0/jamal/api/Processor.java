package javax0.jamal.api;

public interface Processor {
    String process(final String in) throws BadSyntax;
    MacroRegister getRegister();
}
