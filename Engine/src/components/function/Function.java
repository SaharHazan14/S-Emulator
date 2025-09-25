package components.function;

import components.program.StandardProgram;

public class Function extends StandardProgram {
    private final String userString;

    public Function(String name, String userString) {
        super(name);
        this.userString = userString;
    }

    public String getUserString() {
        return userString;
    }
}
