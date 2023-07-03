package com.manuelsarante.bebankapp.fragments;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.manuelsarante.bebankapp.Login;
import com.manuelsarante.bebankapp.LoginPin;
import com.manuelsarante.bebankapp.MainActivity;
import com.manuelsarante.bebankapp.R;
import com.manuelsarante.bebankapp.UserDetails;
import com.manuelsarante.bebankapp.api.UserApi;
import com.manuelsarante.bebankapp.dto.LoginWithPinDto;
import com.manuelsarante.bebankapp.models.BankingAccount;
import com.manuelsarante.bebankapp.models.User;
import com.manuelsarante.bebankapp.room.dao.JwebTokenDao;
import com.manuelsarante.bebankapp.room.dao.UserCredentialsDao;
import com.manuelsarante.bebankapp.room.database.AppDatabase;
import com.manuelsarante.bebankapp.room.models.JwebToken;
import com.manuelsarante.bebankapp.room.models.UserCredentials;
import com.manuelsarante.bebankapp.utils.Apis;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Details#newInstance} factory method to
 * create an instance of this fragment.
 */
/*
 * Copyright (c) Manuel Antonio Sarante Sanchez 2023
 * All rights reserved.
 */
public class Details extends Fragment {



    TextView cvv, cardNumber, name, validDate,money, accountNumber,lastDigit,showMore;
    View view;
    ImageButton btnCopy;
    EditText digit1, digit2, digit3, digit4;
    ProgressBar progressBar;
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    LinearLayout backPartCard;
    StringBuilder pin = new StringBuilder();
    //Database variables
    AppDatabase db;
    UserCredentialsDao userCredentialsDao;
    UserCredentials userCredentials = new UserCredentials();
    JwebTokenDao jwebTokenDao;
    JwebToken jwebToken = new JwebToken();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    User user;
    public static Bundle data;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Details() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Details.
     */
    // TODO: Rename and change types and number of parameters
    public static Details newInstance(String param1, String param2) {
        Details fragment = new Details();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            data = getArguments();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_details, container, false);
        // Inflate the layout for this fragment
        money = view.findViewById(R.id.money);
        accountNumber = view.findViewById(R.id.accountNumber);
        cvv = view.findViewById(R.id.cvv);
        cardNumber = view.findViewById(R.id.cardNumber);
        name = view.findViewById(R.id.name);
        validDate = view.findViewById(R.id.validDate);
        lastDigit = view.findViewById(R.id.lastDigit);
        showMore = view.findViewById(R.id.showMore);
        backPartCard = view.findViewById(R.id.linearLayout5);
        btnCopy = view.findViewById(R.id.copy);


        //Database instance
        db = AppDatabase.getInstance(getContext());
        userCredentialsDao = db.userCredentialsDao();
        jwebTokenDao = db.jwebTokenDao();

        if (data != null) {
            User myString = (User) data.getSerializable("myData");
            BankingAccount ba = (BankingAccount) data.getSerializable("myAc");
            //Here I get the card Number of the card of the account
            String cardNum = String.valueOf(ba.getCards().getCardNumber());
            //Create a String builder to append the last 4 digits of the card
            StringBuilder last4Number = new StringBuilder();
            List<String> numbersListed = new ArrayList<>();
            //here i add the numbers of the card in a list
            for(int i = 0; i<16 ; i++){
                numbersListed.add(String.valueOf(cardNum.charAt(i)));
            }
            //get the last 4 numbers
            for(int i =12; i<16; i++){
                last4Number.append(numbersListed.get(i));
            }
            //set the last 4 numbers to the textview
            lastDigit.setText(last4Number.toString());
            name.setText(myString.getName());
            cvv.setText(String.valueOf(ba.getCards().getCvv()));
            //Setting the pattern #### #### to the card number
            String number = ba.getCards().getCardNumber();
            StringBuilder numberFormated = new StringBuilder();
            for(int i = 0 ; i < number.length(); i++){
                int module = (i+1) % 4;
                if(module==0){
                    numberFormated.append(number.charAt(i)).append(" ");
                }else{
                    numberFormated.append(number.charAt(i));
                }
            }
            cardNumber.setText(String.valueOf(numberFormated));
            //Parsing Card Date to LocalDate
            String date = ba.getCards().getExpireDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate lDate = LocalDate.parse(date, formatter);
            //Setting the month and the year
            validDate.setText(lDate.getMonth().ordinal()+"/"+lDate.getYear());
            accountNumber.setText(String.valueOf(ba.getAccountNumber()));
            //Format currency
            NumberFormat nFormat = DecimalFormat.getCurrencyInstance(Locale.getDefault());
            money.setText(String.valueOf(nFormat.format(ba.getMountAccount())));

        } else{
            Toast.makeText(getContext(), "Null Bundle", Toast.LENGTH_SHORT).show();
        }
        showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if backPart of the card is Shown it will go invisible
                if(backPartCard.isShown()){
                    backPartCard.setVisibility(View.INVISIBLE);
                    showMore.setText(getContext().getString(R.string.showMore));
                }else{
                    createDialog();
                }

            }
        });

        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                // Gets a handle to the clipboard service.
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                // Creates a new text clip to put on the clipboard.
                ClipData clip = ClipData.newPlainText("text", accountNumber.getText().toString());
                // Set the clipboard's primary clip.
                clipboard.setPrimaryClip(clip);

                Toast.makeText(view.getContext(), "Account number copied", Toast.LENGTH_LONG).show();


            }
        });

        return view;
    }
    //Dialog to add the pin and see the card info
    public void createDialog(){
        dialogBuilder = new AlertDialog.Builder(getContext());
        final View editPopUp = getLayoutInflater().inflate(R.layout.popup_pin, null);
        digit1 = (EditText) editPopUp.findViewById(R.id.digit1);
        digit2 = (EditText) editPopUp.findViewById(R.id.digit2);
        digit3 = (EditText) editPopUp.findViewById(R.id.digit3);
        digit4 = (EditText) editPopUp.findViewById(R.id.digit4);
        progressBar =(ProgressBar) editPopUp.findViewById(R.id.progresbar);


        dialogBuilder.setView(editPopUp);
        dialog = dialogBuilder.create();
        dialog.show();

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
                pin.append(digit4.getText().toString());
                //if digit 4 is different of empty it will try to get the user
                if(!digit4.getText().toString().equals("")){
                    //here when the text change in digit 4 will be conecting to the local database and then with the API
                    userCredentials = userCredentialsDao.getAll().get(0);
                    progressBar.setVisibility(View.VISIBLE);
                    LoginWithPinDto log = new LoginWithPinDto();
                    log.setUser(userCredentials.getUser());
                    log.setPassword(userCredentials.getPassword());
                    log.setPin(pin.toString());
                    login(log);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void login(LoginWithPinDto loginDto){
        Apis api = new Apis();
        UserApi userApi = api.getUser();
        //here i get the object with the jsonWebToken to see if the date expired
        jwebToken = jwebTokenDao.getAll().get(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss z yyyy");
        LocalDateTime expireDate = LocalDateTime.parse(jwebToken.getExpirationDate(), formatter);
        LocalDateTime currentTime = LocalDateTime.now();
        //Here i check if the jsonwebtoken has expired in order to get a new one in case that this be true
        int comparisonResult = currentTime.compareTo(expireDate);
        if(comparisonResult>=0){
            createPopUp();
        }else{

            Call<User> call = userApi.loginPin(jwebToken.getJsonWebToken(), loginDto);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.isSuccessful()) {
                        backPartCard.setVisibility(View.VISIBLE);
                        pin.setLength(0);
                        dialog.dismiss();
                        showMore.setText(getContext().getString(R.string.showLess));
                    }else if(response.code()==404){
                        Toast.makeText(getContext(),"Incorrect PIN", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getContext(),"Something wrong happened", Toast.LENGTH_LONG).show();
                }

            });
        }

    }
    public void unlinkUser(){
        //Here i erase the user credentiasl saved locally
        //And the jwebToken
        userCredentials = userCredentialsDao.getAll().get(0);
        userCredentialsDao.deleteUserCredentials(userCredentials);
        jwebToken = jwebTokenDao.getAll().get(0);
        jwebTokenDao.deleteJwt(jwebToken);
        Intent i= new Intent(getContext(), Login.class);
        startActivity(i);
        getActivity().finish();
        //here i close MainActivity with this static variable
        MainActivity.ma.finish();
    }
    //This popup message let know the user that that the Jwebtoken has expired and needs to login again
    public void createPopUp(){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Details.this.getContext());
        builder.setTitle("Information");
        builder.setMessage(getResources().getString(R.string.jwebExpired));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                unlinkUser();
            }
        });
        builder.show();
    }
}