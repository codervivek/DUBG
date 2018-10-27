
package com.univ.team12.navar.network.place;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fields {

    @SerializedName("postedby")
    @Expose
    private Integer postedby;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("people_stuck")
    @Expose
    private Integer peopleStuck;
    @SerializedName("people_injured")
    @Expose
    private Integer peopleInjured;
    @SerializedName("name")
    @Expose
    private String name;

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

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
