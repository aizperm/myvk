package com.gmail.aizperm.vk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MessageIDHolderTest
{

    @Before
    @After
    public void init()
    {
        try
        {
            VKProperties.load(new FileInputStream(new File("src/test/java/com/gmail/aizperm/vk/test.properties")));
        }
        catch (FileNotFoundException e)
        {            
            e.printStackTrace();
        }
    }

    @Test
    public void test()
    {                
        MessageIDHolder inst = MessageIDHolderImpl.getInst();
        inst.writeLastMessageID(null);
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
