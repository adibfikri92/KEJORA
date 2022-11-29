package com.example.kejora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register_Management extends AppCompatActivity {

    boolean valid=true;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_management);

        EditText mNameManagement = findViewById(R.id.Register_Management_Name);
        EditText mEmailManagement = findViewById(R.id.Register_Management_Email);
        EditText mPhoneNoManagement = findViewById(R.id.Register_Management_PhoneNo);
        EditText mPasswordManagement = findViewById(R.id.Register_Management_Password);
        EditText mRePasswordManagement = findViewById(R.id.Register_Management_RePassword);

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        ConstraintLayout mRegister = findViewById(R.id.constraintLayout_Register);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkField(mNameManagement);
                checkField(mEmailManagement);
                checkField(mPhoneNoManagement);
                checkField(mPasswordManagement);
                checkField(mRePasswordManagement);

                if(valid){
                    String nama = mNameManagement.getText().toString().trim();
                    String email = mEmailManagement.getText().toString().trim();
                    String phoneNo = mPhoneNoManagement.getText().toString().trim();
                    String password = mPasswordManagement.getText().toString().trim();
                    String rePassword = mRePasswordManagement.getText().toString().trim();

                    if(rePassword.equals(password)){

                        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    userID = fAuth.getCurrentUser().getUid();
                                    DocumentReference documentReference = fStore.collection("User").document(userID);
                                    Map<String, Object> User = new HashMap<>();
                                    User.put("Name", nama);
                                    User.put("Email", email);
                                    User.put("PhoneNo", phoneNo);
                                    User.put("UserLevel","Management");
                                    documentReference.set(User).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(Register_Management.this, "Registration Success", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(Register_Management.this, "Unsuccessful : "+e.toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                                else{
                                    Toast.makeText(Register_Management.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Register_Management.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }else{
                        Toast.makeText(Register_Management.this, "Password not same", Toast.LENGTH_SHORT).show();
                        mPasswordManagement.getText().clear();
                        mRePasswordManagement.getText().clear();
                    }
                }
                //startActivity(new Intent(getApplicationContext(), Intro.class));
            }
        });
    }

    public boolean checkField(EditText textfield){
        if(textfield.getText().toString().isEmpty()){
            textfield.setError("Error");
            valid=false;
        }else{
            valid=true;
        }
        return valid;
    }
}