package javax0.jamal.engine.macro;

import javax0.jamal.api.*;

import java.util.*;
import java.util.function.Consumer;

public class MacroRegister implements javax0.jamal.api.MacroRegister {
    private final List<Map<String, UserDefinedMacro>> udMacroStack = new ArrayList<>();
    private final List<Map<String, Macro>> macroStack = new ArrayList<>();
    private final List<Delimiters> delimiters = new ArrayList<>();
    private final List<List<Delimiters>> savedDelimiters = new ArrayList<>();
    private final Deque<Object> stackCheckObjects = new LinkedList<>();

    public MacroRegister() {
        push(null);
    }

    public Optional<UserDefinedMacro> getUserMacro(String id) {
        for (int level = udMacroStack.size() - 1; level > -1; level--) {
            var map = udMacroStack.get(level);
            if (map.containsKey(id)) {
                return Optional.of(map.get(id));
            }
        }
        return Optional.empty();
    }

    public Optional<Macro> getMacro(String id) {
        for (int level = macroStack.size() - 1; level > -1; level--) {
            var map = macroStack.get(level);
            if (map.containsKey(id)) {
                return Optional.of(map.get(id));
            }
        }
        return Optional.empty();
    }

    @Override
    public void global(UserDefinedMacro macro) {
        udMacroStack.get(0).put(macro.getId(), macro);
    }

    @Override
    public void global(Macro macro) {
        macroStack.get(0).put(macro.getId(), macro);
    }

    @Override
    public void define(UserDefinedMacro macro) {
        udMacroStack.get(udMacroStack.size() - 1).put(macro.getId(), macro);
    }

    @Override
    public void define(Macro macro) {
        macroStack.get(macroStack.size() - 1).put(macro.getId(), macro);
    }

    @Override
    public void export(String id) throws BadSyntax {
        if (udMacroStack.size() > 1) {
            var macro = udMacroStack.get(udMacroStack.size() - 1).get(id);
            if (macro == null) {
                throw new BadSyntax("Macro '" + id + "' cannot be exported");
            }
            udMacroStack.get(udMacroStack.size() - 2).put(id, macro);
            udMacroStack.get(udMacroStack.size() - 1).remove(id);
        } else {
            throw new BadSyntax("Macro '" + id + "' cannot be exported from the top level");
        }
    }

    private void stack(Macro macro, Consumer<Stackable> c) {
        if (macro instanceof Stackable) {
            c.accept((Stackable) macro);
        }
    }

    @Override
    public void push(Marker check) {
        stackCheckObjects.addLast(check);
        macroStack.forEach(macros -> macros.values().forEach(macro -> stack(macro, Stackable::push)));
        macroStack.add(new HashMap<>());
        udMacroStack.add(new HashMap<>());
        delimiters.add(new javax0.jamal.engine.Delimiters());
        savedDelimiters.add(new ArrayList<>());
    }

    @Override
    public void pop(Marker check) throws BadSyntax {
        if (!Objects.equals(check, stackCheckObjects.getLast())) {
            throw new BadSyntax("Pop was performed by " +
                    check +
                    " for a level pushed by " +
                    stackCheckObjects.getLast());
        }
        stackCheckObjects.removeLast();
        macroStack.remove(macroStack.size() - 1);
        macroStack.forEach(macros -> macros.values().forEach(macro -> stack(macro, Stackable::pop)));
        udMacroStack.remove(udMacroStack.size() - 1);
        delimiters.remove(delimiters.size() - 1);
        savedDelimiters.remove(savedDelimiters.size() - 1);
    }


    @Override
    public String open() {
        for (int level = delimiters.size() - 1; level > -1; level--) {
            var delim = delimiters.get(level);
            if (delim.open() != null) {
                return delim.open();
            }
        }
        return null;
    }

    @Override
    public String close() {
        for (int level = delimiters.size() - 1; level > -1; level--) {
            var delim = delimiters.get(level);
            if (delim.close() != null) {
                return delim.close();
            }
        }
        return null;
    }

    @Override
    public void separators(String openDelimiter, String closeDelimiter) throws BadSyntax {
        if (openDelimiter == null) {
            var delim = delimiters.get(delimiters.size() - 1);
            var list = savedDelimiters.get(savedDelimiters.size() - 1);
            if (list.size() == 0) {
                throw new BadSyntax("There was no saved macro start and end string to restore.");
            }
            var savedDelim = list.remove(list.size() - 1);
            delim.separators(savedDelim.open(), savedDelim.close());
        } else {
            var delim = delimiters.get(delimiters.size() - 1);
            var list = savedDelimiters.get(savedDelimiters.size() - 1);
            var savedDelim = new javax0.jamal.engine.Delimiters();
            savedDelim.separators(delim.open(), delim.close());
            list.add(savedDelim);
            delim.separators(openDelimiter, closeDelimiter);
        }
    }
}
