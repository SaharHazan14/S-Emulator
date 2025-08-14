package components.label;

public enum FixedLabel implements Label {

    EXIT {
        @Override
        public String getStringLabel() {
            return "EXIT";
        }
    },

    EMPTY {
        @Override
        public String getStringLabel() {
            return "";
        }
    };

    @Override
    public abstract String getStringLabel();
}
