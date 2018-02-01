package com.gmail.aizperm.vk;

import java.io.File;

public interface URLDownloader
{
    File download(String userId, String id, String url);
}
