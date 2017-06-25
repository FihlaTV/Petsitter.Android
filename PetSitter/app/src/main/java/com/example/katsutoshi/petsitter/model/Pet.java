package com.example.katsutoshi.petsitter.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Katsutoshi on 24/03/2017.
 */

public class Pet {
    private String uid;
    private String name;
    private String age;
    private String weight;
    private String birthDate;
    private String healthRestrictions;
    private String race;
    private String genre;

    //Empty constructor
    public Pet () {}

    public Pet(String uid, String name, String weight, String birthDate) {
        setUid(uid);
        setName(name);
        setWeight(weight);
        setBirthDate(birthDate);
        //this.age = calcAge(birthDate);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {this.name = name;}

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getHealthRestrictions() {
        return healthRestrictions;
    }

    public void setHealthRestrictions(String healthRestrictions) {
        this.healthRestrictions = healthRestrictions;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
