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

import com.gmail.aizperm.command.CommandArgs;
import com.gmail.aizperm.command.CommandParser;
import com.gmail.aizperm.util.FileUtil;
import com.gmail.aizperm.vk.MessageDesc;
import com.gmail.aizperm.vk.PhotoDownloader;
import com.gmail.aizperm.vk.PhotoSenderImpl;

public class StartBot
{
    private static Logger log = LoggerFactory.getLogger(StartBot.class);
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private PhotoSenderImpl sender = new PhotoSenderImpl();

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
            sender.send(messages);
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
                        CommandArgs command = new CommandParser().parse(body);
                        if (command != null)
                        {
                            FileInputStream input = new FileInputStream(path);
                            byte[] sourceBytes = IOUtils.toByteArray(input);
                            input.close();

                            command.getArgs().addArg(sourceBytes);
                            byte[] signedBytes = (byte[]) command.execute();

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
                            FileOutputStream output = new FileOutputStream(fileSign);
                            IOUtils.write(signedBytes, output);
                            output.close();

                            message.removePath(url);
                            message.addUrl2Path(url, fileSign.getAbsolutePath());
                        }
                    }
                    catch (Exception e)
                    {
                        log.error("Ошибка при обработке сообщения {}", message.getId(), e);
                    }
                }
            }
        }
    }

    public static void main(String[] args)
    {
        DOMConfigurator.configure(FileUtil.getFromConfDir("log4j.xml"));
        new StartBot().start();
    }
}
