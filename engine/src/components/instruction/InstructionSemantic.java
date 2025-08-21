package components.instruction;

public enum InstructionSemantic {
    INCREASE("INCREASE", 1, InstructionType.BASIC),
    DECREASE("DECREASE", 1,  InstructionType.BASIC),
    JUMP_NOT_ZERO("JUMP_NOT_ZERO", 2, InstructionType.BASIC),
    NEUTRAL("NEUTRAL", 0, InstructionType.BASIC),
    ZERO_VARIABLE("ZERO_VARIABLE", 1, InstructionType.SYNTHETIC),
    GOTO_LABEL("GOTO_LABEL", 1, InstructionType.SYNTHETIC),
    ASSIGNMENT("ASSIGNMENT", 4, InstructionType.SYNTHETIC),
    CONSTANT_ASSIGNMENT("CONSTANT_ASSIGNMENT", 2, InstructionType.SYNTHETIC),
    JUMP_ZERO("JUMP_ZERO", 2, InstructionType.SYNTHETIC),
    JUMP_EQUAL_CONSTANT("JUMP_EQUAL_CONSTANT", 2, InstructionType.SYNTHETIC),
    JUMP_EQUAL_VARIABLE("JUMP_EQUAL_VARIABLE", 2, InstructionType.SYNTHETIC),
    ;

    public enum InstructionType {
        BASIC,
        SYNTHETIC,
    }
    private final String name;
    private final int cyclesNumber;
    private final InstructionType instructionType;

    InstructionSemantic(String name, int cyclesNumber, InstructionType instructionType) {
        this.name = name;
        this.cyclesNumber = cyclesNumber;
        this.instructionType = instructionType;
    }

    public String getName() {
        return name;
    }

    public int getCyclesNumber() {
        return cyclesNumber;
    }

    public InstructionType getInstructionType() {
        return instructionType;
    }

    public char getInstructionTypeChar() {
        return switch (instructionType)
        {
            case BASIC -> 'B';
            case SYNTHETIC -> 'S';
        };
    }
}
