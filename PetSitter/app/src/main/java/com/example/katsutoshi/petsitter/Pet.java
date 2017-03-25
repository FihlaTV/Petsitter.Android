package com.example.katsutoshi.petsitter;

/**
 * Created by Katsutoshi on 24/03/2017.
 */

public class Pet {
    private String uid;
    private String name;
    private int age;
    private double weight;
    private String birthDate;

    public Pet ()
    {

    }

    public Pet(String uid, String name, double weight, String birthDate) {
        this.uid = uid;
        this.name = name;
        this.weight = weight;
        this.birthDate = birthDate;
        //this.age = calcAge(birthDate);
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
}
