package com.epayeats.epayeatsadmin.Model;

public class BusinessModel
{
    private String location;
    private String businessId;
    private String longitute;
    private String latitude;
    private String date;

    private String temp1;
    private String temp2;

    public BusinessModel(){}

    public BusinessModel(String location, String businessId, String longitute, String latitude, String date, String temp1, String temp2) {
        this.location = location;
        this.businessId = businessId;
        this.longitute = longitute;
        this.latitude = latitude;
        this.date = date;
        this.temp1 = temp1;
        this.temp2 = temp2;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getLongitute() {
        return longitute;
    }

    public void setLongitute(String longitute) {
        this.longitute = longitute;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTemp1() {
        return temp1;
    }

    public void setTemp1(String temp1) {
        this.temp1 = temp1;
    }

    public String getTemp2() {
        return temp2;
    }

    public void setTemp2(String temp2) {
        this.temp2 = temp2;
    }
}
