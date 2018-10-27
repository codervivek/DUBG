
package com.univ.team12.navar.network;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.univ.team12.navar.network.type.S;

public class TypeResponse {
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("s")
    @Expose
    private List<S> s = null;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public List<S> getS() {
        return s;
    }

    public void setS(List<S> s) {
        this.s = s;
    }

}
