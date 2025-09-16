package components.label;

import dtos.LabelDetails;

public enum FixedLabel implements Label {

    EXIT {
        @Override
        public String getStringLabel() {
            return "EXIT";
        }

        @Override
        public int getSerialNumber() {
            return 100;
        }

        @Override
        public LabelDetails getLabelDetails() {
            return new LabelDetails("EXIT");
        }
    },

    EMPTY {
        @Override
        public String getStringLabel() {
            return "";
        }

        @Override
        public int getSerialNumber() {
            return 0;
        }

        @Override
        public LabelDetails getLabelDetails() {
            return new LabelDetails("");
        }
    };

    @Override
    public abstract String getStringLabel();

    @Override
    public abstract int getSerialNumber();

    @Override
    public abstract LabelDetails getLabelDetails();

}
