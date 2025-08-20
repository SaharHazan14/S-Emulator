package interactive;

import components.instruction.Instruction;
import components.label.Label;
import components.variable.Variable;
import dtos.ProgramDetails;

import java.io.File;
import java.util.Scanner;

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

    public static Command getCommandFromUser()
    {
        Command command;

        do {
            showMenu();

            try {
                int commandNumber = Integer.parseInt(scanner.nextLine());

                command = switch (commandNumber) {
                    case 1 -> Command.READ_XML_FILE;
                    case 2 -> Command.SHOW_PROGRAM;
                    case 3 -> Command.RUN_PROGRAM;
                    case 4 -> Command.EXPAND_PROGRAM;
                    case 5 -> Command.SHOW_STATISTICS;
                    case 6 -> Command.EXIT;
                    default -> {
                        System.out.println("Invalid input. Please enter a number from 1 to 6." +  System.lineSeparator());
                        yield Command.INVALID_COMMAND;
                    }
                };
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number from 1 to 6." +  System.lineSeparator());
                command =  Command.INVALID_COMMAND;
            }
        } while (command ==  Command.INVALID_COMMAND);

        return command;
    }

    private static void showMenu()
    {
        System.out.println("Enter the number of the command you want to run");
        System.out.println("1. Read XML File");
        System.out.println("2. Show Program");
        System.out.println("3. Expand Program");
        System.out.println("4. Run Program");
        System.out.println("5. Show Statistics");
        System.out.println("6. Exit Program");
    }

    public static File readXmlFile()
    {
        boolean validFile = false;
        File file;

        do {
            System.out.print("Enter path to your xml file: ");
            String path = scanner.nextLine();
            file = new File(path);

            if (!path.endsWith(".xml")) {
                System.out.println("Invalid path. Please enter a xml file." + System.lineSeparator());
            }
            else if (!file.exists()) {
                System.out.println("File does not exist. Please enter a valid path." + System.lineSeparator());
            }
            else if (!file.isFile()) {
                System.out.println("Invalid path. Please enter a valid path." + System.lineSeparator());
            }
            else {
                validFile = true;
            }
        } while (!validFile);

        return file;
    }

    public static void showProgram(ProgramDetails programDetails) {
        System.out.println(programDetails.name());

        for (Variable variable : programDetails.inputsVariables())
        {
            System.out.print(variable.getStringVariable() + " ");
        }
        System.out.println();

        for (Label label : programDetails.labels())
        {
            System.out.print(label.getStringLabel() + " ");
        }
        System.out.println();

        int i = 1;
        for (Instruction instruction : programDetails.instructions()) {
            System.out.println("#" + i + " " + instruction.getStringInstruction());
            i++;
        }
        System.out.println();
    }
}