package com.example.doctorsapp;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {


    public DetailsFragment() {
        // Required empty public constructor
    }

    String phoneNumber,gender;
    EditText nameEditText,ageEditText;
    RadioButton male,female;
    Button sendBtn;
    DatabaseReference database;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Details Fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        database = FirebaseDatabase.getInstance().getReference();
        nameEditText = view.findViewById(R.id.nameEditText);
        ageEditText = view.findViewById(R.id.ageEditText);
        male = view.findViewById(R.id.maleRadio);
        female = view.findViewById(R.id.femaleRadio);
        sendBtn = view.findViewById(R.id.sendBtn);

        phoneNumber = getArguments().getString("phone");
        male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    gender = "Male";
                    female.setChecked(false);
                }
            }
        });

        female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    gender = "Female";
                    male.setChecked(false);
                }
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nameEditText.getText().length()>0 && ageEditText.length() > 0){
                    setDatatoFirebase(phoneNumber,nameEditText.getText().toString(),ageEditText.getText().toString(),gender);
                }
            }
        });
        return view;
    }

    public void setDatatoFirebase(String phone, String name, String age, String gender){
        database.child("Patient").child(phone).child("Name").setValue(name);
        database.child("Patient").child(phone).child("Age").setValue(age);
        database.child("Patient").child(phone).child("Gender").setValue(gender);
        database.child("Patient").child(phone).child("prescription").child("advice").setValue("No Advice");
        database.child("Patient").child(phone).child("prescription").child("diagnosis").setValue("No Diagnosis");
        database.child("Patient").child(phone).child("prescription").child("symptoms").setValue("No Symptoms");

        DoctorsFragment doctorsFragment = new DoctorsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("phone",phone);
        doctorsFragment.setArguments(bundle);

        getFragmentManager().beginTransaction().replace(R.id.frame, doctorsFragment).commit();
    }

}
