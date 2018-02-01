package com.gmail.aizperm.vk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.commons.io.IOUtils;

import com.gmail.aizperm.util.FileUtil;

public class TokenUtils
{
    public static void writeToken(String token)
    {
        File tmpTokenFile = FileUtil.getTmpFile("token.txt");
        try
        {
            IOUtils.write(token, new FileOutputStream(tmpTokenFile), "UTF-8");
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String readToken()
    {
        File tmpTokenFile = FileUtil.getTmpFile("token.txt");
        try
        {
            String accessToken = IOUtils.toString(new FileInputStream(tmpTokenFile), "UTF-8");
            return accessToken;
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
