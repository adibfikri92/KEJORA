package com.example.kejora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Signup extends AppCompatActivity {

    boolean valid=true;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        EditText mSignupEmail = findViewById(R.id.Signup_Email);
        EditText mSignupPassword = findViewById(R.id.Signup_Password);
        EditText mSignupRePassword = findViewById(R.id.Signup_RePassword);

        ConstraintLayout mSignup = findViewById(R.id.constraintLayout_Signup);

        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkField(mSignupEmail);
                checkField(mSignupPassword);
                checkField(mSignupRePassword);

                if (valid){
                    String email = mSignupEmail.getText().toString().trim();
                    String password = mSignupPassword.getText().toString().trim();
                    String rePassword = mSignupRePassword.getText().toString().trim();

                    if(password.equals(rePassword)){
                        fStore.collection("User").document(email).get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            DocumentSnapshot document = task.getResult();
                                            if(document.exists()){
                                                fAuth.createUserWithEmailAndPassword(email, password)
                                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                                Toast.makeText(Signup.this, "Success", Toast.LENGTH_SHORT).show();
                                                                finish();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(Signup.this, "Unsuccessful : "+e.toString(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                            }else{
                                                Toast.makeText(Signup.this, "User not found", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                });
                    }else{
                        Toast.makeText(Signup.this, "Password not same", Toast.LENGTH_SHORT).show();
                        mSignupPassword.getText().clear();
                        mSignupRePassword.getText().clear();
                    }
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
}