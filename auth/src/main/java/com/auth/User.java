package com.auth;

import com.google.gson.annotations.SerializedName;

/**
 * Created by clickapps on 28/2/18.
 */

public class User {

    @SerializedName("sub")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("given_name")
    private String firstName;

    @SerializedName("family_name")
    private String lastName;

    @SerializedName("profile")
    private String profile;

    @SerializedName("picture")
    private String image;

    @SerializedName("email")
    private String email;

    @SerializedName("email_verified")
    private boolean emailVerified;

    @SerializedName("gender")
    private String gender;

    @SerializedName("locale")
    private String locale;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getProfile() {
        return profile;
    }

    public String getImage() {
        return image;
    }

    public String getEmail() {
        return email;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public String getGender() {
        return gender;
    }

    public String getLocale() {
        return locale;
    }
}
