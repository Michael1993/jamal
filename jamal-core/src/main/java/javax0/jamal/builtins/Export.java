package javax0.jamal.builtins;

import javax0.jamal.api.BadSyntax;
import javax0.jamal.api.Input;
import javax0.jamal.api.Macro;
import javax0.jamal.api.Processor;

import static javax0.jamal.tools.InputHandler.trim;

public class Export implements Macro {
    @Override
    public String evaluate(Input input, Processor processor) throws BadSyntax {
        String[] params = input.toString().split(",");
        trim(params);
        for (final String param : params) {
            processor.getRegister().export(param);
        }
        return "";
    }
}
