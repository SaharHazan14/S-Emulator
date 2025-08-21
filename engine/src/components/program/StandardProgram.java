package components.program;

import components.instruction.Instruction;
import components.instruction.InstructionSemantic;
import components.instruction.implementations.basic.DecreaseInstruction;
import components.instruction.implementations.basic.JumpNotZeroInstruction;
import components.instruction.implementations.synthetic.ZeroVariableInstruction;
import components.jaxb.generated.SProgram;
import components.label.FixedLabel;
import components.label.Label;
import components.label.LabelFactory;
import components.variable.StandardVariable;
import components.variable.Variable;
import dtos.ProgramDetails;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StandardProgram implements Program {
    private final String name;
    private final List<Instruction> instructions;
    private boolean loaded = false;

    public StandardProgram(String name) {
        this.name = name;
        this.instructions = new ArrayList<>();
        this.loaded = true;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Variable> getInputVariables() {
        List<Variable> variables = new ArrayList<>();
        Variable currentVariable;

        for(Instruction instruction : instructions) {
            currentVariable = instruction.getVariable();

            if (currentVariable.getVariableType() == StandardVariable.VariableType.INPUT && !variables.contains(currentVariable)) {
                variables.add(currentVariable);
            }
        }

        variables.sort(Comparator.comparingInt(Variable::getSerialNumber));
        return variables;
    }

    @Override
    public List<Label> getLabels() {
        List<Label> labels = new ArrayList<>();
        Label currentLabel;

        for(Instruction instruction : instructions) {
            currentLabel = instruction.getLabel();

            if (!labels.contains(currentLabel) && currentLabel != FixedLabel.EMPTY) {
                labels.add(currentLabel);
            }
        }

        labels.sort(Comparator.comparingInt(Label::getSerialNumber));
        return labels;
    }

    @Override
    public List<Instruction> getInstructions() {
        return instructions;
    }

    @Override
    public void addInstruction(Instruction instruction) {
        instructions.add(instruction);
    }

    @Override
    public boolean validate() {
        return false;
        //
    }

    @Override
    public int calculateMaxDegree() {
        return 0;
        //
    }

    @Override
    public int calculateCyclesNumber() {
        return 0;
        //
    }



    @Override
    public Program expand(int degree) {
        if (degree <= 0) {
            return this; // אין מה להרחיב
        }

        Program expandedProgram = new StandardProgram(this.name + "_expanded_degree_" + degree);
        List<Instruction> newInstructions = new ArrayList<>();

        for (Instruction instruction : this.instructions) {
            // כאן נכנסת הלוגיקה המרכזית
            if (isSynthetic(instruction)) { // נצטרך לממש את isSynthetic
                // הוסף את רשימת הפקודות המורחבות
                newInstructions.addAll(expandInstruction(instruction));
            } else {
                // אם הפקודה בסיסית, פשוט הוסף אותה
                newInstructions.add(instruction);
            }
        }

        // עדכון רשימת הפקודות של התוכנית החדשה
        for(Instruction inst : newInstructions) {
            expandedProgram.addInstruction(inst);
        }

        // קריאה רקורסיבית להמשך הרחבה עד לדרגה הרצויה
        return expandedProgram.expand(degree - 1);
    }

    private boolean isSynthetic(Instruction instruction) {
        // הדרך הפשוטה ביותר היא לבדוק את סוג הפקודה דרך ה-enum
        // InstructionSemantic שקיים אצלכם
        return instruction.getInstructionSemantic().getInstructionType() == InstructionSemantic.InstructionType.SYNTHETIC;
    }

    private List<Instruction> expandInstruction(Instruction instruction) {
        // כאן ימומש ה"פירוק" של כל פקודה סינתטית
        // לדוגמה, עבור ZERO_VARIABLE:
        // Y <- 0  ==> [L]: Y <- Y - 1
        //            IF Y != 0 GOTO L

        List<Instruction> expanded = new ArrayList<>();

//        // דוגמה למימוש הרחבה של ZERO_VARIABLE
//        if (instruction instanceof ZeroVariableInstruction) {
//            Label loopLabel = LabelFactory.createNewLabel();
//            Variable var = instruction.getVariable();
//
//            // [L]: Y <- Y - 1
//            expanded.add(new DecreaseInstruction(var, loopLabel, instruction)); // מעבירים את הפקודה המקורית
//
//            // IF Y != 0 GOTO L
//            expanded.add(new JumpNotZeroInstruction(var, loopLabel, instruction));
//        }

        // ... כאן תממש את ההרחבה עבור כל שאר הפקודות הסינתטיות ...
        // GOTO_LABEL, ASSIGNMENT, CONSTANT_ASSIGNMENT וכו'.

        return expanded;
    }



    public ProgramDetails getExpandedProgramDetails(int degree) {
        if (!this.loaded) {
            throw new IllegalStateException("No program loaded.");
        }
        Program expandedProgram = this.expand(degree);
        return new ProgramDetails(expandedProgram.getName(), expandedProgram.getInputVariables(),
                expandedProgram.getLabels(), expandedProgram.getInstructions());
    }

}
