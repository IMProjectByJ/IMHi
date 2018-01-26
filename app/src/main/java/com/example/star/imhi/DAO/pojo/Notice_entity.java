package com.example.star.imhi.DAO.pojo;

import android.widget.Button;

/**
 * Created by d c on 2018/1/19.
 */

public class Notice_entity {
    private String who;
    private String whoid;

    public String getWhoid() {
        return whoid;
    }

    public void setWhoid(String whoid) {
        this.whoid = whoid;
    }

    private String addfriend;
    private Button agree;
    private Button refuse;

    public String getWho() {
        return who;
    }

    public void setWho(String name) {
        this.who = name;
    }

    public String getAddfriend() {
        return addfriend;
    }

    public void setAddfriend(String user_id) {
        this.addfriend = user_id;
    }




    public Button getRefuse() {
        return refuse;
    }

    public void setRefuse(Button button) {
        this.refuse=button;
    }

    public Button getAgree() {
        return agree;
    }

    public void setAgree(Button button) {
        this.agree=button;
    }

    public Notice_entity(){

    }
    public Notice_entity(String who,String whoid){
        this.who=who;
        this.whoid = whoid;
//        this.addfriend =addfriend;
//        this.agree=button1;
//        this.refuse = button2;
    }
}
