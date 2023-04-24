package com.manuelsarante.bebankapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.manuelsarante.bebankapp.R;
import com.manuelsarante.bebankapp.models.BankingAccount;
import com.manuelsarante.bebankapp.models.User;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Details#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Details extends Fragment {

    TextView cvv, cardNumber, name, validDate,money, accountNumber,lastDigit;
    View view;


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
            long number = Long.parseLong(ba.getCards().getCardNumber());
            String formatted = String.format("%s %s %s %s",
                    Long.toString(number).substring(0, 4),
                    Long.toString(number).substring(4, 8),
                    Long.toString(number).substring(8, 12),
                    Long.toString(number).substring(12, 16));
            cardNumber.setText(formatted);
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

        return view;
    }
}