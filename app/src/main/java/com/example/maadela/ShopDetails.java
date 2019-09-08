package com.example.maadela;


import java.util.ArrayList;

public class ShopDetails {
    String ID;
    ArrayList<Fish> listFish;

    public ShopDetails() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public ArrayList<Fish> getListFish() {
        return listFish;
    }

    public void setListFish(ArrayList<Fish> listFish) {
        this.listFish = listFish;
    }
}
