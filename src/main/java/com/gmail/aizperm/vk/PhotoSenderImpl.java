package com.gmail.aizperm.vk;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.FormBodyPartBuilder;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.photos.PhotoUpload;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;
import com.vk.api.sdk.queries.photos.PhotosGetMessagesUploadServerQuery;
import com.vk.api.sdk.queries.photos.PhotosSaveMessagesPhotoQuery;

public class PhotoSenderImpl
{
    private static Logger log = LoggerFactory.getLogger(PhotoSenderImpl.class);

    private VkApiClient vk;
    private UserActor actor;

    public PhotoSenderImpl()
    {
        super();
        int client_id = VKProperties.getClientID();
        String accessToken = TokenUtils.readToken();

        TransportClient transportClient = HttpTransportClient.getInstance();
        vk = new VkApiClient(transportClient);
        actor = new UserActor(client_id, accessToken);
    }

    @SuppressWarnings("deprecation")
    public void send(List<MessageDesc> messages)
    {
        for (MessageDesc message : messages)
        {
            try
            {
                List<String> allAttachment = new ArrayList<>();

                List<String> imageUrls = message.getImageUrls();
                for (String url : imageUrls)
                {
                    String path = message.getPath(url);
                    if (path != null)
                    {

                        PhotosGetMessagesUploadServerQuery uploadServer = vk.photos().getMessagesUploadServer(actor);
                        PhotoUpload upload = uploadServer.execute();
                        String uploadUrl = upload.getUploadUrl();
                        log.debug("Uses upload server: {}", uploadUrl);

                        CloseableHttpClient httpClient = HttpClients.createDefault();
                        HttpPost httpPost = new HttpPost(uploadUrl);

                        FileBody uploadFilePart = new FileBody(new File(path));
                        FormBodyPartBuilder bodyBuilder = FormBodyPartBuilder.create();
                        bodyBuilder.setBody(uploadFilePart);
                        bodyBuilder.setName("photo");
                        MultipartEntity reqEntity = new MultipartEntity();
                        reqEntity.addPart(bodyBuilder.build());
                        httpPost.setEntity(reqEntity);

                        CloseableHttpResponse response = httpClient.execute(httpPost);

                        byte[] respBytes = IOUtils.toByteArray(response.getEntity().getContent());
                        String respStr = new String(respBytes);
                        log.debug("Uploades photo: {}", respStr);

                        JsonObject json = (JsonObject) new JsonParser().parse(respStr);
                        String photo = json.getAsJsonPrimitive("photo").getAsString();

                        PhotosSaveMessagesPhotoQuery photosSaveMessagesPhotoQuery = vk.photos().saveMessagesPhoto(actor, photo);
                        photosSaveMessagesPhotoQuery.hash(json.getAsJsonPrimitive("hash").getAsString()).server(json.getAsJsonPrimitive("server").getAsInt());
                        List<Photo> saved = photosSaveMessagesPhotoQuery.execute();

                        for (Photo p : saved)
                        {
                            allAttachment.add("photo" + p.getOwnerId() + "_" + p.getId());
                        }
                    }
                    else log.error("Отсутствует файл для URL{}", url);
                }

                MessagesSendQuery send = vk.messages().send(actor).userId(message.getUserId());
                send.randomId((int) (Math.random() * 1000000));
                send.message("signed image");
                Integer sendResult = send.attachment(allAttachment).execute();
                message.setSendResult(sendResult);
            }
            catch (Throwable e)
            {
                log.error("Ошибка отправки сообщения: {}", message.getId(), e);
            }
        }
    }

}
