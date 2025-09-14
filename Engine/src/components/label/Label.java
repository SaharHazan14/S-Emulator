package components.label;

import dtos.LabelDetails;

import java.io.Serializable;

public interface Label extends Serializable {
    String getStringLabel();
    int getSerialNumber();
    LabelDetails getLabelDetails();
}
