package com.gmail.aizperm.vk;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.gmail.aizperm.vk.MessageDesc;
import com.gmail.aizperm.vk.MessageIDHolderImpl;
import com.gmail.aizperm.vk.PhotoDownloader;

public class PhotoDownloaderRealTest
{

    @Test
    public void test1()
    {
        MessageIDHolderImpl.getInst().deleteFile();

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
