
package com.univ.team12.navar.network;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.univ.team12.navar.network.message.Status;

public class MessageResponse {

    @SerializedName("status")
    @Expose
    private List<Status> status = null;

    @SerializedName("username")
    @Expose
    private String username;

    public List<Status> getStatus() {
        return status;
    }

    public void setStatus(List<Status> status) {
        this.status = status;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
