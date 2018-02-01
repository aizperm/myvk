package com.gmail.aizperm.init;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.gmail.aizperm.vk.OAuth;
import com.gmail.aizperm.vk.VKProperties;

public class GetAuthCode
{
    public static void main(String[] args) throws IOException, URISyntaxException
    {
        int client_id = VKProperties.getClientID();
        String url = new OAuth(client_id).build();
        Desktop.getDesktop().browse(new URI(url));
    }
}
