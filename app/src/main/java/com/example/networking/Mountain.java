package com.example.networking;

import com.google.gson.annotations.SerializedName;

public class Mountain {
    @SerializedName("ID")
    private String id;
    private String name;
    private String type;
    private String company;
    private String location;
    private String category;
    private int size;
    private int cost;
    private Auxdata auxdata;

    /*public Mountain() {
        name = "Kebenekaise";   trevlig konstruktor
    }*/

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getCompany() {
        return company;
    }

    public String getLocation() {
        return location;
    }

    public String getCategory() {
        return category;
    }

    public int getSize() {
        return size;
    }

    public int getCost() {
        return cost;
    }

    public String getName() {
        return name;
    }

    public Auxdata getAuxdata() {
        return auxdata;
    }

    @Override
    public String toString() {      //toString är vad som alltid kommer skrivas ut då ett objekt kallas på
        return name;               // om bara objeket anropas kommer toString automatiskt att anropas.
    }


}

