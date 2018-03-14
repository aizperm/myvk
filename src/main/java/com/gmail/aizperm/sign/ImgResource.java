package com.gmail.aizperm.sign;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import com.gmail.aizperm.util.PNGArgs;
import com.gmail.aizperm.util.FileUtil;

public class ImgResource
{
    private static int DEF_TYPE = PNGArgs.TYPE_1;
    private static String DEF_LOCALE = PNGArgs.LOCALE_RU;

    public static final File getRu(String locale, Integer type, int sourceWidth)
    {
        if (locale == null)
            locale = DEF_LOCALE;
        if (type == null)
            type = DEF_TYPE;

        Integer key = null;
        Integer min = null;
        Map<Integer, String> allResources = getAllResources(locale, type);
        Set<Entry<Integer, String>> entrySet = allResources.entrySet();
        for (Entry<Integer, String> entry : entrySet)
        {
            int delta = Math.abs(sourceWidth / 2 - entry.getKey());
            if (min == null || delta < min)
            {
                min = delta;
                key = entry.getKey();
            }
        }

        return new File(allResources.get(key));
    }

    @SuppressWarnings("unused")
    private static Map<Integer, String> getAllResources(final String locale, final int type)
    {
        Map<Integer, String> res2width = new TreeMap<>();
        File imgDirFile = FileUtil.getImgDirFile();
        File[] listFiles = imgDirFile.listFiles(new FilenameFilter()
        {
            @Override
            public boolean accept(File dir, String name)
            {
                return name.startsWith(locale + "_" + type);
            }
        });
        for (File file : listFiles)
        {
            try (Scanner scanner = new Scanner(file.getName());)
            {
                scanner.useDelimiter("\\.|_");

                String flocale = null;
                String ftype = null;
                String fwidth = null;
                if (scanner.hasNext())
                    flocale = scanner.next();
                if (scanner.hasNext())
                    ftype = scanner.next();
                if (scanner.hasNext())
                    fwidth = scanner.next();
                if (fwidth != null)
                {
                    int width = Integer.parseInt(fwidth);
                    res2width.put(width, file.getAbsolutePath());
                }
            }
        }
        return res2width;
    }

    public static void main(String[] args)
    {
        File res = getRu("ru", 1, 700);
        System.out.println(res.getAbsolutePath());
    }
}
