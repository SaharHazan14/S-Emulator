package components.variable;

import java.util.concurrent.atomic.AtomicInteger;

public class VariableFactory {
    private static final AtomicInteger workVariableCounter = new AtomicInteger(1);

    /**
     * Creates a new, unique work variable (z1, z2, ...).
     * @return A new Variable of type WORK.
     */
    public static Variable createNewWorkVariable() {
        return new StandardVariable(StandardVariable.VariableType.WORK, workVariableCounter.getAndIncrement());
    }
}