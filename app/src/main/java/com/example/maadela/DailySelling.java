package com.example.maadela;

import androidx.annotation.NonNull;

import java.sql.Time;
import java.sql.Date;

public class DailySelling {
    private String ShopName;
    private String date;
    private String time;
    private String Fishname;
    private double rate;
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DailySelling() {
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFishname() {
        return Fishname;
    }

    public void setFishname(String fishname) {
        Fishname = fishname;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @NonNull
    @Override
    public String toString() {
        return
                Fishname + ":\t\t\t" +
                        "Rs: " + rate ;
    }
}
