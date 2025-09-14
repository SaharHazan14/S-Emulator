package components.label;

import dtos.LabelDetails;

import java.util.Objects;

public class StandardLabel implements Label {
    private final String label;

    public StandardLabel(int serialNumber) {
        label = "L" + serialNumber;
    }

    @Override
    public String getStringLabel() {
        return label;
    }

    @Override
    public int getSerialNumber() {
        return Integer.parseInt(label.substring(1));
    }

    @Override
    public LabelDetails getLabelDetails() {
        return new  LabelDetails(label);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StandardLabel that = (StandardLabel) o;
        return Objects.equals(label, that.label);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(label);
    }
}
