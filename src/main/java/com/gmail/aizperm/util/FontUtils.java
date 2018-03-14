package com.gmail.aizperm.util;

import java.awt.Font;
import java.io.File;

public class FontUtils
{
    public static final String MAYA = "Maya";
    public static final String SLADKOESHKA = "Sladkoeshka";
    public static final String RUSSIAN_LAND_CYRILLIC = "RussianLandCyrillic";

    public static Font getFont(String name, float size)
    {
        String fontFilePath = FileUtil.getFromFontDir(getWithSuffix(name));
        File fontFile = new File(fontFilePath);
        if (!fontFile.exists())
        {
            fontFilePath = FileUtil.getFromFontDir(getWithSuffix(SLADKOESHKA));
            fontFile = new File(fontFilePath);
        }
        Font font = null;
        try
        {
            font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return font.deriveFont(size);
    }

    public static boolean isExists(String fontName)
    {
        String fontFilePath = FileUtil.getFromFontDir(getWithSuffix(fontName));
        File fontFile = new File(fontFilePath);
        return fontFile.exists();
    }
    
    public static String getByNumber(int number)
    {
        switch (number)
        {
            case 2:
                return SLADKOESHKA;                            
            case 3:
                return RUSSIAN_LAND_CYRILLIC;
        }
        return MAYA;
    }

    private static String getWithSuffix(String name)
    {
        return name + ".ttf";
    }
}
