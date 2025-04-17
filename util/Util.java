package util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

public class Util {

    public static Font loadCustomFont(String path, float size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(path));
            return font.deriveFont(size); // Set the size you want
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return new Font("SansSerif", Font.BOLD, (int) size); // fallback
        }
    }
    
    public static Color lighten(Color color, float percentage) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        hsb[2] = Math.min(1.0f, hsb[2] + percentage); // hsb[2] = brightness
        return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
    }

}
