package com.example.doctorsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowData extends AppCompatActivity {

    String docName;
    TextView medicine,advice,prescription,diagnosis,symptoms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        medicine = findViewById(R.id.medicine);
        advice = findViewById(R.id.advice);
        prescription = findViewById(R.id.Prescription);
        diagnosis = findViewById(R.id.Diagnosis);
        symptoms = findViewById(R.id.Symptoms);

        docName = getIntent().getStringExtra("doctor");

        getData();
    }

    public void getData(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot doc = dataSnapshot.child(user.getPhoneNumber()).child("Doctors").child(docName);
                medicine.setText(doc.child("Medicine").getValue().toString());
                advice.setText(doc.child("Advice").getValue().toString());
                prescription.setText(doc.child("Prescription").getValue().toString());
                diagnosis.setText(doc.child("Diagnosis").getValue().toString());
                symptoms.setText(doc.child("Symptoms").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
