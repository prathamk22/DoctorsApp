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

public class FirstActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    ArrayList<String> doctors;
    ListView listView;
    String PHONE_NUMBER;
    ArrayAdapter<String> arrayAdapter;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        listView = findViewById(R.id.listView);
        fab = findViewById(R.id.fab);
        doctors = new ArrayList<>();
        PHONE_NUMBER = firebaseUser.getPhoneNumber();
        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_expandable_list_item_1,doctors);
        listView.setAdapter(arrayAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AddDoctor.class));
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ShowData.class);
                intent.putExtra("doctor", arrayAdapter.getItem(i).toString());
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
                            Log.e("docId2",String.valueOf(docId2.getKey()));
                            Log.e("snapshot",String.valueOf(snapshot.getKey()));
                            if(snapshot.getKey().matches(docId2.getKey())){
                                doctors.add(String.valueOf(docId2.child("name").getValue()));
                            }
                        }
                        arrayAdapter.notifyDataSetChanged();
                    }
                }

//                if (dataSnapshot.child(PHONE_NUMBER).exists()){
//                    DataSnapshot dataSnapshot1 = dataSnapshot.child(PHONE_NUMBER).child("Doctors");
//                    for (DataSnapshot d:dataSnapshot1.getChildren()){
//                        doctors.add(d.getKey());
//                    }
//                    arrayAdapter.notifyDataSetChanged();
//                }else
//                    Toast.makeText(FirstActivity.this, "No Doctor Added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
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
    private void killApp(){
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homeIntent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
