package com.example.jiyanxin.loginui.others;

/**
 * Created by JIYANXIN on 2017/4/12.
 */

public class MyAccount {
    private String username;
    private String password;
    private String phoneNumber;
    private String email;
    private String height;
    private String weight;
    private int login_flag;

    private String name;
    private String value;
    private int imageId;

    public void setUsername(String name){
        this.username = name;
    }
    public String getUsername(){
        return this.username;
    }

    public void setPassword(String pwd){
        this.password = pwd;
    }
    public String getPassword(){
        return this.password;
    }

    public void setPhoneNumber(String phone){
        this.phoneNumber = phone;
    }
    public String getPhoneNumber(){
        return this.phoneNumber;
    }

    public void setEmail(String mail){
        this.email = mail;
    }
    public String getEmail(){
        return this.email;
    }

    public void setHeight(String h){
        this.height = h;
    }
    public String getHeight(){
        return this.height;
    }

    public void setWeight(String w){
        this.weight = w;
    }
    public String getWeight(){
        return this.weight;
    }

    public void setLoginFlag(int loginFlag){
        this.login_flag = loginFlag;
    }
    public int getLogin_flag(){
        return this.login_flag;
    }

    public MyAccount(String _name,int _imageId,String _value){
        this.name = _name;
        this.value = _value;
        this.imageId = _imageId;
    }
    public String getName() {
        return name;
    }
    public String getValue(){
        return value;
    }
    public int getImageId() {
        return imageId;
    }

}
