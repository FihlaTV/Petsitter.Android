package com.example.katsutoshi.petsitter.model;

/**
 * Created by Katsutoshi on 19/06/2017.
 */

public class Agenda {

    private String uid;
    private String description;
    private String notificationTitle;
    private String date;
    private String time;
    private String status;
    private Long millis;

    public Agenda() {}

    public Agenda(String uid, String notificationTitle, String description, Long millis, String date, String time) {
        this.uid = uid;
        this.description = description;
        this.notificationTitle = notificationTitle;
        this.millis = millis;
        this.date = date;
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getMillis() {
        return millis;
    }

    public void setMillis(Long millis) {
        this.millis = millis;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
