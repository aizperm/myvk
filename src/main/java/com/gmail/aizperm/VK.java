package com.gmail.aizperm;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import com.vk.api.sdk.objects.messages.LastActivity;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.messages.MessageAttachment;
import com.vk.api.sdk.objects.messages.responses.GetHistoryResponse;
import com.vk.api.sdk.objects.messages.responses.GetResponse;
import com.vk.api.sdk.objects.messages.responses.SearchResponse;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.photos.PhotoUpload;
import com.vk.api.sdk.queries.messages.*;
import com.vk.api.sdk.queries.photos.PhotosGetMessagesUploadServerQuery;
import com.vk.api.sdk.queries.photos.PhotosGetUploadServerQuery;
import com.vk.api.sdk.queries.photos.PhotosSaveMessagesPhotoQuery;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.*;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.RequestLogHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class VKMain {
    public static void main(String[] args) throws Exception {
        Integer client_id = 6350161;
        String accessToken = null;

        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);

        if (false) {
            String url = new OAuth(client_id).build();
            System.out.println(url);
            //Desktop.getDesktop().browse(new URI(url));
            String code = "";


            UserAuthResponse authResponse = vk.oauth().userAuthorizationCodeFlow(client_id, "ojwlrmx5og8ZxwlrMtF2", "https://oauth.vk.com/blank.html", code).execute();
            accessToken = authResponse.getAccessToken();
            System.out.println("accessToken = " + accessToken);
        } else accessToken = "";

        UserActor actor = new UserActor(client_id, accessToken);

        int svm = 136390099;
        int aiz = 99674726;
        if (false) {
            MessagesGetHistoryQuery mesQuery = vk.messages().getHistory(actor).userId(svm).startMessageId(-1).count(1);
            GetHistoryResponse historyResp = mesQuery.execute();
            List<Message> items = historyResp.getItems();
            for (Message mes : items) {
                System.out.println(mes.getBody());
                List<MessageAttachment> attachments = mes.getAttachments();
                if (attachments != null)
                    for (MessageAttachment att : attachments) {
                        Photo photo = att.getPhoto();
                        String url = getPhotoUrl(photo);
                        System.out.println("photo=" + url);
                        URI uri = new URI(url);
                        byte[] bytes = IOUtils.toByteArray(uri);
                        String query = new File(uri.getPath()).getName();
                        IOUtils.copy(new ByteArrayInputStream(bytes), new FileOutputStream("d:\\Converter\\temp\\" + query));
                    }
            }
        }


        MessagesSendQuery send = vk.messages().send(actor).userId(svm);
        send.randomId((int) (Math.random() * 1000000));
        send.message("signed");

        File dir = new File("d:\\Converter\\temp\\");
        File[] files = dir.listFiles();
        List<String> allAttachment = new ArrayList<>();
        for (File f : files) {
            PhotosGetMessagesUploadServerQuery uploadServer = vk.photos().getMessagesUploadServer(actor);
            PhotoUpload upload = uploadServer.execute();
            String uploadUrl = upload.getUploadUrl();

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(uploadUrl);

            FileBody uploadFilePart = new FileBody(f);
            FormBodyPartBuilder bodyBuilder = FormBodyPartBuilder.create();
            bodyBuilder.setBody(uploadFilePart);
            bodyBuilder.setName("photo");
            MultipartEntity reqEntity = new MultipartEntity();
            reqEntity.addPart(bodyBuilder.build());
            httpPost.setEntity(reqEntity);

            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity ent = response.getEntity();
            byte[] respBytes = IOUtils.toByteArray(response.getEntity().getContent());
            String respStr = new String(respBytes);
            JsonObject json = (JsonObject) new JsonParser().parse(respStr);
            String photo = json.getAsJsonPrimitive("photo").getAsString();

            PhotosSaveMessagesPhotoQuery photosSaveMessagesPhotoQuery = vk.photos().saveMessagesPhoto(actor, photo);
            photosSaveMessagesPhotoQuery.hash(json.getAsJsonPrimitive("hash").getAsString()).server(json.getAsJsonPrimitive("server").getAsInt());
            List<Photo> saved = photosSaveMessagesPhotoQuery.execute();
            for (Photo p : saved) {
                allAttachment.add("photo" + p.getOwnerId() + "_" + p.getId());
            }
        }
        Integer sendedResp = send.attachment(allAttachment).execute();
    }

    private static String getPhotoUrl(Photo photo) {
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
