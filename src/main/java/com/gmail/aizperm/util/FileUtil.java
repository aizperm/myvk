package com.gmail.aizperm.util;

import java.io.File;

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
}
