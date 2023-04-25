package com.manuelsarante.bebankapp.room.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.manuelsarante.bebankapp.room.models.IpAddress;


import java.util.List;

@Dao
public interface IpAddressDao {

    @Query("SELECT * FROM IpAddress")
    List<IpAddress> getAll();

    @Query("UPDATE IpAddress SET ip =:uIp WHERE idAddress =:idAddress ")
    void updateIp(int idAddress, String uIp);

    @Insert
    void insertIpAddress(IpAddress ip);

    @Delete
    void deleteIpAddresss(IpAddress ip);

}
