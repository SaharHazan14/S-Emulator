package interactive;

import components.engine.Engine;
import components.engine.StandardEngine;
import dtos.ProgramDetails;
import dtos.RunHistoryDetails;

import java.io.File;
import java.util.List;

public class LogicManager implements Runnable {
    private static final Engine engine = new StandardEngine();

    @Override
    public void run() {
        boolean exit = false;

        while (!exit) {
            ConsoleManager.Command command = ConsoleManager.getCommandFromUser();

            switch (command) {
                case READ_XML_FILE -> {
                    File file = ConsoleManager.readXmlFile();
                    try {
                        engine.loadProgramFromFile(file);
                        System.out.println("File " + file.getName() + " was successfully loaded." + System.lineSeparator());
                    } catch (RuntimeException e) {
                        System.out.println("An error occurred: " + e.getMessage() + ", file was not loaded." + System.lineSeparator());
                    }
                }
                case SHOW_PROGRAM -> {
                    if (engine.isProgramLoaded()) {
                        ConsoleManager.showProgram(engine.getProgramDetails());
                    } else {
                        System.out.println("There is no loaded program in the system." + System.lineSeparator());
                    }
                }
                case EXPAND_PROGRAM -> {
                    if (engine.isProgramLoaded()) {
                        ProgramDetails originalDetails = engine.getProgramDetails();
                        int maxDegree = engine.getMaxDegree();
                        int degree = ConsoleManager.getExpansionDegreeFromUser(maxDegree);

                        if (degree > 0) {
                            ProgramDetails expandedDetails = engine.getExpandedProgramDetails(degree);
                            // Use the comparison view for a clean, grouped output
                            ConsoleManager.showExpandedProgramComparison(originalDetails, expandedDetails);
                        } else {
                            System.out.println("Expansion degree 0 selected. Showing original program.");
                            ConsoleManager.showProgram(originalDetails);
                        }
                    } else {
                        System.out.println("There is no loaded program in the system." + System.lineSeparator());
                    }
                }
                case RUN_PROGRAM -> {
                    if (engine.isProgramLoaded()) {
                        int degree = ConsoleManager.getExpansionDegreeFromUser(engine.getMaxDegree());
                        List<Long> inputs = ConsoleManager.getInputsFromUser(engine.getProgramDetails().inputsVariables());
                        RunHistoryDetails results = engine.runProgram(degree, inputs);
                        ConsoleManager.showRunResults(results);
                    } else {
                        System.out.println("There is no loaded program in the system." + System.lineSeparator());
                    }
                }
                case SHOW_STATISTICS -> {
                    if (engine.isProgramLoaded()) {
                        ConsoleManager.showStatistics(engine.getRunHistory());
                    } else {
                        System.out.println("There is no loaded program in the system." + System.lineSeparator());
                    }
                }
                case EXIT -> {
                    exit = true;
                }
                default -> {
                    // The default case in getCommandFromUser handles invalid number inputs.
                    // This case is for any other unexpected command enum.
                    System.out.println("Invalid command. Please try again.");
                }
            }
        }
    }
}
