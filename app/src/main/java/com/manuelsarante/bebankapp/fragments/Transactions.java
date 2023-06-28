package com.manuelsarante.bebankapp.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.manuelsarante.bebankapp.MainActivity;
import com.manuelsarante.bebankapp.R;
import com.manuelsarante.bebankapp.Transfer;
import com.manuelsarante.bebankapp.models.AccountTransactions;
import com.manuelsarante.bebankapp.models.BankingAccount;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Transactions#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Transactions extends Fragment {

    //Variables
    TransactionsAdapter transactionsAdapter;
    ImageButton btnCopy;
    NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
    ListView list;
    TextView accountNumber, transfer;
    List<Transactions> transactionsList = new ArrayList<>();
    View view;
    //Global variable inside onCreate
    BankingAccount bankingAccount = new BankingAccount();

    public static Bundle data;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Transactions() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Transactions.
     */
    // TODO: Rename and change types and number of parameters
    public static Transactions newInstance(String param1, String param2) {
        Transactions fragment = new Transactions();
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
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_transactions, container, false);
        list = view.findViewById(R.id.listViewTrans);
        accountNumber = view.findViewById(R.id.accountNumber);
        btnCopy = view.findViewById(R.id.copy);
        transfer= view.findViewById(R.id.makeTrans);



        if(data != null){
            bankingAccount = (BankingAccount) data.getSerializable("myAc");
            accountNumber.setText(String.valueOf(bankingAccount.getAccountNumber()));
            List<AccountTransactions> transactionsList1 = bankingAccount.getTransactions();
            showTransactions(transactionsList1);

        }else{
            Toast.makeText(getContext(), "Null Bundle", Toast.LENGTH_SHORT).show();
        }
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

        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), Transfer.class);
                i.putExtra("transferFrom", String.valueOf(bankingAccount.getAccountNumber()));
                startActivity(i);
            }
        });

        return view;
    }


    public class TransactionsAdapter extends ArrayAdapter<AccountTransactions> {

        public TransactionsAdapter(Context context, ArrayList<AccountTransactions> transactions) {
            super(context,0, transactions);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AccountTransactions transactions1 = getItem(position);

            if(convertView==null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.transactions_ticket,parent,false);
            }
            TextView description = convertView.findViewById(R.id.description);
            TextView amount = convertView.findViewById(R.id.amount);

            description.setText(transactions1.getDescription().toString());
            if(transactions1.getTransactionType().equals("TRANSFERRED")){
                amount.setTextColor(Color.RED);
                amount.setText(String.valueOf("- " + numberFormat.format(transactions1.getAmount())));
            } else if (transactions1.getTransactionType().equals("RECEIVED")) {
                amount.setTextColor(Color.GREEN);
                amount.setText(String.valueOf(numberFormat.format(transactions1.getAmount())));
            }

            return convertView;
        }
    }

    public void showTransactions(List<AccountTransactions> transactionsList){
        ArrayList<AccountTransactions> myTransactions = new ArrayList<>();

        myTransactions.addAll(transactionsList);

        ArrayList<AccountTransactions> arrayList = new ArrayList<>();

        transactionsAdapter = new TransactionsAdapter(getContext(), arrayList);

        list.setAdapter(transactionsAdapter);

        transactionsAdapter.addAll(myTransactions);
    }
}