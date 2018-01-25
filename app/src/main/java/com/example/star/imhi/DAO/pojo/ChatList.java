package com.example.star.imhi.DAO.pojo;

/**
 * Created by d c on 2018/1/15.
 */

public class ChatList {
    private int imageid;
    private Integer newid;
    private int type;
    private String fromwho;
    private String whatcontext;
    private  String nikname;
    private  String messagenum = "0";

    public ChatList(String fromwho,int type,Integer newid,String nikname,String messagenum) {
        this.fromwho = fromwho;
        this.type = type;
        this.newid = newid;
        this.nikname = nikname;
        this.messagenum = messagenum;
        //    this.imageid = imageid;
    }


    public String getMessagenum() {
        return messagenum;
    }

    public void setMessagenum(String messagenum) {
        this.messagenum = messagenum;
    }

    public Integer getNewid() {
        return newid;
    }

    public void setNewid(Integer newid) {
        this.newid = newid;
    }

    public String getNikname() {
        return nikname;
    }

    public void setNikname(String nikname) {
        this.nikname = nikname;
    }




    public int getImageid() {
        return imageid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ChatList() {
    }

    public void setImageid(int imageid) {
        this.imageid = imageid;
    }

    public void setFromwho(String fromwho) {
        this.fromwho = fromwho;
    }

    public void setWhatcontext(String whatcontext) {
        this.whatcontext = whatcontext;
    }

    public String getFromwho() {
        return fromwho;
    }

    public String getWhatcontext() {
        return whatcontext;
    }
//    public int getImageid(){
//        return imageid;
//    }
}
