package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class AdapterClass extends RecyclerView.Adapter<AdapterClass.myViewHolder> {

    private ArrayList<data> list;
    private DatabaseReference to,clear;

    AdapterClass(ArrayList<data> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.party_checking, parent, false);
        return new myViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final myViewHolder holder, final int position) {
        holder.party.setText(list.get(position).getParty_Name());
        holder.bill.setText(list.get(position).getRef_no());
        holder.amount.setText(String.valueOf(list.get(position).getPending()));
        @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date Date = null;
        try {
            Date = inputFormat.parse(list.get(position).getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = outputFormat.format(Date);
        holder.overdue.setText(formattedDate);

        to = FirebaseDatabase.getInstance().getReference();

                holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String party = list.get(position).getParty_Name().replace(".","_dot_").replace("/","_slash_");
                String contact = list.get(position).getContact();
                int id = list.get(position).getId().intValue();
                String area = list.get(position).getArea();

                to.child("workingSheet").child(area).child(party).child("contact").setValue(contact);
                to.child("workingSheet").child(area).child(party).child("party_name").setValue(party);
                to.child("workingSheet").child(area).child(party).child("area").setValue(area);


                java.util.Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                String formattedDate = df.format(c);

                to.child("workingSheet").child(area).child(party).child("Bills").child(String.valueOf(id)).setValue(list.get(position));
                to.child("Backup").child(formattedDate).push().setValue(list.get(position));

                clear = FirebaseDatabase.getInstance().getReference().child("checking").child(list.get(position).getArea()).child(String.valueOf(list.get(position).getId()));
                clear.removeValue();

                holder.mView.setVisibility(View.GONE);
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView party,bill,amount,overdue;
        Context context;


        public myViewHolder(final View itemView) {
            super(itemView);
            mView=itemView;

            party = mView.findViewById(R.id.Party_name);
            bill = mView.findViewById(R.id.Ref_no);
            amount = mView.findViewById(R.id.Amount);
            overdue = mView.findViewById(R.id.Overdue);
        }
    }

}
