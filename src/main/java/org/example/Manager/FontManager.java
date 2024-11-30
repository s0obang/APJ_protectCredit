package org.example.Manager;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;

public class FontManager {

  public static void loadFonts() {
    try {

      InputStream fontStream = FontManager.class.getResourceAsStream("/font/Galmuri11.ttf");
      if (fontStream == null) {
        throw new IOException("폰트 파일을 찾을 수 없습니다: /font/Galmuri11.ttf");
      }

      Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(15f);
      System.out.println(customFont.getFontName());

      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      ge.registerFont(customFont);

      System.out.println("폰트가 성공적으로 로드되었습니다: " + customFont.getFontName());
    } catch (FontFormatException | IOException e) {
      e.printStackTrace();
    }
  }
}
