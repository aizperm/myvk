package com.gmail.aizperm.util;

import java.io.File;

import org.eclipse.jetty.util.StringUtil;

import com.gmail.aizperm.vk.VKProperties;

public class FileUtil
{
    public static File getTmpFile(String name)
    {
        File tmpDir = getTmpDir();
        File file = new File(tmpDir, name);
        return file;
    }

    public static File createTmpFile(String name)
    {
        File tmpDir = getTmpDir();
        File dir = new File(tmpDir, name);
        if (!dir.exists())
            dir.mkdir();
        return dir;
    }

    public static File getTmpDir()
    {
        String tmp = System.getProperty("java.io.tmpdir");
        File tmpDir = new File(tmp);
        return tmpDir;
    }

    public static File getMessageIdFilePath()
    {
        File file = new File(VKProperties.getMessageIdFilePath());
        createDirs(file.getParentFile());
        return file;
    }

    private static void createDirs(File parent)
    {
        if (!parent.exists())
            parent.mkdirs();
    }

    public static File getDownloadDirPath()
    {
        File dir = new File(VKProperties.getDownloadDirPath());
        createDirs(dir);
        return dir;
    }

    public static File getConfDirFile()
    {
        String confdir = System.getProperty("conf.dir");
        if (StringUtil.isBlank(confdir))
        {
            File conf = new File("conf");
            if (conf.exists())
                return conf;
        }
        else
        {
            File conf = new File(confdir);
            if (conf.exists())
                return conf;
        }
        throw new RuntimeException("Не найдена папка конфигурации: conf");
    }

    public static File getImgDirFile()
    {
        String confdir = System.getProperty("img.dir");
        if (StringUtil.isBlank(confdir))
        {
            File conf = new File("img");
            if (conf.exists())
                return conf;
        }
        else
        {
            File conf = new File(confdir);
            if (conf.exists())
                return conf;
        }
        throw new RuntimeException("Не найдена папка конфигурации: img");
    }
    
    public static File getFontDirFile()
    {
        String confdir = System.getProperty("font.dir");
        if (StringUtil.isBlank(confdir))
        {
            File conf = new File("fonts");
            if (conf.exists())
                return conf;
        }
        else
        {
            File conf = new File(confdir);
            if (conf.exists())
                return conf;
        }
        throw new RuntimeException("Не найдена папка конфигурации: font");
    }

    public static String getFromConfDir(String res)
    {
        return new File(getConfDirFile(), res).getAbsolutePath();
    }

    public static String getFromImgDir(String res)
    {
        return new File(getImgDirFile(), res).getAbsolutePath();
    }
    
    public static String getFromFontDir(String font)
    {
        return new File(getFontDirFile(), font).getAbsolutePath();
    }
}
