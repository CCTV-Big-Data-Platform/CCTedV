package com.example.cctedv;

public class Singleton {
    private String userId;
    public String getUserId()
    {
        return userId;
    }
    public void setUserId(String data)
    {
        this.userId = data;
    }
    private static Singleton instance = null;

    public static synchronized Singleton getInstance(){
        if(null == instance){
            instance = new Singleton();
        }
        return instance;
    }
}
