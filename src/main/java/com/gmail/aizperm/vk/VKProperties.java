package com.gmail.aizperm.vk;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.eclipse.jetty.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VKProperties
{
    private static Logger log = LoggerFactory.getLogger(VKProperties.class);

    public static final String SECRET_CODE = "secret_code";
    public static final String CLIENT_ID = "client_id";
    public static final String DOWNLOAD_DIR_PATH = "download_dir_path";
    public static final String MESSAGE_ID_FILE_PATH = "message_id_file_path";
    public static final String DOWNLOAD_MESSAGE_COUNT = "download_message_count";

    private static Properties props;

    private static void init(InputStream in) throws IOException
    {
        props.load(in);
    }

    public static Integer getClientID()
    {
        load();
        return Integer.parseInt(props.getProperty(CLIENT_ID));
    }

    public static String getSecretCode()
    {
        load();
        return props.getProperty(SECRET_CODE);
    }

    private static void load()
    {
        InputStream defResource = VKProperties.class.getResourceAsStream("config.properties");
        load(defResource);
        log.debug("default properties init");
    }

    public static void load(InputStream resource)
    {
        if (props == null)
        {
            props = new Properties();
            try
            {
                init(resource);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    public static String getDownloadDirPath()
    {
        load();
        return props.getProperty(DOWNLOAD_DIR_PATH);
    }

    public static String getMessageIdFilePath()
    {
        load();
        return props.getProperty(MESSAGE_ID_FILE_PATH);
    }

    public static int getDownloadMessgeCount(int def)
    {
        load();
        String count = props.getProperty(DOWNLOAD_MESSAGE_COUNT);
        if (StringUtil.isBlank(count))
            return def;
        return Integer.parseInt(count);
    }
}
