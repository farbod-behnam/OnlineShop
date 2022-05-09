package com.OnlineShop.exception;


import java.util.ArrayList;
import java.util.List;

public class ErrorListResponse
{
    private int status;
    private List<String> messageList;
    private long timeStamp;

    public ErrorListResponse()
    {

    }

    public ErrorListResponse(int status, List<String> messageList, long timeStamp)
    {
        this.status = status;
        this.messageList = messageList;
        this.timeStamp = timeStamp;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public List<String> getMessage()
    {
        return messageList;
    }

    public void setMessage(List<String> message)
    {
        this.messageList = message;
    }

    public long getTimeStamp()
    {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp)
    {
        this.timeStamp = timeStamp;
    }

    public void AddMessage(String message)
    {
        if (this.messageList.isEmpty())
            this.messageList = new ArrayList<>();

        this.messageList.add(message);
    }
}
