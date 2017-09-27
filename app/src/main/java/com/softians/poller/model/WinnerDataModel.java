package com.softians.poller.model;

/**
 * Created by HP on 16-09-2017.
 */

public class WinnerDataModel {
    public int id;
    public String name,image,pDate;

    public WinnerDataModel(int id, String name, String image, String pDate) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.pDate = pDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getpDate() {
        return pDate;
    }

    public void setpDate(String pDate) {
        this.pDate = pDate;
    }
}
