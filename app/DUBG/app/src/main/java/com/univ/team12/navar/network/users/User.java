
package com.univ.team12.navar.network.users;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("fields")
    @Expose
    private Fields fields;
    @SerializedName("pk")
    @Expose
    private Integer pk;
    @SerializedName("model")
    @Expose
    private String model;

    public Fields getFields() {
        return fields;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

}
