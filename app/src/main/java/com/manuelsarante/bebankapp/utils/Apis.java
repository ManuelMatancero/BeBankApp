package com.manuelsarante.bebankapp.utils;

import com.manuelsarante.bebankapp.Login;
import com.manuelsarante.bebankapp.api.UserApi;
import com.manuelsarante.bebankapp.room.dao.IpAddressDao;
import com.manuelsarante.bebankapp.room.database.AppDatabase;

public class Apis {

    public AppDatabase db = AppDatabase.getInstance(Login.loginActivity);
    public  IpAddressDao ipAddressDao = db.ipAddressDao();
    //here i get the ipAddress saved in the local database
    public  String URL_0001 = "http://"+ipAddressDao.getAll().get(0).getIp();

    public UserApi getUser(){
        return UserRetro.getUser(URL_0001).create(UserApi.class);
    }
}
