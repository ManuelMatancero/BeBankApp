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
import com.manuelsarante.bebankapp.dto.Messages;
import com.manuelsarante.bebankapp.models.User;
import com.manuelsarante.bebankapp.utils.Apis;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/*
 * Copyright (c) Manuel Antonio Sarante Sanchez 2023
 * All rights reserved.
 */
public class Register extends AppCompatActivity {

    EditText txtFullName, email, txtUser, password, cPassword, pin, cPin;
    Button create;
    ImageButton goBack;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtFullName = findViewById(R.id.fullName);
        email = findViewById(R.id.email);
        txtUser = findViewById(R.id.user);
        password = findViewById(R.id.password);
        cPassword = findViewById(R.id.cPassword);
        pin = findViewById(R.id.pin);
        cPin = findViewById(R.id.cPin);
        create = findViewById(R.id.createUser);
        progressBar = findViewById(R.id.progresbar);
        goBack = findViewById(R.id.goBack);
        List<TextView> textViews = new ArrayList<>();

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Here i start the progressbar
                progressBar.setVisibility(View.VISIBLE);
                //create a bool variable  to confirm if there are textviews emptys
                boolean emptyValues = false;
                //Add all the textviews to a list
                textViews.add(txtFullName);
                textViews.add(email);
                textViews.add(txtUser);
                textViews.add(password);
                textViews.add(cPassword);
                textViews.add(pin);
                textViews.add(cPin);
                //here confirm that all textviews are filled and if the email has an @
                for (TextView textView : textViews){
                    if(textView.getText().toString().trim().isEmpty()){
                        progressBar.setVisibility(View.INVISIBLE);
                        textView.setError("Can not be empty");
                        emptyValues = true;
                    }else if(!email.getText().toString().contains("@")){
                        progressBar.setVisibility(View.INVISIBLE);
                        email.setError("This has to be an email");
                        emptyValues = true;
                    }
                }
                //If there are not empty textviews it will run this
                if(!emptyValues){
                    //Here i create a user object to be persisted
                    User userToBePersist = new User();
                    userToBePersist.setName(txtFullName.getText().toString());
                    userToBePersist.setEmail(email.getText().toString());
                    userToBePersist.setUser(txtUser.getText().toString());
                    //if password and c password are equals it will set the password to de user
                    if(password.getText().toString().equals(cPassword.getText().toString())){
                        userToBePersist.setPassword(password.getText().toString());
                        //if pin is equal to cPin it will set the pin to the user
                        if(pin.getText().toString().equals(cPin.getText().toString())){
                            //here i confirm that the pin will be 4 digits
                            if(pin.getText().length() < 4){
                                progressBar.setVisibility(View.INVISIBLE);
                                pin.setError("PIN has to have 4 digits");
                                cPin.setError("PIN has to have 4 digits");
                            }else {
                                userToBePersist.setPin(pin.getText().toString());
                                //if pin and password are correct the user will be persisted
                                createUser(userToBePersist);
                            }
                        }else if(!pin.getText().toString().equals(cPin.getText().toString())){
                            progressBar.setVisibility(View.INVISIBLE);
                            cPin.setError("PINS are not equals");
                        }
                    }else if(!password.getText().toString().equals(cPassword.getText().toString())){
                        progressBar.setVisibility(View.INVISIBLE);
                        cPassword.setError("Passwords are not equals");
                    }
                }
            }
        });

        //Go back button
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    public void createUser (User user){
        Apis api = new Apis();
        UserApi userApi = api.getUser();
        Call<Messages> call = userApi.createUser(user);
        call.enqueue(new Callback<Messages>() {
            @Override
            public void onResponse(Call<Messages> call, Response<Messages> response) {
                if(response.code()==200) {
                    Messages messages = response.body();
                    assert messages != null;
                    if(messages.getMessage().equals("The user is already in use")){
                        progressBar.setVisibility(View.INVISIBLE);
                        txtUser.setError("Type another user");
                        Toast.makeText(getApplicationContext(), "The user is already in use", Toast.LENGTH_LONG).show();
                    }else if(messages.getMessage().equals("The email is already in use")){
                        progressBar.setVisibility(View.INVISIBLE);
                        email.setError("Type another email");
                        Toast.makeText(getApplicationContext(), "The email is already in use", Toast.LENGTH_LONG).show();
                    }else if(messages.getMessage().equals("User created successfully")){
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "User created successfully", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }else if(response.code()==404){
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "An error ocurred while saving user", Toast.LENGTH_LONG).show();
                }
                else if(response.code()==401){
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Unauthorized request", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Messages> call, Throwable t) {
                Log.e("Error:",t.getMessage());
                Toast.makeText(getApplicationContext(),"Check internet connection or check the URL", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }

        });

    }
}