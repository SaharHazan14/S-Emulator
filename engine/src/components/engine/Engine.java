package components.engine;

import dtos.ProgramDetails;

import java.io.File;

public interface Engine {
    void loadProgramFromFile(File file);

    ProgramDetails getProgramDetails();

    // boolean isProgramLoaded();
}
