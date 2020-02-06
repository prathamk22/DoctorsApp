package com.example.doctorsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.ViewHolder> {

    Context context;
    ArrayList<DoctorModel> arrayList;
    OnClick click;

    public DoctorsAdapter(Context context, ArrayList<DoctorModel> arrayList,OnClick click) {
        this.context = context;
        this.arrayList = arrayList;
        this.click = click;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.doctor_layout,parent,false);

        return new ViewHolder(view,click);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.docName.setText(arrayList.get(position).name);
        holder.hospitalName.setText(arrayList.get(position).hospital_name);
        holder.speciality.setText(arrayList.get(position).spl);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView docName,hospitalName,speciality;
        OnClick click;
        public ViewHolder(@NonNull View itemView, OnClick click) {
            super(itemView);
            docName = itemView.findViewById(R.id.docName);
            hospitalName = itemView.findViewById(R.id.hospitalName);
            speciality = itemView.findViewById(R.id.speciality);
            this.click = click;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            click.onclick(arrayList.get(getAdapterPosition()));
        }
    }

    public interface OnClick{
        void onclick(DoctorModel doctorModel);
    }
}
