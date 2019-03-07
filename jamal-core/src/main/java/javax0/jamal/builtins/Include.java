package javax0.jamal.builtins;

import javax0.jamal.api.*;
import javax0.jamal.tools.Marker;

import static javax0.jamal.tools.FileTools.absolute;
import static javax0.jamal.tools.FileTools.getInput;
import static javax0.jamal.tools.InputHandler.skipWhiteSpaces;

public class Include implements Macro {
    private int depth = 100;

    @Override
    public String evaluate(Input input, Processor processor) throws BadSyntax {
        skipWhiteSpaces(input);
        String reference = input.getReference();
        String fileName = absolute(reference, input.toString().trim());
        if (depth-- == 0) {
            throw new BadSyntax("Include depth is too deep");
        }
        Marker marker = new Marker("{@include " + fileName + "}");
        processor.getRegister().push(marker);
        String result = processor.process(getInput(fileName));
        processor.getRegister().pop(marker);
        return result;
    }
}
