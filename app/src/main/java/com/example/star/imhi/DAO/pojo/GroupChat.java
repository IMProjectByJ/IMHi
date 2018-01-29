package com.example.star.imhi.DAO.pojo;

/**
 * Created by 11599 on 2018/1/17.
 */

public class GroupChat {
    private  String groupId;
    private  String userId;
    private  String  groupName;
    private  String  createDate;
    private String headUrl;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    @Override
    public String toString() {
        return "GroupChat{" +
                "groupId='" + groupId + '\'' +
                ", userId='" + userId + '\'' +
                ", groupName='" + groupName + '\'' +
                ", createDate='" + createDate + '\'' +
                ", headUrl='" + headUrl + '\'' +
                '}';
    }
}
