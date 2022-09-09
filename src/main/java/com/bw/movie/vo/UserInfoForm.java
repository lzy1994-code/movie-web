package com.bw.movie.vo;

import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xyj on 2018/7/19.
 */
public class UserInfoForm implements Serializable {

    @FormParam("nickName")
    private String nickName;

    @FormParam("phone")
    private String phone;

    @FormParam("pwd")
    private String pwd;

    @FormParam("pwd2")
    private String pwd2;

    @FormParam("sex")
    private int sex;

    @FormParam("email")
    private String email;

    @FormParam("birthday")
    private String birthday;

    @HeaderParam("ak")
    private String ak;

    @HeaderParam("userId")
    private int userId;

    @FormParam("imei")
    private String imei;

    @FormParam("ua")
    private String ua;

    @FormParam("screenSize")
    private String screenSize;

    @FormParam("os")
    private String os;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPwd2() {
        return pwd2;
    }

    public void setPwd2(String pwd2) {
        this.pwd2 = pwd2;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        if (birthday == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(birthday);
        } catch (ParseException e) {
            return null;
        }
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAk() {
        return ak;
    }

    public void setAk(String ak) {
        this.ak = ak;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    public String getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(String screenSize) {
        this.screenSize = screenSize;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserInfoForm{" +
                "nickName='" + nickName + '\'' +
                ", phone='" + phone + '\'' +
                ", pwd='" + pwd + '\'' +
                ", pwd2='" + pwd2 + '\'' +
                ", sex=" + sex +
                ", email='" + email + '\'' +
                ", birthday='" + birthday + '\'' +
                ", ak='" + ak + '\'' +
                ", userId=" + userId +
                ", imei='" + imei + '\'' +
                ", ua='" + ua + '\'' +
                ", screenSize='" + screenSize + '\'' +
                ", os='" + os + '\'' +
                '}';
    }
}
