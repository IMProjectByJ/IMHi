package com.example.star.imhi.DAO.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by star on 18-1-5.
 */

public class User  {

    private  Integer userId;
    private  String phoneNum;
    private  String nikname;
    private  String headUrl;
    private  String gender;
    private  Integer age;
    private  String userPassword;
    private  String birth;
    private  String motto;

    public User(){}
    public User(String phoneNum){
        this.phoneNum = phoneNum;
    }
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", phoneNum='" + phoneNum + '\'' +
                ", nikname='" + nikname + '\'' +
                ", headUrl='" + headUrl + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", userPassword='" + userPassword + '\'' +
                ", birth='" + birth + '\'' +
                ", motto='" + motto + '\'' +
                '}';
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getNikname() {
        return nikname;
    }

    public void setNikname(String nikname) {
        this.nikname = nikname;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }
}
