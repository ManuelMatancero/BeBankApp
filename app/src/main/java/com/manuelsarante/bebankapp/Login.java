package com.manuelsarante.bebankapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.manuelsarante.bebankapp.api.UserApi;
import com.manuelsarante.bebankapp.dto.LoginDto;
import com.manuelsarante.bebankapp.models.BankingAccount;
import com.manuelsarante.bebankapp.models.User;
import com.manuelsarante.bebankapp.utils.Apis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity{

    EditText user, pass;
    Button btnLogin;
    UserApi userApi;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        user = findViewById(R.id.user);
        pass = findViewById(R.id.password);
        btnLogin = findViewById(R.id.button);
        progressBar = findViewById(R.id.progresbar);

        LoginDto log = new LoginDto();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                log.setUser(user.getText().toString());
                log.setPassword(pass.getText().toString());
                login(log);
            }
        });

    }

    public void login(LoginDto loginDto){
        userApi = Apis.getUser();
        Call<User> call = userApi.login(loginDto);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                   User user = response.body();
                    progressBar.setVisibility(View.INVISIBLE);
                    Intent i = new Intent(Login.this, MainActivity.class);
                    i.putExtra("user", user);
                    startActivity(i);
                }else if(response.code()==404){
                    Toast.makeText(getApplicationContext(),"Incorrect Password or User not found", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    pass.setError("Incorrect Password");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Error:",t.getMessage());
            }

        });

    }
}