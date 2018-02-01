package com.gmail.aizperm.vk;

import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Test;

import com.gmail.aizperm.vk.MessageDesc;
import com.gmail.aizperm.vk.VKGetImagesImpl;

public class VKGetImagesRealTest
{

    @Test
    public void test()
    {
        List<MessageDesc> messages = new VKGetImagesImpl().getMessages(null);
        assertFalse(messages.isEmpty());
    }

}
