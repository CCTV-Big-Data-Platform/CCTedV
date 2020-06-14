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
    /*
     * 이 Singleton은 애플리케이션이 시작될 때 어떤 클래스가 최초 한번만 메모리를 할당하고(Static) 그 메모리에 인스턴스를 만들어 사용하는 클래스입니다.
     * 유저 정보를 저장하고 접근할 때, 사용됩니다.
     * */
    public static synchronized Singleton getInstance(){
        if(null == instance){
            instance = new Singleton();
        }
        return instance;
    }
}
