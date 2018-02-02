package com.gmail.aizperm.vk;

import java.util.List;

import org.junit.Assert;

public class PhotoDownloaderMain
{
    public static void main(String[] args)
    {
        PhotoDownloader downloader = PhotoDownloader.builder().build();
        List<MessageDesc> messages = downloader.download();
        for (MessageDesc message : messages)
        {
            String body = message.getBody();
            System.out.println(body);
            List<String> imageUrls = message.getImageUrls();
            for (String url : imageUrls)
            {
                System.out.println(message.getPath(url));
            }
        }

        new PhotoSenderImpl().send(messages);

        messages = downloader.download();
        Assert.assertTrue(messages.isEmpty());
    }

}
