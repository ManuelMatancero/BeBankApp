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
import com.manuelsarante.bebankapp.room.dao.UserCredentialsDao;
import com.manuelsarante.bebankapp.room.database.AppDatabase;
import com.manuelsarante.bebankapp.room.models.UserCredentials;
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

    //Database Variables
    AppDatabase db;
    UserCredentialsDao userCredentialsDao;
    UserCredentials userCredentials = new UserCredentials();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        user = findViewById(R.id.user);
        pass = findViewById(R.id.password);
        btnLogin = findViewById(R.id.button);
        progressBar = findViewById(R.id.progresbar);

        //Conection and creation of the database
        db = AppDatabase.getInstance(Login.this);
        userCredentialsDao = db.userCredentialsDao();

        //Check If user did log in
        checkIfUserLoged();


        LoginDto log = new LoginDto();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getText().toString().isEmpty()){
                    user.setError("Cannot be empty");
                    if(pass.getText().toString().isEmpty()){
                        pass.setError("Cannot be empty");
                    }
                }else if(pass.getText().toString().isEmpty()){
                    pass.setError("Cannot be empty");
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    log.setUser(user.getText().toString());
                    log.setPassword(pass.getText().toString());
                    login(log);
                }
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
                   //Passing data to the UserCredentials to save it in the database
                   userCredentials.setUser(user.getUser());
                   //Here i pass the password from the edit text to avoid pass the hash coded password from database
                   userCredentials.setPassword(pass.getText().toString());
                   userCredentials.setPin(user.getPin());
                   //Save the entrie once
                    userCredentialsDao.insertUserCredential(userCredentials);

                    progressBar.setVisibility(View.INVISIBLE);
                    Intent i = new Intent(Login.this, MainActivity.class);
                    i.putExtra("user", user);
                    startActivity(i);
                    finish();
                }else if(response.code()==404){
                    Toast.makeText(getApplicationContext(),"Incorrect Password or User not found", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    pass.setError("Incorrect Password");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Error:",t.getMessage());
                Toast.makeText(getApplicationContext(),"Check internet connection or try again later", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkIfUserLoged();
    }

    public void checkIfUserLoged(){
        //here if the table in the database is not empty it will open the pinActivity
        List<UserCredentials> userCredentials = userCredentialsDao.getAll();
        if(userCredentials.isEmpty()){

        }else{
            Intent i = new Intent(Login.this, LoginPin.class);
            startActivity(i);
        }
    }
}