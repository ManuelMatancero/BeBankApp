package com.manuelsarante.bebankapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.manuelsarante.bebankapp.fragments.Details;
import com.manuelsarante.bebankapp.models.BankingAccount;
import com.manuelsarante.bebankapp.models.User;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity{
    User user;
    BankingAccountsAdapter accountsAdapter;
    ListView list;
    TextView welcome;
    String userName="hola";
    ImageButton logout, userDetails;
    List<BankingAccount> accounts= new ArrayList<>();
    //With this static instance I will be able to close this activity from the next
    public static Activity ma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ma = this;

        logout = findViewById(R.id.logOut);
        userDetails = findViewById(R.id.userDetail);
        welcome = findViewById(R.id.welcome);
        list= findViewById(R.id.listview);

        user = (User) getIntent().getSerializableExtra("user");
        userName = user.getName();

        welcome.setText("Welcome, " + user.getName());

        //Show accounts in list view
        showAccounts();

        //Gets the accounts from user
        accounts = user.getBankingAccounts();

        //Do something when i click an element from the list
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BankingAccount account = accounts.get(position);
                Intent i = new Intent(MainActivity.this, AccountDetails.class);
                i.putExtra("account", account);
                startActivity(i);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    public class BankingAccountsAdapter extends ArrayAdapter<BankingAccount>{

        public BankingAccountsAdapter(Context context, ArrayList<BankingAccount> accounts) {
            super(context,0, accounts);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           BankingAccount account = getItem(position);

           if(convertView==null){
               convertView = LayoutInflater.from(getContext()).inflate(R.layout.account_ticket,parent,false);
           }
            TextView numberBankingAccount = (TextView) convertView.findViewById(R.id.accountNumber);
            TextView money = (TextView) convertView.findViewById(R.id.money);

            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
            numberBankingAccount.setText(String.valueOf(account.getAccountNumber()));
            money.setText("US"+numberFormat.format(account.getMountAccount()));



            return convertView;
        }
    }

    public void showAccounts(){
        List<BankingAccount> accountList = user.getBankingAccounts();

        ArrayList<BankingAccount> myAccounts = new ArrayList<>();

        myAccounts.addAll(accountList);

        ArrayList<BankingAccount> arrayList = new ArrayList<>();

        accountsAdapter = new BankingAccountsAdapter(MainActivity.this, arrayList);

        list.setAdapter(accountsAdapter);

        accountsAdapter.addAll(myAccounts);
    }
}