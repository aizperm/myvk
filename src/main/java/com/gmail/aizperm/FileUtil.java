package com.gmail.aizperm;

import java.io.File;

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
}
