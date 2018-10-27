
package com.univ.team12.navar.network.poi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Status {

    @SerializedName("pk")
    @Expose
    private Integer pk;
    @SerializedName("fields")
    @Expose
    private Fields fields;
    @SerializedName("model")
    @Expose
    private String model;

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public Fields getFields() {
        return fields;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

}
