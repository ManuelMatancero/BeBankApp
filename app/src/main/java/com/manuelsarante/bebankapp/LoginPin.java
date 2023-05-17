package com.manuelsarante.bebankapp;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.manuelsarante.bebankapp.api.UserApi;
import com.manuelsarante.bebankapp.dto.LoginWithPinDto;
import com.manuelsarante.bebankapp.models.User;
import com.manuelsarante.bebankapp.room.dao.JwebTokenDao;
import com.manuelsarante.bebankapp.room.dao.UserCredentialsDao;
import com.manuelsarante.bebankapp.room.database.AppDatabase;
import com.manuelsarante.bebankapp.room.models.JwebToken;
import com.manuelsarante.bebankapp.room.models.UserCredentials;
import com.manuelsarante.bebankapp.utils.Apis;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPin extends AppCompatActivity {

    Button unlink;
    ImageButton next;
    EditText digit1, digit2, digit3, digit4;
    UserApi userApi;
    ProgressBar progressBar;
    StringBuilder pin = new StringBuilder();

    //Database variables
    AppDatabase db;
    UserCredentialsDao userCredentialsDao;
    JwebTokenDao jwebTokenDao;
    UserCredentials userCredentials = new UserCredentials();
    JwebToken jwebToken = new JwebToken();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_pin);

        unlink = findViewById(R.id.unlink);
        next= findViewById(R.id.log);
        digit1 = findViewById(R.id.digit1);
        digit2 = findViewById(R.id.digit2);
        digit3 = findViewById(R.id.digit3);
        digit4 = findViewById(R.id.digit4);
        progressBar =findViewById(R.id.progresbar);

        digit1.requestFocus();

        //Database instance
        db = AppDatabase.getInstance(LoginPin.this);
        userCredentialsDao = db.userCredentialsDao();
        jwebTokenDao = db.jwebTokenDao();

        //This code is to go to next textview when text is changed
        digit1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                digit2.requestFocus();
                pin.append(digit1.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        digit2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                digit3.requestFocus();
                pin.append(digit2.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        digit3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                digit4.requestFocus();
                pin.append(digit3.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        digit4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                next.requestFocus();
                pin.append(digit4.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        //This make the action to do login
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get the credentials saved locally
                UserCredentials userCredentials = userCredentialsDao.getAll().get(0);
                progressBar.setVisibility(View.VISIBLE);
                LoginWithPinDto log = new LoginWithPinDto();
                log.setUser(userCredentials.getUser());
                log.setPassword(userCredentials.getPassword());
                log.setPin(pin.toString());
                login(log);

            }
        });

        //Click to unlink app to log in again
        unlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userCredentials = userCredentialsDao.getAll().get(0);
                userCredentialsDao.deleteUserCredentials(userCredentials);
                Intent i= new Intent(LoginPin.this, Login.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void login(LoginWithPinDto loginDto){
        Apis api = new Apis();
        UserApi userApi = api.getUser();
        Call<User> call = userApi.loginPin(loginDto);//////////////////////////////////////////In this line there is an error because i have to pass the token as parameter
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    User user = response.body();
                    progressBar.setVisibility(View.INVISIBLE);
                    Intent i = new Intent(LoginPin.this, MainActivity.class);
                    i.putExtra("user", user);
                    startActivity(i);
                    finish();
                }else if(response.code()==404){
                    Toast.makeText(getApplicationContext(),"Incorrect PIN", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    digit1.setText("");
                    digit2.setText("");
                    digit3.setText("");
                    digit4.setText("");
                    digit1.requestFocus();
                    pin.setLength(0);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Error:",t.getMessage());
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),"Something wrong happened", Toast.LENGTH_LONG).show();
            }

        });

    }
}