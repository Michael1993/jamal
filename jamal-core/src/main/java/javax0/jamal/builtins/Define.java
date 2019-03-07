package javax0.jamal.builtins;

import javax0.jamal.api.*;

import static javax0.jamal.tools.InputHandler.*;

public class Define implements Macro {
    @Override
    public String evaluate(Input input, Processor processor) throws BadSyntax {
        skipWhiteSpaces(input);
        boolean optional = input.length() > 0 && input.charAt(0) == '?';
        if (optional) {
            skip(input, 1);
            skipWhiteSpaces(input);
        }
        String id = fetchId(input);
        if (optional && processor.isDefined(id)) {
            return "";
        }
        skipWhiteSpaces(input);
        final String[] params = getParameters(input, id);
        if (!firstCharIs(input, '=')) {
            throw new BadSyntax("define '" + id + "' has no '=' to body");
        }
        skip(input, 1);
        if (isGlobalMacro(id)) {
            UserDefinedMacro macro = processor.newUserDefinedMacro(convertGlobal(id), input.toString(), params);
            processor.defineGlobal(macro);
        } else {
            UserDefinedMacro macro = processor.newUserDefinedMacro(id, input.toString(), params);
            processor.define(macro);
        }
        return "";
    }
}
