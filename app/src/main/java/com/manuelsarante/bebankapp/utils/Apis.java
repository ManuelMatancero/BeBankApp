package com.manuelsarante.bebankapp.utils;

import com.manuelsarante.bebankapp.api.UserApi;

public class Apis {
    public static final String URL_0001 = "http://192.168.56.1:8080/";

    public static UserApi getUser(){
        return UserRetro.getUser(URL_0001).create(UserApi.class);
    }
}
