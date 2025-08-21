package interactive;

import components.engine.Engine;
import components.engine.StandardEngine;
import dtos.ProgramDetails;

import java.io.File;

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
                        System.out.println(e.getCause().getMessage() + ", file was not loaded." + System.lineSeparator());
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
                        // 1. קבל מהמשתמש את דרגת ההרחבה הרצויה דרך ConsoleManager
                        int degree = ConsoleManager.getExpansionDegreeFromUser(engine.getMaxDegree()); // תצטרך לממש את getMaxDegree
                        // 2. קרא למנוע כדי לקבל את פרטי התוכנית המורחבת
                        ProgramDetails expandedDetails = engine.getExpandedProgramDetails(degree);
                        // 3. הצג את התוצאה למשתמש
                        ConsoleManager.showProgram(expandedDetails); // נעביר פרמטר נוסף כדי שידע להדפיס היסטוריה
                    } else {
                        System.out.println("There is no loaded program in the system." + System.lineSeparator());
                    }
                }
                case RUN_PROGRAM -> {}
                case SHOW_STATISTICS -> {}
                case EXIT -> {
                    exit = true;
                }
            }
        }
    }
}