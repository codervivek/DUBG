
package com.univ.team12.navar.network;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.univ.team12.navar.network.poi.Status;

public class PoiResponse {

    @SerializedName("status")
    @Expose
    private List<Status> status = null;

    public List<Status> getStatus() {
        return status;
    }

    public void setStatus(List<Status> status) {
        this.status = status;
    }

}
