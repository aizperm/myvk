package com.gmail.aizperm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

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

        int client_id = 6353824;
        String secretCode = "aeeFnvVL4CGQnyrgApBH";

        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);

        UserAuthResponse authResponse = vk.oauth().userAuthorizationCodeFlow(client_id, secretCode, "https://oauth.vk.com/blank.html", code).execute();
        String accessToken = authResponse.getAccessToken();

        File tmpTokenFile = FileUtil.getTmpFile("token.txt");
        IOUtils.write(accessToken, new FileOutputStream(tmpTokenFile), "UTF-8");

    }
}
