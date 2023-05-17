package com.manuelsarante.bebankapp.room.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.manuelsarante.bebankapp.room.models.JwebToken;

import java.util.List;

@Dao
public interface JwebTokenDao {
    @Query("SELECT * FROM JwebToken")
    List<JwebToken> getAll();

    @Insert
    void insertJwt(JwebToken jsonWebToken);

    @Query("UPDATE JwebToken SET jsonWebToken = :jwt WHERE idJsonWebToken = :id ")
    void updateJwt( int id, String jwt);

    @Delete
    void deleteJwt(JwebToken jsonWebToken);

}
