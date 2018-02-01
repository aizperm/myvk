package com.gmail.aizperm.vk;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gmail.aizperm.util.FileUtil;

public class URLDownloaderImpl implements URLDownloader
{
    private static Logger log = LoggerFactory.getLogger(URLDownloader.class);

    private static final URLDownloader INST = new URLDownloaderImpl();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy");

    public static final URLDownloader getInst()
    {
        return INST;
    }

    private URLDownloaderImpl()
    {
        super();
    }

    @Override
    public File download(String userId, String messageId, String urlString)
    {
        log.debug("Скачиваем url {}", urlString);

        File downloadDir = FileUtil.getDownloadDirPath();

        File userDir = new File(downloadDir, userId);
        if (!userDir.exists())
            userDir.mkdir();

        String dirName = sdf.format(new Date());

        File dirToday = new File(userDir, dirName);
        if (!dirToday.exists())
            dirToday.mkdir();

        File dirMessage = new File(dirToday, String.valueOf(messageId));
        if (!dirMessage.exists())
            dirMessage.mkdir();

        File outputFile = null;
        try
        {
            URL url = new URL(urlString);
            URI uri = new URI(urlString);
            byte[] bytes = IOUtils.toByteArray(uri);
            log.debug("Скачали url {}", urlString);

            String name = new File(url.getPath()).getName();
            outputFile = new File(dirMessage, name);
            IOUtils.write(bytes, new FileOutputStream(outputFile));
            log.debug("Сохранили url {} в {}", urlString, outputFile.getAbsolutePath());
        }
        catch (Throwable e)
        {
            log.error("Ошибка при скачивании URL{}", urlString, e);
            throw new RuntimeException(e);
        }
        return outputFile;
    }

}
