package com.example.kejora;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Register_Referee extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_referee);

        EditText mNameReferee = findViewById(R.id.Register_Referee_Name);
        EditText mEmailReferee = findViewById(R.id.Register_Referee_Email);
        EditText mPhoneNoReferee = findViewById(R.id.Register_Referee_PhoneNo);
        EditText mPasswordReferee = findViewById(R.id.Register_Referee_Password);
        EditText mRePasswordReferee = findViewById(R.id.Register_Referee_RePassword);

        ConstraintLayout mRegister = findViewById(R.id.constraintLayout_Register);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}