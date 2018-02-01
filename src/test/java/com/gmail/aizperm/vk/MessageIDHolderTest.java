package com.gmail.aizperm.vk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gmail.aizperm.util.FileUtil;
import com.gmail.aizperm.vk.MessageIDHolder;
import com.gmail.aizperm.vk.MessageIDHolderImpl;
import com.gmail.aizperm.vk.VKProperties;

public class MessageIDHolderTest
{

    @Before
    @After
    public void deleteFile()
    {
        File file = FileUtil.getMessageIdFilePath();
        if (file.exists())
            file.delete();
    }

    @Test
    public void test()
    {
        VKProperties.load(MessageIDHolderTest.class.getResourceAsStream("test.properties"));
        MessageIDHolder inst = MessageIDHolderImpl.getInst();
        assertNotNull(inst);

        Integer lastMessageID = inst.readLastMessageID();
        assertNull(lastMessageID);

        Integer id1 = 111;
        inst.writeLastMessageID(id1);
        assertEquals(id1, inst.readLastMessageID());

        Integer id2 = 112;
        inst.writeLastMessageID(id2);
        assertEquals(id2, inst.readLastMessageID());
    }

}
