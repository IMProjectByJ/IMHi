package com.example.star.imhi.DAO.pojo;

import java.util.Date;

/**
 * Created by 11599 on 2018/1/11.
 */

public class Loginlnfo {
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setIp_addr(String ip_addr) {
        this.ip_addr = ip_addr;
    }

    public void setIp_port(String ip_port) {
        this.ip_port = ip_port;
    }

    public void setLogin_date(Date login_date) {
        this.login_date = login_date;
    }

    public int getUser_id(int i) {

        return user_id;
    }

    public String getIp_addr() {
        return ip_addr;
    }

    public String getIp_port() {
        return ip_port;
    }

    public Date getLogin_date() {
        return login_date;
    }

    private  int user_id;
    private  String ip_addr;
    private  String ip_port;
    private Date  login_date;

}
