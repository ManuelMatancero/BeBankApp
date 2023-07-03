package com.manuelsarante.bebankapp.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/*
 * Copyright (c) Manuel Antonio Sarante Sanchez 2023
 * All rights reserved.
 */
public class UserRetro {
    public static Retrofit getUser(String url){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit;
    }
}
