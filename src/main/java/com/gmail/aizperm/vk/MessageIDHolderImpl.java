package com.gmail.aizperm.vk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.io.IOUtils;

import com.gmail.aizperm.util.FileUtil;

public class MessageIDHolderImpl implements MessageIDHolder
{
    private static final MessageIDHolder INST = new MessageIDHolderImpl();

    public static final MessageIDHolder getInst()
    {
        return INST;
    }

    private Lock lock = new ReentrantLock();

    private MessageIDHolderImpl()
    {
        super();
    }

    @Override
    public void writeLastMessageID(Integer id)
    {
        try
        {
            lock.lock();
            writeId(id);
        }
        finally
        {
            lock.unlock();
        }
    }

    @Override
    public Integer readLastMessageID()
    {
        try
        {
            lock.lock();
            return readId();
        }
        finally
        {
            lock.unlock();
        }
    }

    @Override
    public void deleteFile()
    {
        try
        {
            lock.lock();
            File file = FileUtil.getMessageIdFilePath();
            if (file.exists())
                file.delete();
        }
        finally
        {
            lock.unlock();
        }
    }

    private void writeId(Integer id)
    {
        File file = FileUtil.getMessageIdFilePath();
        try
        {
            IOUtils.write(String.valueOf(id), new FileOutputStream(file), "UTF-8");
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

    private Integer readId()
    {
        File file = FileUtil.getMessageIdFilePath();
        if (!file.exists())
            return null;
        try
        {
            return Integer.parseInt(IOUtils.toString(new FileInputStream(file), "UTF-8"));
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

}
