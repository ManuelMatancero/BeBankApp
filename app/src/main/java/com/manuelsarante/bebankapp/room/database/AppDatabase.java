package com.manuelsarante.bebankapp.room.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.manuelsarante.bebankapp.room.dao.IpAddressDao;
import com.manuelsarante.bebankapp.room.dao.UserCredentialsDao;
import com.manuelsarante.bebankapp.room.models.IpAddress;
import com.manuelsarante.bebankapp.room.models.UserCredentials;

@Database(entities = {UserCredentials.class, IpAddress.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public static AppDatabase INSTANCE;

    public abstract UserCredentialsDao userCredentialsDao();
    public abstract IpAddressDao ipAddressDao();

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
