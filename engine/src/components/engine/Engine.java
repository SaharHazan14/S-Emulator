package components.engine;

import dtos.ProgramDetails;

import java.io.File;

public interface Engine {
    void loadProgramFromFile(File file);
    boolean isProgramLoaded();
    ProgramDetails getProgramDetails();
    ProgramDetails getExpandedProgramDetails(int degree);
    int getMaxDegree();
}