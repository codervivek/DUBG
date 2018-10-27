
package com.univ.team12.navar.network.poi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fields {

    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("postedby")
    @Expose
    private Integer postedby;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("people_stuck")
    @Expose
    private Integer peopleStuck;
    @SerializedName("people_injured")
    @Expose
    private Integer peopleInjured;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Integer getPostedby() {
        return postedby;
    }

    public void setPostedby(Integer postedby) {
        this.postedby = postedby;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPeopleStuck() {
        return peopleStuck;
    }

    public void setPeopleStuck(Integer peopleStuck) {
        this.peopleStuck = peopleStuck;
    }

    public Integer getPeopleInjured() {
        return peopleInjured;
    }

    public void setPeopleInjured(Integer peopleInjured) {
        this.peopleInjured = peopleInjured;
    }

}
