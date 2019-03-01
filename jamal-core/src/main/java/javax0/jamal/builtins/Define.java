package javax0.jamal.builtins;

import javax0.jamal.api.*;

import static javax0.jamal.tools.InputHandler.*;

public class Define implements Macro {
    @Override
    public String evaluate(Input input, Processor processor) throws BadSyntax, BadSyntaxAt {
        skipWhiteSpaces(input);
        var optional = input.length() > 0 && input.charAt(0) == '?';
        if (optional) {
            skip(input, 1);
            skipWhiteSpaces(input);
        }
        var id = fetchId(input);
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
            var macro = processor.newUserDefinedMacro(convertGlobal(id), input.toString(), params);
            processor.defineGlobal(macro);
        } else {
            var macro = processor.newUserDefinedMacro(id, input.toString(), params);
            processor.define(macro);
        }
        return "";
    }
}
