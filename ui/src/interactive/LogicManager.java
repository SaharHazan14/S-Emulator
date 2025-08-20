package interactive;

import components.engine.Engine;
import components.engine.StandardEngine;

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
                case EXPAND_PROGRAM -> {}
                case RUN_PROGRAM -> {}
                case SHOW_STATISTICS -> {}
                case EXIT -> {
                    exit = true;
                }
            }
        }
    }
}