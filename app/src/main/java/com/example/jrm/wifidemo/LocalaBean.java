package com.example.jrm.wifidemo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.IllegalFormatCodePointException;
import java.util.List;

/**
 * Created by jrm on 2017-4-6.
 */

public class LocalaBean implements Parcelable{

    public static final Parcelable.Creator<LocalaBean> CREATOR;

    private String ssid; //wifi的名称  --->b
    private String caps ;//加密方式 ---> e
    private String linkedSpeed; //连接的速度 --->j
    private String ipAddress; //ip地址 ---->k
    private String  frequency; // 频率 ---> i
    private String title; //显示有无密码的名称 --->d
    private int level ; //信号强弱的等级 ---->f
    private int newId ; //网络Id --->a
    private int distance ;//距离 -->c
    private boolean untrusted ;// 是否保存wifi --->g
    private boolean isRead; //是否自动连接 ——->h

    static {
        CREATOR = new Parcelable.Creator<LocalaBean>() {
            @Override
            public LocalaBean createFromParcel(Parcel source) {
                return new LocalaBean(source);
            }
            @Override
            public LocalaBean[] newArray(int size) {
                return new LocalaBean[size];
            }
        };
    }

    public LocalaBean() {
    }

    protected LocalaBean(final Parcel parcel) {
        boolean isRead = true;
        this.newId = parcel.readInt();
        this.ssid = parcel.readString();
        this.distance = parcel.readInt();
        this.title = parcel.readString();
        this.caps = parcel.readString();
        this.level = parcel.readInt();
        this.untrusted = (parcel.readByte() != 0 && isRead);
        if (parcel.readByte() == 0) {
            isRead = false;
        }
        this.isRead = isRead;
        this.frequency = parcel.readString();
        this.linkedSpeed = parcel.readString();
        this.ipAddress = parcel.readString();
    }

    /**
     * 获取LocalBean的实例
     * @param ssid wifi的名称
     * @param list localBean对象的集合
     * @return
     */
    public static LocalaBean getInitLocalBean(final String ssid, final List<LocalaBean> list) {
        if (ssid != null){
            final String newSSID = ssid.replaceAll("\"", "");
            for (final LocalaBean localaBean : list) {
                if (newSSID.equals(localaBean.ssid)) {
                    return localaBean;
                }
            }
        }
        return null;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setNewId(final int netId){
        this.newId = netId;
    }

    public void setSsid(final String ssid) {
        this.ssid = ssid;
    }

    public void setDistance(final int distance){
        this.distance = distance;
    }

    public void setCaps(final String caps){
        this.caps = caps;
    }

    public void setUntrusted(final boolean untrusted) {
        this.untrusted = untrusted;
    }


    public void setLevel(final int level){
        this.level = level;
    }

    public void setLinkedSpeed(final String linkedSpeed){
        this.linkedSpeed = linkedSpeed;
    }

    public void setFrequency(final String frequency){
        this.frequency = frequency;
    }

    public int describeContents() {
        return 0;
    }
    public  void setTitle(final  String title){
        this.title = title;
    }

    public void setIpAddress(final String ip){
        this.ipAddress = ip;
    }


    public String getSsid() {
        return ssid;
    }

    public String getCaps() {
        return caps;
    }

    public String getLinkedSpeed() {
        return linkedSpeed;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getTitle() {
        return title;
    }

    public int getLevel() {
        return level;
    }

    public int getNewId() {
        return newId;
    }

    public int getDistance() {
        return distance;
    }

    public boolean isUntrusted() {
        return untrusted;
    }


    public void writeToParcel(final Parcel parcel, final int n) {
        int n2 = 1;
        parcel.writeInt(this.newId);
        parcel.writeString(this.ssid);
        parcel.writeInt(this.distance);
        parcel.writeString(this.title);
        parcel.writeString(this.caps);
        parcel.writeInt(this.level);
        int n3;
        if (this.untrusted) {
            n3 = n2;
        }
        else {
            n3 = 0;
        }
        parcel.writeByte((byte)n3);
        if (!this.isRead) {
            n2 = 0;
        }
        parcel.writeByte((byte)n2);
        parcel.writeString(this.frequency);
        parcel.writeString(this.linkedSpeed);
        parcel.writeString(this.ipAddress);
    }
}
