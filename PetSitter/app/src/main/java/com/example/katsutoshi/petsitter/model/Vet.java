package com.example.katsutoshi.petsitter.model;

/**
 * Created by Katsutoshi on 23/04/2017.
 */

public class Vet {

    private String uid;
    private String description;
    private String vetName;
    private String consultDate;

    public Vet() {}

    public Vet(String uid, String description, String vetName, String consultDate) {
        this.uid = uid;
        this.description = description;
        this.vetName = vetName;
        this.consultDate = consultDate;
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

    public String getVetName() {
        return vetName;
    }

    public void setVetName(String vetName) {
        this.vetName = vetName;
    }

    public String getConsultDate() {
        return consultDate;
    }

    public void setConsultDate(String consultDate) {
        this.consultDate = consultDate;
    }
}
