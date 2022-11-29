package com.example.kejora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register_HeadofHouse extends AppCompatActivity {

    boolean valid=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_headof_house);

        Intent data = getIntent();
        String _phoneNo = data.getStringExtra("PhoneNo");
        String _IDnumber = data.getStringExtra("IDnumber");
        String _name = data.getStringExtra("Name");

        EditText mNameHeadofHouse = findViewById(R.id.Register_HeadofHouse_Name);
        EditText mEmailHeadofHouse = findViewById(R.id.Register_HeadofHouse_Email);
        EditText mPhoneNoHeadofHouse = findViewById(R.id.Register_HeadofHouse_PhoneNo);
        EditText mIDnumberHeadofHouse = findViewById(R.id.Register_HeadofHouse_IDnumber);
        Spinner mRumah = findViewById(R.id.RumahID);

        mNameHeadofHouse.setText(_name);
        mPhoneNoHeadofHouse.setText(_phoneNo);
        mIDnumberHeadofHouse.setText(_IDnumber);

        ArrayAdapter<String> myAdapter3 = new ArrayAdapter<String>(Register_HeadofHouse.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Rumah));
        myAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRumah.setAdapter(myAdapter3);

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        ConstraintLayout mRegister = findViewById(R.id.constraintLayout_Register);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkField(mNameHeadofHouse);
                checkField(mEmailHeadofHouse);
                checkField(mPhoneNoHeadofHouse);
                checkField(mIDnumberHeadofHouse);
                String house = String.valueOf(mRumah.getSelectedItem());

                if(valid){
                    String Name = mNameHeadofHouse.getText().toString().trim();
                    String Email = mEmailHeadofHouse.getText().toString().trim();
                    String PhoneNo = mPhoneNoHeadofHouse.getText().toString().trim();
                    String IDnumber = mIDnumberHeadofHouse.getText().toString().trim();

                    String idDocHeadofHouse = house;
                    String idDocUser = Email;

                    Map<String, Object> HeadofHouse = new HashMap<>();
                    HeadofHouse.put("NameHeadofHouse", Name);
                    HeadofHouse.put("IDnumberHeadofHouse", IDnumber);
                    HeadofHouse.put("House",house);
                    fStore.collection("HeadofHouse").document(idDocHeadofHouse).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        DocumentSnapshot document = task.getResult();
                                        if(document.exists()){
                                            Toast.makeText(Register_HeadofHouse.this, "Head of House already in database", Toast.LENGTH_SHORT).show();
                                        }else{
                                            fStore.collection("HeadofHouse")
                                                    .document(idDocHeadofHouse)
                                                    .set(HeadofHouse)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Map<String, Object> User = new HashMap<>();
                                                            User.put("Email", Email);
                                                            User.put("UserLevel","HeadofHouse");
                                                            fStore.collection("User").document(idDocUser).get()
                                                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                    if(task.isSuccessful()){
                                                                                        DocumentSnapshot document = task.getResult();
                                                                                        if(document.exists()){
                                                                                            Toast.makeText(Register_HeadofHouse.this, "User already in database", Toast.LENGTH_SHORT).show();
                                                                                        }else{
                                                                                            fStore.collection("User")
                                                                                                    .document(idDocUser)
                                                                                                    .set(User)
                                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                            Toast.makeText(Register_HeadofHouse.this, "Success", Toast.LENGTH_SHORT).show();
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
                                    }else{
                                        Toast.makeText(Register_HeadofHouse.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

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