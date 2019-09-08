package com.example.maadela;

public class Requests {
    String cusname;
    String shopname;
    String fishname;
    String time;
    String fid;
    String acctime;
    String status;
    String amount;
    String reqid;

    public Requests() { }

    public String getReqid() {
        return reqid;
    }

    public void setReqid(String reqid) {
        this.reqid = reqid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAcctime() { return acctime; }

    public void setAcctime(String acctime) { this.acctime = acctime; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getCusname() { return cusname; }

    public void setCusname(String cusname) { this.cusname = cusname; }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getFishname() {
        return fishname;
    }

    public void setFishname(String fishname) {
        this.fishname = fishname;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }
}
