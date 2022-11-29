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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Find_HeadofHouse extends AppCompatActivity {

    boolean valid=true;

    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_headof_house);

        EditText mIDnumber = findViewById(R.id.Find_IDnumber);

        ConstraintLayout mRegister = findViewById(R.id.constraintLayout_Search);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkField(mIDnumber);

                if(valid){
                    String IDnumber = mIDnumber.getText().toString().trim();

                    fStore.collection("Student")
                            .whereEqualTo("IDnumber",IDnumber)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        for (QueryDocumentSnapshot document : task.getResult()){
                                            String _phoneNo = document.getString("PhoneNo");
                                            String _IDnumber = document.getString("IDnumber");
                                            String _Name = document.getString("Name");
                                            if(!_phoneNo.isEmpty() || !_IDnumber.isEmpty() || !_Name.isEmpty()){
                                                Intent i = new Intent(view.getContext(),Register_HeadofHouse.class);
                                                i.putExtra("PhoneNo",_phoneNo);
                                                i.putExtra("IDnumber",_IDnumber);
                                                i.putExtra("Name",_Name);
                                                startActivity(i);
                                                finish();
                                            }
                                        }
                                    }else{
                                        Toast.makeText(Find_HeadofHouse.this, "No data found", Toast.LENGTH_SHORT).show();
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