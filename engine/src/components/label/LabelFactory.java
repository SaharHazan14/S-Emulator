package components.label;

import java.util.concurrent.atomic.AtomicInteger;
import components.label.Label;
import components.label.StandardLabel;

public class LabelFactory {
    private static final AtomicInteger labelCounter = new AtomicInteger(1);

    public static Label createNewLabel() {
        return new StandardLabel(labelCounter.getAndIncrement());
    }
}