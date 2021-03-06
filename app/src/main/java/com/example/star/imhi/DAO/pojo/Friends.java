package com.example.star.imhi.DAO.pojo;

import android.graphics.Bitmap;

/**
 * Created by d c on 2018/1/9.
 */

public class Friends {
    private String name;
    private String user_id;
    private Bitmap imageid;
    private  Integer offline_msg;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Bitmap getImageid() {
        return imageid;
    }

    public void setImageid(Bitmap imageid) {
        this.imageid = imageid;
    }

    public Integer getOffline_msg() {
        return offline_msg;
    }

    public void setOffline_msg(int offline_msg) {
        this.offline_msg = offline_msg;
    }

    public Friends(){

    }
    public Friends(String user_id, String name, Bitmap imageid){
        this.user_id = user_id;
        this.name = name;
        this.imageid = imageid;
    }
}

