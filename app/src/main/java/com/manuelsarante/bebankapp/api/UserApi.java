package com.manuelsarante.bebankapp.api;

import com.manuelsarante.bebankapp.dto.LoginDto;
import com.manuelsarante.bebankapp.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApi {

    @GET("user/list")
    Call<List<User>> getUsers();

    @POST("user/login")
    Call<User> login(@Body LoginDto loginDto);

    @GET("user/{id}")
    Call<User> getCliente(@Path("id") int userId);
}
