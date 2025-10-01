package application.theme;

public enum AppTheme {
    LIGHT_GREEN("/resources/css/light_green.css"),
    BLUE_DARK("/resources/css/blue_dark.css"),
    PURPLE_MINIMAL("/resources/css/purple_minimal.css"),;

    private final String path;

    AppTheme(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
