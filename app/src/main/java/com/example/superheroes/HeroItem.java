package com.example.superheroes;

public class HeroItem {

    private String sImageUrl;
    private String sName;
    private int sID;
    public HeroItem(String imageUrl, String HeroName, int ID) {
        sImageUrl = imageUrl;
        sName = HeroName;
        sID = ID;
    }
    public String getImageUrl() {
        return sImageUrl;
    }
    public String getName() {
        return sName;
    }
    public int getID() {
        return sID;
    }

}
