package org.example.Manager;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FontManager {
    public static void loadFonts() {
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/java/org/example/font/neodgm.ttf")).deriveFont(24f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
            // 폰트이름: Neo둥근모

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }
}