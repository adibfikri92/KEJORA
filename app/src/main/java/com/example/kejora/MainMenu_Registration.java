package com.example.kejora;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainMenu_Registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_registration);

        ConstraintLayout mRegisterManagement = findViewById(R.id.constraintLayout_Management);

        mRegisterManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register_Management.class));
                finish();
            }
        });

        ConstraintLayout mRegisterStaff = findViewById(R.id.constraintLayout_Staff);

        mRegisterStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register_Staff.class));
                finish();
            }
        });

        ConstraintLayout mRegisterStudent = findViewById(R.id.constraintLayout_Student);

        mRegisterStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register_Student.class));
                finish();
            }
        });

        ConstraintLayout mRegisterHeadofHouse = findViewById(R.id.constraintLayout_HeadofHouse);

        mRegisterHeadofHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Find_HeadofHouse.class));
                finish();
            }
        });

        ConstraintLayout mRegisterReferee = findViewById(R.id.constraintLayout_Referee);

        mRegisterReferee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Find_Referee.class));
                finish();
            }
        });
    }
}