package com.example.maadela;

public class Fish {
    String name;
    int price;

    public Fish() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return
                 name + ":\t\t\t" +
                "Rs: " + price ;
    }
}
