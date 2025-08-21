package components.variable;

import java.util.concurrent.atomic.AtomicInteger;

public class VariableFactory {
    // Starts from a high number to avoid clashes with user-defined work variables
    private static final AtomicInteger workVariableCounter = new AtomicInteger(100);

    /**
     * Creates a new, unique work variable (z100, z101, ...).
     * @return A new Variable of type WORK.
     */
    public static Variable createNewWorkVariable() {
        return new StandardVariable(StandardVariable.VariableType.WORK, workVariableCounter.getAndIncrement());
    }
}