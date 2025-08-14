package components.label;

public class StandardLabel implements Label {
    private final String label;

    public StandardLabel(int serialNumber) {
        label = "L" + serialNumber;
    }

    @Override
    public String getStringLabel() {
        return label;
    }
}
