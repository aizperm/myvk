package com.gmail.aizperm.vk;

import java.util.List;

public class VKGetImagesMain
{
    public static void main(String[] args)
    {
        List<MessageDesc> messages = new VKGetImagesImpl().getMessages(null);
        for (MessageDesc messageDesc : messages)
        {
            System.out.println(messageDesc.getBody());
        }
    }

}
