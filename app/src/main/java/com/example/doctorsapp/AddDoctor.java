package com.example.doctorsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddDoctor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);

        Button addDoc = findViewById(R.id.addDoc);
        final EditText editText = findViewById(R.id.editText);
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        addDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().length()>2){
                    DatabaseReference docName =  reference
                            .child(user.getPhoneNumber())
                            .child("Doctors")
                            .child(editText.getText().toString());
                    docName.child("Medicine").setValue("No Medicine");
                    docName.child("Symptoms").setValue("No Symptoms");
                    docName.child("Prescription").setValue("No Prescription");
                    docName.child("Diagnosis").setValue("No Diagnosis");
                    docName.child("Advice").setValue("No Advice");
                }else
                    Toast.makeText(AddDoctor.this, "Enter Correct Name", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
