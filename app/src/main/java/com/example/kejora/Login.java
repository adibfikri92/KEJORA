package com.example.kejora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
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