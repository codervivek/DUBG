
package com.univ.team12.navar.network;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.univ.team12.navar.network.place.S;

public class PlaceResponse {

    @SerializedName("s")
    @Expose
    private List<S> s = null;

    public List<S> getS() {
        return s;
    }

    public void setS(List<S> s) {
        this.s = s;
    }

}
