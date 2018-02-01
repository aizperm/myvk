package com.gmail.aizperm.vk;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.messages.MessageAttachment;
import com.vk.api.sdk.objects.messages.responses.GetResponse;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.queries.messages.MessagesGetQuery;

public class VKGetImagesImpl implements VKGetImages
{
    private static Logger log = LoggerFactory.getLogger(URLDownloader.class);

    private UserActor actor;
    private VkApiClient vk;

    public VKGetImagesImpl()
    {
        super();
        int client_id = VKProperties.getClientID();
        String accessToken = TokenUtils.readToken();

        TransportClient transportClient = HttpTransportClient.getInstance();
        vk = new VkApiClient(transportClient);
        actor = new UserActor(client_id, accessToken);
    }

    @Override
    public List<MessageDesc> getMessages(Integer lastIdMessage)
    {
        List<MessageDesc> result = new ArrayList<MessageDesc>();
        List<Message> items = fetchMessages(lastIdMessage);
        for (Message message : items)
        {
            MessageDesc desc = new MessageDesc();
            desc.setId(message.getId());
            desc.setUserId(message.getUserId());
            desc.setBody(message.getBody());

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            String date = sdf.format(new Date(message.getDate() * 1000L));
            log.info("Message: date = {}, userId = {}, id = {}: {}", date, message.getUserId(), message.getId(), message.getBody());

            List<MessageAttachment> attachments = message.getAttachments();
            if (attachments != null)
            {
                for (MessageAttachment attachment : attachments)
                {
                    Photo photo = attachment.getPhoto();
                    if (photo != null)
                    {
                        String url = getPhotoUrl(photo);
                        desc.getImageUrls().add(url);
                    }
                }
            }
            result.add(desc);
        }

        return result;
    }

    private List<Message> fetchMessages(Integer lastIdMessage)
    {
        MessagesGetQuery mesQuery = createQuery(lastIdMessage);

        GetResponse historyResp;
        try
        {
            historyResp = mesQuery.execute();
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return historyResp.getItems();
    }

    private MessagesGetQuery createQuery(Integer lastIdMessage)
    {
        return lastIdMessage != null ? vk.messages().get(actor).lastMessageId(lastIdMessage).count(200) : vk.messages().get(actor);
    }

    private static String getPhotoUrl(Photo photo)
    {
        if (photo.getPhoto2560() != null)
            return photo.getPhoto2560();
        if (photo.getPhoto1280() != null)
            return photo.getPhoto1280();
        if (photo.getPhoto807() != null)
            return photo.getPhoto807();
        if (photo.getPhoto604() != null)
            return photo.getPhoto604();
        if (photo.getPhoto130() != null)
            return photo.getPhoto130();
        return photo.getPhoto75();
    }

}
