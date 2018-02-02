package com.gmail.aizperm.vk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gmail.aizperm.util.FileUtil;
import com.gmail.aizperm.vk.MessageDesc;
import com.gmail.aizperm.vk.MessageIDHolderImpl;
import com.gmail.aizperm.vk.PhotoDownloader;
import com.gmail.aizperm.vk.URLDownloader;
import com.gmail.aizperm.vk.VKGetImages;

public class PhotoDownloaderTest
{

    @Before
    @After
    public void deleteFile()
    {
        try
        {
            VKProperties.load(new FileInputStream(new File("src/test/java/com/gmail/aizperm/vk/test.properties")));
        }
        catch (FileNotFoundException e)
        {            
            e.printStackTrace();
        }
        File file = FileUtil.getMessageIdFilePath();
        if (file.exists())
            file.delete();
    }

    @Test
    public void test()
    {
        MessageDesc desc1 = new MessageDesc();
        desc1.setId(1);
        desc1.setBody("img");
        desc1.getImageUrls().add("http://download1.ru");

        MessageDesc desc2 = new MessageDesc();
        desc2.setId(2);
        desc2.setBody("img");
        desc2.getImageUrls().add("http://download2.ru");

        VKGetImages getter = getGetter(desc1, desc2);

        URLDownloader urlDownloader = getTestUrlDownloader();

        PhotoDownloader photoDownloader = PhotoDownloader.builder().getter(getter).downloader(urlDownloader).build();
        List<MessageDesc> downloadedMessages = photoDownloader.download();
        assertEquals(2, downloadedMessages.size());

        for (MessageDesc messageDesc : downloadedMessages)
        {
            List<String> imageUrls = messageDesc.getImageUrls();
            for (String url : imageUrls)
            {
                assertNotNull("Не скачался url " + url, messageDesc.getPath(url));
            }
        }

        assertEquals(desc2.getId(), MessageIDHolderImpl.getInst().readLastMessageID());
    }

    private URLDownloader getTestUrlDownloader()
    {
        URLDownloader urlDownloader = new URLDownloader()
        {
            @Override
            public File download(String userId, String id, String url)
            {
                return new File("");
            }
        };
        return urlDownloader;
    }

    private VKGetImages getGetter(final MessageDesc... desc)
    {
        VKGetImages getter = new VKGetImages()
        {
            @Override
            public List<MessageDesc> getMessages(Integer lastId)
            {
                List<MessageDesc> list = new ArrayList<MessageDesc>();
                for (MessageDesc desc : desc)
                {
                    list.add(desc);
                }
                return list;
            }
        };
        return getter;
    }

}
