package com.gmail.aizperm.vk;

public interface MessageIDHolder
{
    void writeLastMessageID(Integer id);
    
    Integer readLastMessageID();
    
    void deleteFile();
}
