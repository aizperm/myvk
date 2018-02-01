package com.gmail.aizperm.vk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageDesc
{
    private Integer id;
    private Integer userId;
    private String body;
    private List<String> imageUrls = new ArrayList<String>();
    private Map<String, String> url2path = new HashMap<String, String>();
    private Integer sendResult;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    public List<String> getImageUrls()
    {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls)
    {
        this.imageUrls = imageUrls;
    }

    public void addUrl2Path(String url, String path)
    {
        url2path.put(url, path);
    }
    
    public void removePath(String url)
    {
        url2path.remove(url);        
    }

    public Map<String, String> getUrl2path()
    {
        return url2path;
    }

    public String getPath(String url)
    {
        return url2path.get(url);
    }

    public Integer getUserId()
    {
        return userId;
    }

    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    public Integer getSendResult()
    {
        return sendResult;
    }

    public void setSendResult(Integer sendResult)
    {
        this.sendResult = sendResult;
    }   

}
