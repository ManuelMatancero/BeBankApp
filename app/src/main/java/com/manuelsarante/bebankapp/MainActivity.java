package com.manuelsarante.bebankapp;



import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.manuelsarante.bebankapp.api.UserApi;
import com.manuelsarante.bebankapp.models.BankingAccount;
import com.manuelsarante.bebankapp.models.User;
import com.manuelsarante.bebankapp.room.dao.JwebTokenDao;
import com.manuelsarante.bebankapp.room.database.AppDatabase;
import com.manuelsarante.bebankapp.room.models.JwebToken;
import com.manuelsarante.bebankapp.utils.Apis;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity{
    public static User user;
    public static Long idUser;
    BankingAccountsAdapter accountsAdapter;
    ListView list;
    TextView welcome, empty;
    String userName="hola";
    ImageButton logout, userDetails;
    List<BankingAccount> accounts= new ArrayList<>();
    BankingAccount account;
    //With this static instance I will be able to close this activity from the next
    public static Activity ma;
    //Variables to accses the local database
    AppDatabase db;
    JwebTokenDao jwebTokenDao;
    JwebToken jwebToken = new JwebToken();
    public static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ma = this;

        logout = findViewById(R.id.logOut);
        userDetails = findViewById(R.id.userDetail);
        welcome = findViewById(R.id.welcome);
        list= findViewById(R.id.listview);
        TextView emptyText1 = (TextView)findViewById(R.id.empty1);
        list.setEmptyView(emptyText1);
        //Here i get the user info from last activity
        user = (User) getIntent().getSerializableExtra("user");
        idUser = user.getIdUser();



        //Initializing local database
        db = AppDatabase.getInstance(MainActivity.this);
        jwebTokenDao = db.jwebTokenDao();
        //getting the jwtoken object from database
        jwebToken = jwebTokenDao.getAll().get(0);
        token = jwebToken.getJsonWebToken();

        //Here within this method I also show the list of accounts
        //TODO: here i left Yesterday/////////////////////////////////////////////////
        refreshUserData();

        //Show welcome message on top of the activity
        welcome.setText("Welcome, " + user.getName());

        //Gets the accounts from user
        accounts = user.getBankingAccounts();

        //Do something when i click an element from the list
        //TODO: I have to check this because is not sending the correct ammount when i click one element
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                account = accounts.get(position);
                Intent i = new Intent(MainActivity.this, AccountDetails.class);
                i.putExtra("user", user);
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
                Intent i = new Intent(MainActivity.this, UserDetails.class);
                i.putExtra("user", user);
                startActivity(i);
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



    public void refreshUserData(){
        Apis apis = new Apis();
        UserApi userApi = apis.getUser();
        Call<User> userCall = userApi.getUser(token, idUser);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    user = response.body();
                    showAccounts(user);
                } else if (response.code()==401) {
                    Toast.makeText(ma.getApplicationContext(), "Your sesion has expired, do login again",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ma.getApplicationContext(), "Check your internet conection or IP server",Toast.LENGTH_LONG).show();
            }
        });

    }
    public void showAccounts(User user1){
        List<BankingAccount> accountList = user1.getBankingAccounts();

        ArrayList<BankingAccount> myAccounts = new ArrayList<>();

        myAccounts.addAll(accountList);

        ArrayList<BankingAccount> arrayList = new ArrayList<>();

        accountsAdapter = new BankingAccountsAdapter(MainActivity.this, arrayList);

        list.setAdapter(accountsAdapter);

        accountsAdapter.addAll(myAccounts);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(ma.getApplicationContext(), "on start", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(ma.getApplicationContext(), "on resume", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(ma.getApplicationContext(), "on pause", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(ma.getApplicationContext(), "on stop", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refreshUserData();
        Toast.makeText(ma.getApplicationContext(), "on restart", Toast.LENGTH_LONG).show();
    }

}