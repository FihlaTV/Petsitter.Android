package com.example.katsutoshi.petsitter.model;

/**
 * Created by Katsutoshi on 23/04/2017.
 */

public class PetFood {

    private String uid;
    private String petFoodName;
    private String description;
    private String petFoodDate;
    private String petFoodQnt;

    public PetFood() {
    }

    public PetFood(String uid, String petFoodName, String description, String petFoodQnt) {
        this.uid = uid;
        this.petFoodName = petFoodName;
        this.description = description;
        this.petFoodQnt = petFoodQnt;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPetFoodName() {
        return petFoodName;
    }

    public void setPetFoodName(String petFoodName) {
        this.petFoodName = petFoodName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPetFoodDate() {
        return petFoodDate;
    }

    public void setPetFoodDate(String petFoodDate) {
        this.petFoodDate = petFoodDate;
    }

    public String getPetFoodQnt() {
        return petFoodQnt;
    }

    public void setPetFoodQnt(String petFoodQnt) {
        this.petFoodQnt = petFoodQnt;
    }
}
