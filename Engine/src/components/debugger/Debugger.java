package components.debugger;

import dtos.ContextDetails;
import dtos.DebugDetails;

public interface Debugger {
    DebugDetails initializeDebugger(Long... inputs);
    DebugDetails stepForward();
    DebugDetails stepBackward();
}
