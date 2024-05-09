package com.example.residentapplication;

public class Facilities {
    private String name;
    private String year;
    private String id;
    private String purpose;
    private String image;
    private String model;

    public Facilities(String year, String id, String purpose, String image, String model,String name) {
        this.year = year;
        this.id = id;
        this.purpose = purpose;
        this.image = image;
        this.model = model;
        this.name =name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
