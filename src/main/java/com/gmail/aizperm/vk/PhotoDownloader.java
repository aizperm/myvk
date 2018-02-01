package com.gmail.aizperm.vk;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jetty.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhotoDownloader
{
    private static Logger log = LoggerFactory.getLogger(PhotoDownloader.class);

    private MessageIDHolder messageIdHolder;
    private VKGetImages getter;
    private URLDownloader urlDownloader;

    public PhotoDownloader(MessageIDHolder messageIdHolder, VKGetImages getter, URLDownloader urlDownloader)
    {
        super();
        this.messageIdHolder = messageIdHolder;
        this.getter = getter;
        this.urlDownloader = urlDownloader;
    }

    protected PhotoDownloader()
    {
    }

    public List<MessageDesc> download()
    {
        Integer messageID = messageIdHolder.readLastMessageID();
        List<MessageDesc> messages = getter.getMessages(messageID);
        log.debug("Получено сообщений: {} ", messages.size());
        sortById(messages);

        List<MessageDesc> filtered = new ArrayList<>();
        for (MessageDesc message : messages)
        {
            String body = message.getBody();
            if (!StringUtil.isBlank(body))
            {
                String[] split = body.split(" ");
                String command = split[0];
                List<String> asList = Arrays.asList("i", "img", "image");
                if (asList.contains(command.toLowerCase()))
                {
                    downloadFromMessage(message);
                    filtered.add(message);
                }
            }
            messageIdHolder.writeLastMessageID(message.getId());
        }
        return filtered;
    }

    private void downloadFromMessage(MessageDesc message)
    {

        Integer id = message.getId();
        List<String> imageUrls = message.getImageUrls();
        for (String url : imageUrls)
        {
            String userId = String.valueOf(message.getUserId());
            String messageId = String.valueOf(id);
            File downloaded = urlDownloader.download(userId, messageId, url);
            message.addUrl2Path(url, downloaded.getAbsolutePath());
        }

    }

    private void sortById(List<MessageDesc> messages)
    {
        Collections.sort(messages, new Comparator<MessageDesc>()
        {
            @Override
            public int compare(MessageDesc o1, MessageDesc o2)
            {
                return Integer.compare(o1.getId(), o2.getId());
            }
        });
    }

    public static Builder builder()
    {
        return new Builder();
    }

    public static class Builder
    {
        private MessageIDHolder messageIdHolder;
        private VKGetImages getter;
        private URLDownloader urlDownloader;

        private Builder()
        {
            super();
        }

        public PhotoDownloader build()
        {
            if (messageIdHolder == null)
                messageIdHolder = MessageIDHolderImpl.getInst();
            if (urlDownloader == null)
                urlDownloader = URLDownloaderImpl.getInst();
            if (getter == null)
                getter = new VKGetImagesImpl();
            return new PhotoDownloader(messageIdHolder, getter, urlDownloader);
        }

        public Builder holder(MessageIDHolder holder)
        {
            messageIdHolder = holder;
            return this;
        }

        public Builder downloader(URLDownloader downloader)
        {
            urlDownloader = downloader;
            return this;
        }

        public Builder getter(VKGetImages getter)
        {
            this.getter = getter;
            return this;
        }

    }
}
