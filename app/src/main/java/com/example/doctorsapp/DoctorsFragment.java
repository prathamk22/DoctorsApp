package com.example.doctorsapp;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorsFragment extends Fragment implements DoctorsAdapter.OnClick {

    ArrayList<DoctorModel> arrayList;
    DoctorsAdapter adapter;
    String phone;
    ProgressBar progressBar;
    public DoctorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctors, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.fab);
         progressBar = view.findViewById(R.id.progressBar);

        arrayList = new ArrayList<>();
        phone = getArguments().getString("phone");
        getData();
        adapter = new DoctorsAdapter(getContext(), arrayList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FirstActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

    private void getData() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                DataSnapshot doc = dataSnapshot.child("Doctors");
                for (DataSnapshot dataSnapshot1 : doc.getChildren()){
                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){
                        String name = dataSnapshot2.child("name").getValue().toString();
                        String spl = dataSnapshot2.child("spl").getValue().toString();
                        String hospital_name = dataSnapshot2.child("hospital_name").getValue().toString();

                        DoctorModel model = new DoctorModel(hospital_name,name,spl,dataSnapshot2.getKey());
                        arrayList.add(model);
                    }
                }
                progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onclick(DoctorModel model) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Patient").child(phone).child("Doctors").child(model.id).setValue(model.name);
        Toast.makeText(getContext(), model.name.concat(" added to doctors list"), Toast.LENGTH_SHORT).show();
    }
}
