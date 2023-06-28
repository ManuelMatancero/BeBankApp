package com.manuelsarante.bebankapp.api;

import com.manuelsarante.bebankapp.dto.AuthenticationResponse;
import com.manuelsarante.bebankapp.dto.LoginDto;
import com.manuelsarante.bebankapp.dto.LoginWithPinDto;
import com.manuelsarante.bebankapp.dto.Messages;
import com.manuelsarante.bebankapp.dto.TransactionDto;
import com.manuelsarante.bebankapp.models.AccountTransactions;
import com.manuelsarante.bebankapp.models.User;

import java.util.List;

import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http2.Http2;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApi {

    @GET("user/list")
    Call<List<User>> getUsers();

    @POST("user/login")
    Call<User> login(@Header ("Authorization") String token, @Body LoginDto loginDto);

    @POST("user/pinlogin")
    Call<User> loginPin(@Header ("Authorization") String token, @Body LoginWithPinDto loginWithPinDto);

    @POST("user/save")
    Call<Messages> createUser(@Body User user);

    @GET("user/{id}")
    Call<User> getCliente(@Path("id") int userId);

    //With this endpoint we reicive the jwtoken in the header of the request
    @POST("jwt")
    Call<ResponseBody> getToken(@Body LoginDto loginDto);

    @POST("transactions/transfer")
    Call<AccountTransactions> saveTransaction(@Header ("Authorization") String token, @Body TransactionDto transactionDto);

}
