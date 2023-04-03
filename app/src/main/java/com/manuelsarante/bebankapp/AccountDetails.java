package com.manuelsarante.bebankapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.manuelsarante.bebankapp.fragments.Details;
import com.manuelsarante.bebankapp.fragments.Transactions;
import com.manuelsarante.bebankapp.models.BankingAccount;
import com.manuelsarante.bebankapp.models.User;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class AccountDetails extends AppCompatActivity {

    BankingAccount account;
    User user;
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
        //Get the account sent in the intent
        account = (BankingAccount) getIntent().getSerializableExtra("account");
        user = (User) getIntent().getSerializableExtra("user");

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

                        case R.id.Home:
                            selectedFragment = new Details();
                            break;
                        case R.id.Clients:
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