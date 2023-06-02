package com.manuelsarante.bebankapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import com.manuelsarante.bebankapp.dto.AuthenticationResponse;
import com.manuelsarante.bebankapp.dto.LoginDto;
import com.manuelsarante.bebankapp.models.BankingAccount;
import com.manuelsarante.bebankapp.models.User;
import com.manuelsarante.bebankapp.room.dao.IpAddressDao;
import com.manuelsarante.bebankapp.room.dao.JwebTokenDao;
import com.manuelsarante.bebankapp.room.dao.UserCredentialsDao;
import com.manuelsarante.bebankapp.room.database.AppDatabase;
import com.manuelsarante.bebankapp.room.models.IpAddress;
import com.manuelsarante.bebankapp.room.models.JwebToken;
import com.manuelsarante.bebankapp.room.models.UserCredentials;
import com.manuelsarante.bebankapp.utils.Apis;


import java.util.Date;
import java.util.List;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login extends AppCompatActivity{
    String token;
    private static final Long ACCESS_TOKEN_VALIDITY_SECONDS = 7200L;
    long expirationTime = ACCESS_TOKEN_VALIDITY_SECONDS * 1000;
    LoginDto log = new LoginDto();
    EditText user, pass, ip;
    TextView forgotPass;
    Button btnLogin, createAccount;
    ImageButton add, imageButton;
    ProgressBar progressBar;
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    public static Login loginActivity;

    //Database Variables
    AppDatabase db;
    UserCredentialsDao userCredentialsDao;
    JwebTokenDao jwebTokenDao;
    IpAddressDao ipAddressDao;
    UserCredentials userCredentials = new UserCredentials();
    IpAddress ipAddress = new IpAddress();
    JwebToken jwebToken = new JwebToken();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Creating a static instance of login to be used in Apis.class
        loginActivity =this;

        user = findViewById(R.id.user);
        pass = findViewById(R.id.password);
        btnLogin = findViewById(R.id.button);
        progressBar = findViewById(R.id.progresbar);
        imageButton = findViewById(R.id.imageButton);
        forgotPass = findViewById(R.id.forgotPass);
        createAccount = findViewById(R.id.createUser);

        //Conection and creation of the database
        db = AppDatabase.getInstance(Login.this);
        userCredentialsDao = db.userCredentialsDao();
        ipAddressDao = db.ipAddressDao();
        jwebTokenDao = db.jwebTokenDao();

        //Check If user did log in
        checkIfUserLoged();

        //Button to takeyo to the regiter form
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isIpCreated()){
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
                        //here i get the jsonwebtoken fromn api and if the value of token is null it is goign to give me an error
                        //but in case that the response is successful it will run the login method
                        getJwtFromApi(log);
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Add the URL before login", Toast.LENGTH_LONG).show();
                    createDialog();
                }
            }
        });
        //here I create the dialog to add the ip address
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog();
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Login.this);
                builder.setTitle("Information");
                builder.setMessage(getResources().getString(R.string.pMessage));
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });

    }

    public void login(LoginDto loginDto){
        Apis api = new Apis();
        UserApi userApi = api.getUser();
            Call<User> call = userApi.login(token, loginDto);
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
                    else if(response.code()==401){
                        Toast.makeText(getApplicationContext(),"Access Unauthorized", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e("Error:",t.getMessage());
                    Toast.makeText(getApplicationContext(),"Check internet connection or check the URL", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }

            });

    }

    public void getJwtFromApi(LoginDto loginDto){

        Apis apis = new Apis();
        UserApi userApi = apis.getUser();
        Call<ResponseBody> call = userApi.getToken(loginDto);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code()==200) {
                    //Here i get the token from the header of the response
                    token = response.headers().get("Authorization");
                    //After i get the JWT i save it in a local database for a future use wen login with PIN
                    jwebToken.setJsonWebToken(token);
                    Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);
                    jwebToken.setExpirationDate(String.valueOf(expirationDate));
                    jwebTokenDao.insertJwt(jwebToken);
                    login(log);
                }
                else if(response.code()==401){
                    Toast.makeText(getApplicationContext(),"Password or User are incorrect", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Error:",t.getMessage());
                Toast.makeText(getApplicationContext(),"Check internet connection or check the URL", Toast.LENGTH_LONG).show();
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

    //Here shows the dialog to add the ip address
    public void createDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View editPopUp = getLayoutInflater().inflate(R.layout.popup, null);
        ip = (EditText) editPopUp.findViewById(R.id.ipAddress);
        add = (ImageButton) editPopUp.findViewById(R.id.add);

        dialogBuilder.setView(editPopUp);
        dialog = dialogBuilder.create();
        dialog.show();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If is created i only update the row
                if(isIpCreated()){
                    if(ip.getText().toString().trim().isEmpty()){
                        ip.setError("This can not be empty");
                    }else{
                        String ipAdd = ip.getText().toString();
                        int idIp = ipAddressDao.getAll().get(0).getIdAddress();
                        ipAddressDao.updateIp(idIp, ipAdd);
                        Toast.makeText(getApplicationContext(), "Ip address updated successfully", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                    //If it is not created add a new row with the IP
                }else{
                    if(ip.getText().toString().trim().isEmpty()){
                        ip.setError("This can not be empty");
                    }else{
                        ipAddress.setIp(ip.getText().toString());
                        ipAddressDao.insertIpAddress(ipAddress);
                        Toast.makeText(getApplicationContext(), "Ip address added successfully", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                }
            }
        });
    }

    public boolean isIpCreated(){
        //Here I check if the ipAddress was created or not
        //Check if ipAddress was created
        boolean isCreated;
        if(ipAddressDao.getAll().isEmpty()){
            isCreated = false;
        }else{
            isCreated = true;
        }
        return isCreated;
    }
}