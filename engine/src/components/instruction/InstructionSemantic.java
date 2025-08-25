package components.instruction;

public enum InstructionSemantic {
    INCREASE("Increase", "y <- y + 1", 1, 0, 'B'),
    DECREASE("Decrease", "y <- y - 1", 1, 0, 'B'),
    NEUTRAL("Neutral", "y <- y", 0, 0, 'B'),
    JUMP_NOT_ZERO("JumpNotZero", "IF y!=0 GOTO L", 2, 0, 'B'),
    ASSIGNMENT("Assignment", "y <- x", 4, 1, 'S'),
    GOTO("Goto", "GOTO L", 3, 1, 'S'),
    CONSTANT_ASSIGNMENT("ConstantAssignment", "y <- k", 0, 2, 'S'),
    ZERO_VARIABLE("ZeroVariable", "y <- 0", 0, 1, 'S'),
    JUMP_EQUAL_CONSTANT("JumpEqualConstant", "IF y=k GOTO L", 0, 2, 'S'),
    JUMP_EQUAL_VARIABLE("JumpEqualVariable", "IF y=x GOTO L", 0, 2, 'S'),
    JUMP_ZERO("JumpZero", "IF y=0 GOTO L", 0, 2, 'S');


    private final String name;
    private final String description;
    private final int cyclesNumber;
    private final int degree; // Add this field
    private final char instructionTypeChar;


    InstructionSemantic(String name, String description, int cyclesNumber, int degree, char instructionTypeChar) {
        this.name = name;
        this.description = description;
        this.cyclesNumber = cyclesNumber;
        this.degree = degree; // Initialize this field
        this.instructionTypeChar = instructionTypeChar;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCyclesNumber() {
        return cyclesNumber;
    }

    public int getDegree() { // Add this method
        return degree;
    }

    public char getInstructionTypeChar() {
        return instructionTypeChar;
    }
}