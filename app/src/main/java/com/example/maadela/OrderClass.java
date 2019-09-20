package com.example.maadela;

public class OrderClass {

    private String type;
    private double amount;
    private String date;
    private String CustomerName;
    private String SellerName ;
    private String status;
    private String CustomerContact;
    private String SellerContact;
    private String id;
    private double totprice;

    public double getTotprice() {
        return totprice;
    }

    public void setTotprice(double totprice) {
        this.totprice = totprice;
    }

    public String getSellerContact() {
        return SellerContact;
    }

    public void setSellerContact(String sellerContact) {
        SellerContact = sellerContact;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getSellerName() {
        return SellerName;
    }

    public void setSellerName(String sellerName) {
        SellerName = sellerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerContact() {
        return CustomerContact;
    }

    public void setCustomerContact(String customerContact) {
        CustomerContact = customerContact;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }




    public OrderClass() {
    }


}
