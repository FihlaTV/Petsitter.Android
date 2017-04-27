package com.example.katsutoshi.petsitter.model;

/**
 * Created by Katsutoshi on 23/04/2017.
 */

public class Medication {

    private String uid;
    private String description;
    private String medicineName;
    private String medDate;

    public Medication() {}

    public Medication(String uid, String description, String medicineName, String medDate) {
        this.uid = uid;
        this.description = description;
        this.medicineName = medicineName;
        this.medDate = medDate;
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
        return medicineName;
    }

    public void setVetName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getConsultDate() {
        return medDate;
    }

    public void setConsultDate(String medDate) {
        this.medDate = medDate;
    }
}
