package com.example.kejora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Login extends AppCompatActivity {

    boolean valid=true;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText mLoginEmail = findViewById(R.id.Login_Email);
        EditText mLoginPassword = findViewById(R.id.Login_Password);

        ConstraintLayout mSignup = findViewById(R.id.constraintLayout_Signup);

        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Signup.class));
            }
        });

        ConstraintLayout mForgetPassword = findViewById(R.id.constraintLayout_ForgetPassword);

        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Set up the input
                final EditText input = new EditText(Login.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                input.setHint("Email");
                input.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


                new AlertDialog.Builder(Login.this)
                        .setTitle("Password Reset")
                        .setMessage("Are you sure you want to reset your password?")
                        .setView(input)
                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String forgetEmail = input.getText().toString();
                                if(!forgetEmail.isEmpty()){
                                    fAuth.sendPasswordResetEmail(forgetEmail)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(Login.this, "Reset link has been sent", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(Login.this, "Unsuccessful : "+e.toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }else{
                                    Toast.makeText(Login.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        ConstraintLayout mLogin = findViewById(R.id.constraintLayout_Login);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkField(mLoginEmail);
                checkField(mLoginPassword);

                if (valid) {
                    String email = mLoginEmail.getText().toString().trim();
                    String password = mLoginPassword.getText().toString().trim();
                    fAuth.signInWithEmailAndPassword(email,password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(Login.this, "Success", Toast.LENGTH_SHORT).show();
                                                checkUserAccessLevel(email);
                                            }else{
                                                Toast.makeText(Login.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                }else{
                    Toast.makeText(Login.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                }

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

    private void checkUserAccessLevel(String email) {
        fStore.collection("User")
                .whereEqualTo("Email",email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                if(document.getString("UserLevel").equals("Management")){
                                    startActivity(new Intent(getApplicationContext(), MainMenu_Management.class));
                                    finish();
                                }
                            }
                        }
                    }
                });

    }
}