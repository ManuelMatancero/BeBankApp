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
import com.manuelsarante.bebankapp.dto.TransactionDto;
import com.manuelsarante.bebankapp.models.AccountTransactions;
import com.manuelsarante.bebankapp.room.dao.JwebTokenDao;
import com.manuelsarante.bebankapp.room.database.AppDatabase;
import com.manuelsarante.bebankapp.room.models.JwebToken;
import com.manuelsarante.bebankapp.utils.Apis;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Transfer extends AppCompatActivity {

    EditText edtAccountToTransfer, edtAmountToTransfer;
    TextView txvTrasnferFrom;
    Button btnTransfer, btnCancel;
    ImageButton btnGoBack;
    ProgressBar progressBar;
    //Database instance to get the JWT from database
    AppDatabase db;
    JwebToken jwebToken = new JwebToken();
    JwebTokenDao jwebTokenDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        edtAccountToTransfer = findViewById(R.id.accToTransfer);
        edtAmountToTransfer = findViewById(R.id.amountToTransfer);
        txvTrasnferFrom = findViewById(R.id.transFrom);
        btnTransfer = findViewById(R.id.transfer);
        btnCancel = findViewById(R.id.cancel);
        btnGoBack = findViewById(R.id.goBack);
        progressBar=findViewById(R.id.progresbar);

        //Starting App database
        db = AppDatabase.getInstance(Transfer.this);
        jwebTokenDao = db.jwebTokenDao();
        jwebToken = jwebTokenDao.getAll().get(0);

        //Getting transfer from account, from previous activity
        String transferFrom = getIntent().getStringExtra("transferFrom");
        //Setting transfer from in a textview
        txvTrasnferFrom.setText("Transfer from "+transferFrom);

        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtAmountToTransfer.getText().toString().trim().isEmpty()||edtAccountToTransfer.getText().toString().trim().isEmpty()){
                    if(edtAccountToTransfer.getText().toString().isEmpty()){
                        edtAccountToTransfer.setError("This can not be empty");
                    }
                    if(edtAmountToTransfer.getText().toString().trim().isEmpty()){
                        edtAmountToTransfer.setError("This can not be empty");
                    }
                }else if(edtAccountToTransfer.getText().toString().equals(transferFrom)){
                    edtAccountToTransfer.setError("Can not send money within the same bank account");
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);
                    TransactionDto transactionDto = new TransactionDto();
                    transactionDto.setActualAccount(transferFrom);
                    transactionDto.setAmount(Double.parseDouble(edtAmountToTransfer.getText().toString()));
                    transactionDto.setOutAccount(edtAccountToTransfer.getText().toString());
                    doTransfer(transactionDto);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    public void doTransfer(TransactionDto transactionDto){
        Apis apis = new Apis();
        UserApi userApi = apis.getUser();
        Call<AccountTransactions> call = userApi.saveTransaction(jwebToken.getJsonWebToken(),transactionDto);

        call.enqueue(new Callback<AccountTransactions>() {
            @Override
            public void onResponse(Call<AccountTransactions> call, Response<AccountTransactions> response) {
                if(response.isSuccessful()){
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Transaction completed",Toast.LENGTH_LONG).show();
                        finish();

                } else if (response.code()==404) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Account to transfer not found",Toast.LENGTH_LONG).show();
                }
                else if(response.code()==401){
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"Access Unauthorized, do login again.", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<AccountTransactions> call, Throwable t) {
                Log.e("Error:",t.getMessage());
                Toast.makeText(getApplicationContext(),"Check internet connection or check the URL", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }
}