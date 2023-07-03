package com.manuelsarante.bebankapp.room.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.manuelsarante.bebankapp.room.dao.IpAddressDao;
import com.manuelsarante.bebankapp.room.dao.JwebTokenDao;
import com.manuelsarante.bebankapp.room.dao.UserCredentialsDao;
import com.manuelsarante.bebankapp.room.models.IpAddress;

import com.manuelsarante.bebankapp.room.models.JwebToken;
import com.manuelsarante.bebankapp.room.models.UserCredentials;
/*
 * Copyright (c) Manuel Antonio Sarante Sanchez 2023
 * All rights reserved.
 */
@Database(entities = {UserCredentials.class, IpAddress.class, JwebToken.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public static AppDatabase INSTANCE;

    public abstract UserCredentialsDao userCredentialsDao();
    public abstract IpAddressDao ipAddressDao();

    public abstract JwebTokenDao jwebTokenDao();

    public static AppDatabase getInstance(Context context){
        if(INSTANCE==null){

            INSTANCE = Room.databaseBuilder(context, AppDatabase.class, "usercredentials.db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();

        }

        return INSTANCE;

    }
}
