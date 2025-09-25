package components.function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionFactory {
    private Map<String, Function> functions;

    public FunctionFactory() {
        functions = new HashMap<>();
    }

    public Function getFunction(String functionName) {
        return functions.get(functionName);
    }

    public void addFunction(Function function) {
        functions.put(function.getName(), function);
    }

    public List<Function> getFunctions() {
        return new ArrayList<>(functions.values());
    }
}
