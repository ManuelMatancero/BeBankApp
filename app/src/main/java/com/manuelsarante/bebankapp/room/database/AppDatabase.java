package com.manuelsarante.bebankapp.room.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.manuelsarante.bebankapp.room.dao.UserCredentialsDao;
import com.manuelsarante.bebankapp.room.models.UserCredentials;

@Database(entities = {UserCredentials.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public static AppDatabase INSTANCE;

    public abstract UserCredentialsDao userCredentialsDao();

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
