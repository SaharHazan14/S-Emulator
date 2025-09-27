package components.engine;

import dtos.DebugDetails;
import dtos.ExecutionDetails;
import dtos.ProgramDetails;
import dtos.RunHistoryDetails;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public interface Engine extends Serializable {
    void loadProgramFromFile(File file);
    boolean isProgramLoaded();
    ProgramDetails getProgramDetails();
    int getProgramMaxDegree();
    ProgramDetails expandProgram(int expansionDegree);
    ExecutionDetails runProgram(int expansionDegree, Long... input);
    List<RunHistoryDetails> getStatistics();
    boolean isRunning();
    DebugDetails debugProgram(int expansionDegree, Long... input);
    DebugDetails debugStepForward();
    DebugDetails debugResume();
    public void setCurrentProgram(String programName);
}
