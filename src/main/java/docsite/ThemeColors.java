package docsite;


import java.util.Objects;

public class ThemeColors {

    public static final ThemeColors DEFAULT = ThemeColors.builder()
        .menuRegularBackgroundColor("#575757")
        .menuBoldBackgroundColor("#413e3e")
        .menuForegroundColor("#f1f4f4")
        .menuDecorationColor("#b7d4da")
        .guiElementColor("#f08080")
        .build();

    private String menuRegularBackgroundColor;
    private String menuBoldBackgroundColor;
    private String menuForegroundColor;
    private String menuDecorationColor;
    private String guiElementColor;


    public ThemeColors() {
        //
    }


    public ThemeColors(
        String menuRegularBackgroundColor,
        String menuBoldBackgroundColor,
        String menuForegroundColor,
        String menuDecorationColor,
        String guiElementColor
    ) {
        this.menuRegularBackgroundColor = menuRegularBackgroundColor;
        this.menuBoldBackgroundColor = menuBoldBackgroundColor;
        this.menuForegroundColor = menuForegroundColor;
        this.menuDecorationColor = menuDecorationColor;
        this.guiElementColor = guiElementColor;
    }


    public static ThemeColorsBuilder builder() {
        return new ThemeColorsBuilder();
    }


    public String menuRegularBackgroundColor() {
        return Objects.requireNonNull(this.menuRegularBackgroundColor);
    }


    public String menuBoldBackgroundColor() {
        return Objects.requireNonNull(this.menuBoldBackgroundColor);
    }


    public String menuForegroundColor() {
        return Objects.requireNonNull(this.menuForegroundColor);
    }


    public String menuDecorationColor() {
        return Objects.requireNonNull(this.menuDecorationColor);
    }


    public String guiElementColor() {
        return Objects.requireNonNull(this.guiElementColor);
    }



    public ThemeColors menuRegularBackgroundColor(String menuRegularBackgroundColor) {
        this.menuRegularBackgroundColor = menuRegularBackgroundColor;
        return this;
    }


    public ThemeColors menuBoldBackgroundColor(String menuBoldBackgroundColor) {
        this.menuBoldBackgroundColor = menuBoldBackgroundColor;
        return this;
    }


    public ThemeColors menuForegroundColor(String menuForegroundColor) {
        this.menuForegroundColor = menuForegroundColor;
        return this;
    }


    public ThemeColors menuDecorationColor(String menuDecorationColor) {
        this.menuDecorationColor = menuDecorationColor;
        return this;
    }


    public ThemeColors guiElementColor(String guiElementColor) {
        this.guiElementColor = guiElementColor;
        return this;
    }


    public static class ThemeColorsBuilder {

        private String menuRegularBackgroundColor;
        private String menuBoldBackgroundColor;
        private String menuForegroundColor;
        private String menuDecorationColor;
        private String guiElementColor;


        ThemeColorsBuilder() {
        }


        public ThemeColorsBuilder menuRegularBackgroundColor(String menuRegularBackgroundColor) {
            this.menuRegularBackgroundColor = menuRegularBackgroundColor;
            return this;
        }


        public ThemeColorsBuilder menuBoldBackgroundColor(String menuBoldBackgroundColor) {
            this.menuBoldBackgroundColor = menuBoldBackgroundColor;
            return this;
        }


        public ThemeColorsBuilder menuForegroundColor(String menuForegroundColor) {
            this.menuForegroundColor = menuForegroundColor;
            return this;
        }


        public ThemeColorsBuilder menuDecorationColor(String menuDecorationColor) {
            this.menuDecorationColor = menuDecorationColor;
            return this;
        }


        public ThemeColorsBuilder guiElementColor(String guiElementColor) {
            this.guiElementColor = guiElementColor;
            return this;
        }


        public ThemeColors build() {
            return new ThemeColors(menuRegularBackgroundColor, menuBoldBackgroundColor, menuForegroundColor, menuDecorationColor, guiElementColor);
        }

    }

}
