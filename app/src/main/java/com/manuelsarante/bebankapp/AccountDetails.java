package com.manuelsarante.bebankapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.manuelsarante.bebankapp.fragments.Details;
import com.manuelsarante.bebankapp.fragments.Transactions;
import com.manuelsarante.bebankapp.models.BankingAccount;
import com.manuelsarante.bebankapp.models.User;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class AccountDetails extends AppCompatActivity {

    public static  BankingAccount account;
    public static User user;
    TextView ammount;
    ImageButton goBack, logout;
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        bottomNav= findViewById(R.id.bottom_navigation);
        ammount = findViewById(R.id.ammount);
        goBack = findViewById(R.id.goBack);
        logout = findViewById(R.id.logOut);

        //Get the account and user sent in the intent
        account = (BankingAccount) getIntent().getSerializableExtra("account");
        user = (User) getIntent().getSerializableExtra("user");

        //Sending user and account from this Activity to Details Fragment
        Details detail = new Details();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle data = new Bundle();
        data.putSerializable("myData", user);
        data.putSerializable("myAc", account);
        detail.setArguments(data);
        fragmentTransaction.replace(R.id.fragment_container, detail).commit();
        /////////////////////////////////////////////////////////////////////////////
        //Sending user and account from this Activity to Details Fragment
        Transactions transactions = new Transactions();
        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
        Bundle data2 = new Bundle();
        data2.putSerializable("myData", user);
        data2.putSerializable("myAc", account);
        transactions.setArguments(data2);
        fragmentTransaction2.replace(R.id.fragment_container, transactions).commit();
        /////////////////////////////////////////////////////////////////////////////

        //Number format to give currency format to the amount
        NumberFormat nFormat = DecimalFormat.getCurrencyInstance(Locale.getDefault());
        ammount.setText(String.valueOf(nFormat.format(account.getMountAccount())));

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //First close MainActivity and Then close this activity
               MainActivity.ma.finish();
               finish();
            }
        });
        //BottomNavigation default action
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new Details()).commit();

    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {

                        case R.id.detail:
                            selectedFragment = new Details();
                            break;
                        case R.id.trans:
                            selectedFragment = new Transactions();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };

    @Override
    public void onBackPressed() {
            super.onBackPressed();
    }
}