package com.example.doctorsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.view.View.GONE;

public class FirstActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    ArrayList<String> doctors;
    ListView listView;
    String PHONE_NUMBER;
    ArrayAdapter<String> arrayAdapter;
    FloatingActionButton fab;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        listView = findViewById(R.id.listView);
        progressBar = findViewById(R.id.progressBar);
        fab = findViewById(R.id.fab);
        doctors = new ArrayList<>();
        PHONE_NUMBER = firebaseUser.getPhoneNumber();
        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_expandable_list_item_1,doctors);
        listView.setAdapter(arrayAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("doc",true);
                intent.putExtra("phone",PHONE_NUMBER);
                startActivity(intent);
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ShowData.class);
                intent.putExtra("doctor", arrayAdapter.getItem(i));
                startActivity(intent);
            }
        });

        getData();
    }

    void getData(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                doctors.clear();

                DataSnapshot dataSnapshot1 = dataSnapshot.child("Patient").child(PHONE_NUMBER).child("Doctors");

                for (DataSnapshot snapshot : dataSnapshot1.getChildren()){
                    DataSnapshot doc = dataSnapshot.child("Doctors");
                    for (DataSnapshot docId1 : doc.getChildren()){
                        for (DataSnapshot docId2 : docId1.getChildren()){
                            if(snapshot.getKey().matches(docId2.getKey())){
                                doctors.add(String.valueOf(docId2.child("name").getValue()));
                            }
                        }
                        progressBar.setVisibility(GONE);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(GONE);
                Toast.makeText(FirstActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_first,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.sign_out){
            AuthUI.getInstance()
                    .signOut(FirstActivity.this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(FirstActivity.this, "User signed out", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
