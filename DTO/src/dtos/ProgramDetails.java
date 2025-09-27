package dtos;

import java.util.List;

public record ProgramDetails(String name, List<VariableDetails> inputVariables,
                             List<VariableDetails> workVariables,
                             List<LabelDetails> labels, List<InstructionDetails> instructions,
                             int maxDegree, int basicInstructionsNumber, List<FunctionDetails> functions) {
    public ProgramDetails {
        inputVariables = List.copyOf(inputVariables);
        workVariables = List.copyOf(workVariables);
        labels = List.copyOf(labels);
        instructions = List.copyOf(instructions);
        functions = List.copyOf(functions);
    }
}