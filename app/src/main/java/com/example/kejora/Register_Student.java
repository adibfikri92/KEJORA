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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Register_Student extends AppCompatActivity {

    boolean valid=true;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_student);

        EditText mNameStudent = findViewById(R.id.Register_Student_Name);
        EditText mEmailStudent = findViewById(R.id.Register_Student_Email);
        EditText mIDnumberStudent = findViewById(R.id.Register_Student_IDnumber);

        fStore = FirebaseFirestore.getInstance();

        ConstraintLayout mRegister = findViewById(R.id.constraintLayout_Register);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkField(mNameStudent);
                checkField(mEmailStudent);
                checkField(mIDnumberStudent);

                if(valid){
                    String Name = mNameStudent.getText().toString().trim();
                    String Email = mEmailStudent.getText().toString().trim();
                    String IDnumber = mIDnumberStudent.getText().toString().trim();
                    String idDoc = IDnumber;

                    Map<String, Object> Student = new HashMap<>();
                    Student.put("Name", Name);
                    Student.put("Email", Email);
                    Student.put("IDnumber", IDnumber);
                    //DocumentReference docRef = fStore.collection("Sukan");
                    fStore.collection("Student").document(idDoc).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        DocumentSnapshot document = task.getResult();
                                        Toast.makeText(Register_Student.this, document.getId(), Toast.LENGTH_SHORT).show();
                                        if(document.exists()){
                                            Toast.makeText(Register_Student.this, "Student already in database", Toast.LENGTH_SHORT).show();
                                        }else{
                                            fStore.collection("Student")
                                                    .document(IDnumber)
                                                    .set(Student)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(Register_Student.this, "Success", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(Register_Student.this, "Failed : "+e.toString(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Register_Student.this,"Failed : "+e.toString(), Toast.LENGTH_SHORT).show();
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