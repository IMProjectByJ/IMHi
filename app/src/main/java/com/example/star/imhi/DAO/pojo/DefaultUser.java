package com.example.star.imhi.DAO.pojo;

import java.io.Serializable;

import cn.jiguang.imui.commons.models.IUser;

public class DefaultUser implements IUser, Serializable {

    private String id;
    private String displayName;
    private String avatar;
    private int type;


    public DefaultUser(String id, String displayName, String avatar, int type) {
        this.id = id;
        this.displayName = displayName;
        this.avatar = avatar;
        this.type = type;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getAvatarFilePath() {
        return avatar;
    }

    public String  getUserName(String userName) {
        return userName;
    }
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
