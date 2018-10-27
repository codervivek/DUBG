
package com.univ.team12.navar.network.type;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fields {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("engaged")
    @Expose
    private Object engaged;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("user")
    @Expose
    private Integer user;
    @SerializedName("longitude")
    @Expose
    private String longitude;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getEngaged() {
        return engaged;
    }

    public void setEngaged(Object engaged) {
        this.engaged = engaged;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}
