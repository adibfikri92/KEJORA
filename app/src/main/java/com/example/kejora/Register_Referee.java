package com.example.kejora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register_Referee extends AppCompatActivity {

    boolean valid=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_referee);

        Intent data = getIntent();
        String _phoneNo = data.getStringExtra("PhoneNo");
        String _IDnumber = data.getStringExtra("IDnumber");
        String _name = data.getStringExtra("Name");

        EditText mNameReferee = findViewById(R.id.Register_Referee_Name);
        EditText mEmailReferee = findViewById(R.id.Register_Referee_Email);
        EditText mPhoneNoReferee = findViewById(R.id.Register_Referee_PhoneNo);
        EditText mIDnumberReferee = findViewById(R.id.Register_Referee_IDnumber);

        mNameReferee.setText(_name);
        mPhoneNoReferee.setText(_phoneNo);
        mIDnumberReferee.setText(_IDnumber);

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        ConstraintLayout mRegister = findViewById(R.id.constraintLayout_Register);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkField(mNameReferee);
                checkField(mEmailReferee);
                checkField(mPhoneNoReferee);
                checkField(mIDnumberReferee);
                if(valid){
                    String Name = mNameReferee.getText().toString().trim();
                    String Email = mEmailReferee.getText().toString().trim();
                    String PhoneNo = mPhoneNoReferee.getText().toString().trim();
                    String IDnumber = mIDnumberReferee.getText().toString().trim();

                    String idDocReferee = IDnumber;
                    String idDocUser = Email;

                    Map<String, Object> Referee = new HashMap<>();
                    Referee.put("NameReferee", Name);
                    Referee.put("IDnumberReferee", IDnumber);
                    Referee.put("PhoneNo",PhoneNo);
                    fStore.collection("Referee").document(idDocReferee).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        DocumentSnapshot document = task.getResult();
                                        if(document.exists()){
                                            Toast.makeText(Register_Referee.this, "Head of House already in database", Toast.LENGTH_SHORT).show();
                                        }else{
                                            fStore.collection("Referee")
                                                    .document(idDocReferee)
                                                    .set(Referee)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Map<String, Object> User = new HashMap<>();
                                                            User.put("Email", Email);
                                                            User.put("UserLevel","Referee");
                                                            fStore.collection("User").document(idDocUser).get()
                                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                            if(task.isSuccessful()){
                                                                                DocumentSnapshot document = task.getResult();
                                                                                if(document.exists()){
                                                                                    Toast.makeText(Register_Referee.this, "User already in database", Toast.LENGTH_SHORT).show();
                                                                                }else{
                                                                                    fStore.collection("User")
                                                                                            .document(idDocUser)
                                                                                            .set(User)
                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                    Toast.makeText(Register_Referee.this, "Success", Toast.LENGTH_SHORT).show();
                                                                                                    finish();
                                                                                                }
                                                                                            });
                                                                                }
                                                                            }
                                                                        }
                                                                    });
                                                        }
                                                    });
                                        }
                                    }
                                }
                            });
                }else{
                    Toast.makeText(Register_Referee.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
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