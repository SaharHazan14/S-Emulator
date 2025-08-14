package components.label;

public enum FixedLabel implements Label {

    EXIT {
        @Override
        public String getLabel() {
            return "EXIT";
        }
    },

    EMPTY {
        @Override
        public String getLabel() {
            return "";
        }
    };

    @Override
    public abstract String getLabel();
}
