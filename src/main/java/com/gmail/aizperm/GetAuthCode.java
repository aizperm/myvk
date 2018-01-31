package com.gmail.aizperm;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class GetAuthCode
{
    public static void main(String[] args) throws IOException, URISyntaxException
    {
        int client_id = 6353824;
        String url = new OAuth(client_id).build();
        Desktop.getDesktop().browse(new URI(url));
    }
}
