package interactive;

import components.instruction.Instruction;
import components.label.Label;
import components.variable.Variable;
import dtos.ProgramDetails;
import dtos.RunHistoryDetails;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ConsoleManager {
    public enum Command {
        READ_XML_FILE,
        SHOW_PROGRAM,
        EXPAND_PROGRAM,
        RUN_PROGRAM,
        SHOW_STATISTICS,
        EXIT,
        INVALID_COMMAND,
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static Command getCommandFromUser() {
        Command command;
        do {
            showMenu();
            try {
                int commandNumber = Integer.parseInt(scanner.nextLine());
                command = switch (commandNumber) {
                    case 1 -> Command.READ_XML_FILE;
                    case 2 -> Command.SHOW_PROGRAM;
                    case 3 -> Command.EXPAND_PROGRAM;
                    case 4 -> Command.RUN_PROGRAM;
                    case 5 -> Command.SHOW_STATISTICS;
                    case 6 -> Command.EXIT;
                    default -> {
                        System.out.println("Invalid input. Please enter a number from 1 to 6." + System.lineSeparator());
                        yield Command.INVALID_COMMAND;
                    }
                };
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number from 1 to 6." + System.lineSeparator());
                command = Command.INVALID_COMMAND;
            }
        } while (command == Command.INVALID_COMMAND);
        return command;
    }

    private static void showMenu() {
        System.out.println("Enter the number of the command you want to run");
        System.out.println("1. Read XML File");
        System.out.println("2. Show Program");
        System.out.println("3. Expand Program");
        System.out.println("4. Run Program");
        System.out.println("5. Show Statistics");
        System.out.println("6. Exit Program");
    }

    public static File readXmlFile() {
        boolean validFile = false;
        File file;
        do {
            System.out.print("Enter path to your xml file: ");
            String path = scanner.nextLine();
            file = new File(path);
            if (!path.endsWith(".xml")) {
                System.out.println("Invalid path. Please enter a xml file." + System.lineSeparator());
            } else if (!file.exists()) {
                System.out.println("File does not exist. Please enter a valid path." + System.lineSeparator());
            } else if (!file.isFile()) {
                System.out.println("Invalid path. Please enter a valid path." + System.lineSeparator());
            } else {
                validFile = true;
            }
        } while (!validFile);
        return file;
    }

    public static void showProgram(ProgramDetails programDetails) {
        showProgram(programDetails, false);
    }

    public static void showProgram(ProgramDetails programDetails, boolean showExpansionHistory) {
        System.out.println("Program name: " + programDetails.name());
        System.out.print("Variables:");
        for (Variable variable : programDetails.inputsVariables()) {
            System.out.print(" " + variable.getStringVariable());
        }
        System.out.println();
        System.out.print("Labels:");
        for (Label label : programDetails.labels()) {
            System.out.print(" " + label.getStringLabel());
        }
        System.out.println();
        int i = 1;
        System.out.println("Instructions:");
        for (Instruction instruction : programDetails.instructions()) {
            StringBuilder line = new StringBuilder();
            line.append("#").append(i).append(" ").append(instruction.getStringInstruction());
            if (showExpansionHistory) {
                Instruction original = instruction.getOriginalInstruction();
                while (original != null) {
                    line.append(" <<< ").append(original.getStringInstruction());
                    original = original.getOriginalInstruction();
                }
            }
            System.out.println(line.toString());
            i++;
        }
        System.out.println();
    }

    /**
     * Shows a clean, grouped comparison of an expansion.
     * This is the new method that provides the clean output you want.
     */
    public static void showExpandedProgramComparison(ProgramDetails originalDetails, ProgramDetails expandedDetails) {
        System.out.println("Program name: " + expandedDetails.name());
        System.out.println("Expansion Result:");
        System.out.println("=========================================");

        int expandedInstructionCounter = 1;
        // Loop through the ORIGINAL instructions to create groups
        for (Instruction originalInstruction : originalDetails.instructions()) {

            // Find all instructions in the expanded list that came from this original instruction
            List<Instruction> children = expandedDetails.instructions().stream()
                    .filter(exp -> exp.getUltimateOriginalInstruction() == originalInstruction)
                    .collect(Collectors.toList());

            if (children.isEmpty()) {
                // This was a basic instruction that wasn't expanded. We print it cleanly.
                System.out.println("#" + expandedInstructionCounter++ + " " + originalInstruction.getStringInstruction());
            } else {
                // This was a synthetic instruction. Print its children.
                for (Instruction child : children) {
                    StringBuilder line = new StringBuilder();
                    line.append("#").append(expandedInstructionCounter++).append(" ");
                    line.append(child.getStringInstruction());

                    // Build the history chain
                    Instruction parent = child; // Start with the child itself
                    while (parent.getOriginalInstruction() != null) {
                        parent = parent.getOriginalInstruction();
                        line.append(" <<< ").append(parent.getStringInstruction());
                    }
                    System.out.println(line.toString());
                }
            }
        }
        System.out.println("=========================================\n");
    }


    public static int getExpansionDegreeFromUser(int maxDegree) {
        int degree = -1;
        if (maxDegree == 0) {
            System.out.println("This program contains only basic instructions and cannot be expanded.");
            return 0;
        }
        do {
            System.out.println("The maximum expansion degree is " + maxDegree);
            System.out.print("Please enter the desired expansion degree (from 0 to " + maxDegree + "): ");
            try {
                degree = Integer.parseInt(scanner.nextLine());
                if (degree < 0 || degree > maxDegree) {
                    System.out.println("Invalid degree. Please enter a number between 0 and " + maxDegree + "." + System.lineSeparator());
                    degree = -1;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number." + System.lineSeparator());
            }
        } while (degree == -1);
        return degree;
    }

    public static List<Long> getInputsFromUser(List<Variable> requiredInputs) {
        System.out.println("Please provide inputs for the program.");
        String required = requiredInputs.stream().map(Variable::getStringVariable).collect(Collectors.joining(", "));
        System.out.println("Required inputs are for: " + required);
        System.out.print("Enter comma-separated numbers (e.g., 5, 10, 15): ");

        List<Long> inputs = new ArrayList<>();
        String line = scanner.nextLine();
        try {
            if (!line.trim().isEmpty()) {
                String[] numbers = line.split(",");
                for (String numStr : numbers) {
                    inputs.add(Long.parseLong(numStr.trim()));
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter only numbers separated by commas. Treating inputs as empty.");
            return new ArrayList<>();
        }
        return inputs;
    }

    public static void showRunResults(RunHistoryDetails results) {
        System.out.println("\n--- Run Finished ---");
        System.out.println("Formal Output (y): " + results.outputY());
        System.out.println("Cycles Consumed: " + results.cyclesConsumed());
        System.out.println("--------------------\n");
    }

    public static void showStatistics(List<RunHistoryDetails> history) {
        if (history.isEmpty()) {
            System.out.println("No runs have been recorded yet for this program.");
            return;
        }

        System.out.println("\n--- Program Run History & Statistics ---");
        System.out.printf("%-10s | %-10s | %-20s | %-10s | %-10s%n", "Run #", "Degree", "Inputs", "Output (y)", "Cycles");
        System.out.println("----------------------------------------------------------------------");
        for (RunHistoryDetails details : history) {
            String inputsStr = details.inputs().stream().map(Object::toString).collect(Collectors.joining(", "));
            System.out.printf("%-10d | %-10d | %-20s | %-10d | %-10d%n",
                    details.runNumber(),
                    details.expansionDegree(),
                    inputsStr,
                    details.outputY(),
                    details.cyclesConsumed());
        }
        System.out.println("----------------------------------------------------------------------\n");
    }
}
