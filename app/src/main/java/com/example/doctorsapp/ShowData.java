package com.example.doctorsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
    FirebaseUser firebaseUser;
    String phoneNo,messageLink;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        checkForSmsPermission();
        medicine = findViewById(R.id.medicine);
        advice = findViewById(R.id.advice);
        prescription = findViewById(R.id.Prescription);
        diagnosis = findViewById(R.id.Diagnosis);
        symptoms = findViewById(R.id.Symptoms);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null)
        phoneNo = firebaseUser.getPhoneNumber();
        messageLink ="This will be the link to report";

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

    //Adding this function to button or anywhere you want
    public void sendSmsToPatient(View view){
        checkForSmsPermission();
        if(phoneNo!=null){
            SmsAsyncTask smsAsyncTask = new SmsAsyncTask();
            smsAsyncTask.execute(phoneNo,messageLink);
        }
    }

    public void sendEmailToPatient(View view){
        String subject ="Patient Report";
        String patientEmail = "abhishekavi4602@gmail.com";//change email here
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{patientEmail});
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT, messageLink);
        email.setType("message/rfc822");

        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }

    private void checkForSmsPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);

        }
    }

    private class SmsAsyncTask extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String phone = strings[0];
            String message = strings[1];

            if((ActivityCompat.checkSelfPermission(ShowData.this,
                    Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)){
                Intent intent = new Intent(getApplicationContext(),ShowData.class);
                PendingIntent pi = PendingIntent.getActivity(getApplicationContext(),0,intent,0);
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phone,null,message,pi,null);
                return "SMS sent successfully to "+phone;
            }
            else
                return "Please enable SMS Permission in App Settings";

        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}
