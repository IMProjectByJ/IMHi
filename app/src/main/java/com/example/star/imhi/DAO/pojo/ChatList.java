package com.example.star.imhi.DAO.pojo;

/**
 * Created by d c on 2018/1/15.
 */

public class ChatList  {
    private int imageid;
    private String fromwho,whatcontext;
    public ChatList(String fromwho,String whatcontext){
        this.fromwho = fromwho;
        this.whatcontext = whatcontext;
    //    this.imageid = imageid;
    }
    public String getFromwho(){
        return fromwho;
    }
    public  String getWhatcontext(){
        return  whatcontext;
    }
//    public int getImageid(){
//        return imageid;
//    }
}
