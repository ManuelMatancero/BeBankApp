package com.manuelsarante.bebankapp.dto;

import com.google.gson.annotations.SerializedName;

public class AuthenticationResponse {

    private String token;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
