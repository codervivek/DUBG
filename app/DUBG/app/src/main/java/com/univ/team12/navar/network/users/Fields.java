
package com.univ.team12.navar.network.users;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fields {

    @SerializedName("last_login")
    @Expose
    private String lastLogin;
    @SerializedName("is_staff")
    @Expose
    private Boolean isStaff;
    @SerializedName("user_permissions")
    @Expose
    private List<Object> userPermissions = null;
    @SerializedName("date_joined")
    @Expose
    private String dateJoined;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("groups")
    @Expose
    private List<Object> groups = null;
    @SerializedName("is_active")
    @Expose
    private Boolean isActive;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("is_superuser")
    @Expose
    private Boolean isSuperuser;

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Boolean getIsStaff() {
        return isStaff;
    }

    public void setIsStaff(Boolean isStaff) {
        this.isStaff = isStaff;
    }

    public List<Object> getUserPermissions() {
        return userPermissions;
    }

    public void setUserPermissions(List<Object> userPermissions) {
        this.userPermissions = userPermissions;
    }

    public String getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(String dateJoined) {
        this.dateJoined = dateJoined;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Object> getGroups() {
        return groups;
    }

    public void setGroups(List<Object> groups) {
        this.groups = groups;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsSuperuser() {
        return isSuperuser;
    }

    public void setIsSuperuser(Boolean isSuperuser) {
        this.isSuperuser = isSuperuser;
    }

}
