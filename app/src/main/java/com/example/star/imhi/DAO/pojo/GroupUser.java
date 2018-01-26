package com.example.star.imhi.DAO.pojo;

/**
 * Created by 11599 on 2018/1/17.
 */

public class GroupUser {
    private String group_id;
    private String member_id;

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getGroup_membership() {
        return group_membership;
    }

    public void setGroup_membership(String group_membership) {
        this.group_membership = group_membership;
    }
    private String group_membership;

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    private  String message_id;
}
