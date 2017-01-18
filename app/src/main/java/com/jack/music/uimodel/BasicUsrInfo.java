package com.jack.music.uimodel;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by liuyang on 2016/12/24.
 */

public class BasicUsrInfo implements Serializable{
    private Drawable headImg;
    private String nickName;
    private String registerTime;
    private String loginFrequency;

    public Drawable getHeadImg() {
        return headImg;
    }

    public String getNickName() {
        return nickName;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public String getLoginFrequency() {
        return loginFrequency;
    }

    public void setHeadImg(Drawable headImg) {
        this.headImg = headImg;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public void setLoginFrequency(String loginFrequency) {
        this.loginFrequency = loginFrequency;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(TextUtils.isEmpty(nickName) ? "Nick name is empty" : nickName);
        sb.append(TextUtils.isEmpty(loginFrequency) ? "loginFrequency" : loginFrequency);
        sb.append(TextUtils.isEmpty(registerTime) ? "registerTime" : registerTime);
        return sb.toString();
    }
}
