package com.manuelsarante.bebankapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.manuelsarante.bebankapp.models.User;

public class UserDetails extends AppCompatActivity {

    TextView fName, email;
    ImageButton goBack;

    public static UserDetails userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        userDetails =this;
        fName = findViewById(R.id.fName);
        email = findViewById(R.id.email);
        goBack = findViewById(R.id.goBack);
        User user = (User) getIntent().getSerializableExtra("user");

        fName.setText(user.getName());
        email.setText(user.getEmail());

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}