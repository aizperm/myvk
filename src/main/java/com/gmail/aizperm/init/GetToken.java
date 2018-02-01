package com.gmail.aizperm.init;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.gmail.aizperm.vk.TokenUtils;
import com.gmail.aizperm.vk.VKProperties;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;

public class GetToken
{
    public static void main(String[] args) throws ApiException, ClientException, FileNotFoundException, IOException
    {
        String code = args[0];

        int client_id = VKProperties.getClientID();
        String secretCode = VKProperties.getSecretCode();

        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);

        UserAuthResponse authResponse = vk.oauth().userAuthorizationCodeFlow(client_id, secretCode, "https://oauth.vk.com/blank.html", code).execute();
        String accessToken = authResponse.getAccessToken();

        TokenUtils.writeToken(accessToken);
    }
}
