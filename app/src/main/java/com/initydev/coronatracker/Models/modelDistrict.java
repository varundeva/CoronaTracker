package com.initydev.coronatracker.Models;

public class modelDistrict {
    private String districtName, confirmed;

    public modelDistrict() {
    }

    public modelDistrict(String districtName, String confirmed) {
        this.districtName = districtName;
        this.confirmed = confirmed;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }
}
