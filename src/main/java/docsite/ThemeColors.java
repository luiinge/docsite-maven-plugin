package docsite;

import lombok.*;
import lombok.experimental.Accessors;

@Builder
@Getter
@Setter @Accessors(fluent = true)
public class ThemeColors {

    public static ThemeColors DEFAULT = ThemeColors.builder()
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

}
