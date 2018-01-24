package com.example.star.imhi.DAO.pojo;

/**
 * Created by d c on 2018/1/15.
 */

public class ChatList {
    private int imageid;
    private Integer old_id;
    private int type;
    private String fromwho, whatcontext;

    public Integer getOld_id() {
        return old_id;
    }

    public void setOld_id(Integer old_id) {
        this.old_id = old_id;
    }

    public String getNikname() {
        return nikname;
    }

    public void setNikname(String nikname) {
        this.nikname = nikname;
    }

    private  String nikname;
    public ChatList(String fromwho,int type,String whatcontext,String nikname) {
        this.fromwho = fromwho;
        this.type = type;
        this.whatcontext = whatcontext;
        this.nikname = nikname;
        //    this.imageid = imageid;
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
