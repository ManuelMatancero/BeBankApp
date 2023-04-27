package com.manuelsarante.bebankapp.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.manuelsarante.bebankapp.models.User;
import com.manuelsarante.bebankapp.room.models.UserCredentials;

import java.util.List;

@Dao
public interface UserCredentialsDao {

    @Query("SELECT * FROM UserCredentials")
    List<UserCredentials> getAll();

    @Query("UPDATE UserCredentials SET pin =:ePin WHERE idUserCredentials =:id")
    void updatePin(int id, String ePin);

    @Insert
    void insertUserCredential(UserCredentials userCredentials);

    @Delete
    void deleteUserCredentials(UserCredentials userCredentials);

}
