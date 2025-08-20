package interactive;

import components.engine.Engine;
import components.engine.StandardEngine;

import java.io.File;

public class LogicManager implements Runnable {
    private static final Engine engine = new StandardEngine();

    @Override
    public void run() {
        while (true) {
            ConsoleManager.Command command = ConsoleManager.getCommandFromUser();

            if (command == ConsoleManager.Command.READ_XML_FILE)
            {
                File file = ConsoleManager.readXmlFile();
                try {
                    engine.loadProgramFromFile(file);
                    System.out.println("File " + file.getName() + " was successfully loaded." + System.lineSeparator());
                } catch (RuntimeException e) {
                    System.out.println(e.getCause().getMessage() + ", file was not loaded." + System.lineSeparator());
                }
            }
            else if (command == ConsoleManager.Command.SHOW_PROGRAM)
            {

            }
        }
    }
}