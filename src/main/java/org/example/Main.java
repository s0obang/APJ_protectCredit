package org.example;

import org.example.Manager.FontManager;
import org.example.Manager.GameManager;

public class Main {
    public static void main(String[] args) {
        FontManager.loadFonts();

        new GameManager();
    }
}
