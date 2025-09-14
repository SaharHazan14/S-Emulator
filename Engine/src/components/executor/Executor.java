package components.executor;

public interface Executor {
    Long run(Long... input);
    Context getVariablesContext();
}
