package javax0.jamal.builtins;

import javax0.jamal.api.*;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Define the {@code for} looping macro. The syntax of the macro is
 *
 * <pre>
 *     {#for var in (a,b,c,d)= var is either a, b, c or d
 *     }
 * </pre>
 * <p>
 * The default separator is {@code ,} (comma), but it can be redefined assigning a value to the user defined
 * macro {@code $forsep}.
 */
public class For implements Macro {

    private static final Pattern pattern = Pattern.compile("\\s+(\\w[\\w\\d_$]*)\\s+in\\s*\\((.*?)\\)\\s*=(.*)", Pattern.DOTALL);

    @Override
    public String evaluate(Input input, Processor processor) throws BadSyntax {
        Matcher matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            throw new BadSyntax("use macro has bad syntax '" + input + "'");
        }
        final String loopVariable = matcher.group(1);
        final String values = matcher.group(2);
        final String content = matcher.group(3);
        final Optional<UserDefinedMacro> optionalForSepMacro = processor.getRegister().getUserMacro("$forsep");
        final String splitter = optionalForSepMacro.isPresent() ? optionalForSepMacro.get().evaluate() : ",";
        final String[] valueList = values.split(splitter);
        final StringBuilder output = new StringBuilder();
        final Segment root = new Segment(null, content);
        root.split(loopVariable);
        for (final String value : valueList) {
            for (Segment segment = root; segment != null; segment = segment.next()) {
                output.append(segment.content().orElse(value));
            }
        }
        return output.toString();
    }

    private static class Segment {
        Segment nextSeg;
        String text;

        Segment(Segment nextSeg, String text) {
            this.nextSeg = nextSeg;
            this.text = text;
        }

        private static void split(final Segment root, final String parameter) {
            Segment it = root;
            //noinspection StatementWithEmptyBody
            while ((it = splitAndGetNext(it, parameter)) != null) ;
        }

        private static Segment splitAndGetNext(final Segment it, final String parameter) {
            final int start = it.text.indexOf(parameter);
            if (start < 0) {
                return null;
            }
            final Segment textSeg = new Segment(it.nextSeg, it.text.substring(start + parameter.length()));
            it.nextSeg = new Segment(textSeg, null);
            it.text = it.text.substring(0, start);
            return textSeg;
        }

        Optional<String> content() {
            return Optional.ofNullable(text);
        }

        Segment next() {
            return nextSeg;
        }

        void split(String parameter) {
            split(this, parameter);
        }
    }
}
