package com.example.star.imhi.mina;

/**
 * Created by 11599 on 2018/1/9.
 */

public class Protocol {


    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Integer getType() {
        return type;
    }


    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setType(Integer type) {
        this.type = type;
    }


    private  String from;
    private  String to;
    private  Integer type;
    private  Integer texttype;
    private  Object textcontent;

    public void setTexttype(Integer texttype) {
        this.texttype = texttype;
    }

    public void setTextcontent(Object textcontent) {
        this.textcontent = textcontent;
    }

    public Integer getTexttype() {

        return texttype;
    }

    public Object getTextcontent() {
        return textcontent;
    }











}
