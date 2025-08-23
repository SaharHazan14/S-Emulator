package components.engine;

import dtos.ProgramDetails;
import dtos.RunHistoryDetails;

import java.io.File;
import java.util.List;

public interface Engine {
    void loadProgramFromFile(File file);
    boolean isProgramLoaded();
    ProgramDetails getProgramDetails();
    ProgramDetails getExpandedProgramDetails(int degree);
    int getMaxDegree();
    //show history
    RunHistoryDetails runProgram(int degree, List<Long> inputs);
    List<RunHistoryDetails> getRunHistory();
}