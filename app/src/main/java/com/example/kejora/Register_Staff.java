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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register_Staff extends AppCompatActivity {

    boolean valid=true;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_staff);

        EditText mNameStaff = findViewById(R.id.Register_Staff_Name);
        EditText mPhoneNoStaff = findViewById(R.id.Register_Staff_PhoneNo);
        EditText mIDnumberStaff = findViewById(R.id.Register_Staff_IDnumber);

        fStore = FirebaseFirestore.getInstance();

        ConstraintLayout mRegister = findViewById(R.id.constraintLayout_Register);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkField(mNameStaff);
                checkField(mPhoneNoStaff);
                checkField(mIDnumberStaff);

                if(valid){
                    String Name = mNameStaff.getText().toString().trim();
                    String PhoneNo = mPhoneNoStaff.getText().toString().trim();
                    String IDnumber = mIDnumberStaff.getText().toString().trim();
                    String idDoc = IDnumber;

                    Map<String, Object> Staff = new HashMap<>();
                    Staff.put("Name", Name);
                    Staff.put("PhoneNo", PhoneNo);
                    Staff.put("IDnumber", IDnumber);

                    fStore.collection("Staff").document(idDoc).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        DocumentSnapshot document = task.getResult();
                                        if(document.exists()){
                                            Toast.makeText(Register_Staff.this, "Student already in database", Toast.LENGTH_SHORT).show();
                                        }else{
                                            fStore.collection("Staff")
                                                    .document(IDnumber)
                                                    .set(Staff)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(Register_Staff.this, "Success", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(Register_Staff.this, "Failed : "+e.toString(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Register_Staff.this,"Failed : "+e.toString(), Toast.LENGTH_SHORT).show();
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