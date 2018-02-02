package com.gmail.aizperm.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gmail.aizperm.sign.PhotoSignerImpl;
import com.gmail.aizperm.util.CommandArgs;
import com.gmail.aizperm.util.FileUtil;
import com.gmail.aizperm.vk.MessageDesc;
import com.gmail.aizperm.vk.PhotoDownloader;
import com.gmail.aizperm.vk.PhotoSenderImpl;

public class StartBot
{
    private static Logger log = LoggerFactory.getLogger(StartBot.class);
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public void start()
    {
        log.info("Started bot: {}", sdf.format(new Date()));

        long delay = TimeUnit.SECONDS.toMillis(10);
        new Timer().schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                readAndSend();
            }
        }, new Date(), delay);
    }

    protected void readAndSend()
    {
        log.info("readAndSend: {}", sdf.format(new Date()));
        try
        {
            PhotoDownloader downloader = PhotoDownloader.builder().build();
            List<MessageDesc> messages = downloader.download();
            sign(messages);
            new PhotoSenderImpl().send(messages);
            log.info("sended: {}", messages.size());
        }
        catch (Exception e)
        {
            log.error("Ошибка при чтении/отправке сообщений", e);
        }
    }

    private void sign(List<MessageDesc> messages)
    {
        for (MessageDesc message : messages)
        {
            List<String> imageUrls = message.getImageUrls();
            for (String url : imageUrls)
            {
                String path = message.getPath(url);
                if (path != null)
                {
                    try
                    {
                        String body = message.getBody();
                        String locale = getLocale(body);
                        Integer type = getType(body);

                        byte[] sourceBytes = IOUtils.toByteArray(new FileInputStream(path));
                        byte[] signedBytes = new PhotoSignerImpl().sign(locale, type, sourceBytes);
                        File file = new File(path);
                        String filename = file.getName();
                        String[] split = filename.split("\\.");
                        String suf = "";
                        String name = split[0];
                        if (split.length > 1)
                            suf = split[1];
                        File parentFile = file.getParentFile();
                        FileUtils.deleteQuietly(file);

                        File fileSign = new File(parentFile, name + "_sign." + suf);
                        IOUtils.write(signedBytes, new FileOutputStream(fileSign));
                        message.removePath(url);
                        message.addUrl2Path(url, fileSign.getAbsolutePath());
                    }
                    catch (Exception e)
                    {
                        log.error("Ошибка при обработке сообщения {}", message.getId(), e);
                    }
                }
            }
        }
    }

    private Integer getType(String body)
    {
        String[] split = body.split(" ");
        for (String string : split)
        {
            if (String.valueOf(CommandArgs.TYPE_1).equalsIgnoreCase(string))
                return CommandArgs.TYPE_1;
            if (String.valueOf(CommandArgs.TYPE_2).equalsIgnoreCase(string))
                return CommandArgs.TYPE_2;
        }
        return CommandArgs.TYPE_1;
    }

    private String getLocale(String body)
    {
        String[] split = body.split(" ");
        for (String string : split)
        {
            if (CommandArgs.LOCALE_RU.equalsIgnoreCase(string))
                return CommandArgs.LOCALE_RU;
            if (CommandArgs.LOCALE_EN.equalsIgnoreCase(string))
                return CommandArgs.LOCALE_EN;
        }
        return CommandArgs.LOCALE_RU;
    }

    public static void main(String[] args)
    {
        DOMConfigurator.configure(FileUtil.getFromConfDir("log4j.xml"));
        new StartBot().start();
    }
}
