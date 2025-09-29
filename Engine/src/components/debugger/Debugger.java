package components.debugger;

import dtos.DebugDetails;

public interface Debugger {
    DebugDetails initializeDebugger(Long... inputs);
    DebugDetails stepForward();
    DebugDetails stepBackward();
    DebugDetails resume();
}
