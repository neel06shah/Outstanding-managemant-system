package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class Party_list extends AppCompatActivity {

    private RecyclerView list_view;
    private Query databaseReference;
    String area;
    ProgressBar progressBar;
    SearchView searchView;
    TextView bills, pend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_list);

        Intent i = getIntent();
        area = i.getStringExtra("Area");

        setTitle(area);

        list_view=findViewById(R.id.list_view);
        progressBar = findViewById(R.id.progressBar);

        list_view.setHasFixedSize(true);
        list_view.setLayoutManager(new LinearLayoutManager(this));

        bills = findViewById(R.id.bills);
        pend = findViewById(R.id.pending);

        databaseReference= FirebaseDatabase.getInstance().getReference().child("workingSheet").child(area);
        databaseReference.keepSynced(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                int amount = 0;
                for(DataSnapshot children : dataSnapshot.getChildren()){
                    for(DataSnapshot child : children.child("Bills").getChildren()) {
                        count = count+1;
                        data data = child.getValue(com.example.myapplication.data.class);
                        assert data != null;
                        Long pending = data.getPending();
                        int pen = pending.intValue();
                        amount=amount+pen;
                    }
                }
                bills.setText(""+count);
                pend.setText("\u20b9 "+new DecimalFormat("##,##,###").format(amount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Party_list.this, "Please connect internet connection", Toast.LENGTH_SHORT).show();
            }
        });


        FirebaseRecyclerAdapter<party,partyViewHolder> recyclerAdapter=new FirebaseRecyclerAdapter<party,partyViewHolder>(
                party.class,
                R.layout.party,
                partyViewHolder.class,
                databaseReference) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            protected void populateViewHolder(partyViewHolder viewHolder, party model, int position ) {
                viewHolder.setParty_name(model.getParty_name());
                viewHolder.setArea(model.getArea());
                viewHolder.setContact(model.getContact());
                progressBar.setVisibility(View.GONE);
            }
        };
        list_view.setAdapter(recyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    public static class partyViewHolder extends RecyclerView.ViewHolder{
        View mView;
        String name,Area,Contact;
        TextView pend, bills;
        DatabaseReference reference,count;
        int Count = 0;

        public partyViewHolder(final View itemView) {
            super(itemView);
            mView=itemView;

            pend = mView.findViewById(R.id.pending);
            bills = mView.findViewById(R.id.bills);

            count = FirebaseDatabase.getInstance().getReference().child("Receipt");
            count.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Count = Count + 1;
                        }
                    }

                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(mView.getContext(), Details.class);
                            i.putExtra("party",name);
                            i.putExtra("area",Area);
                            i.putExtra("contact",Contact);
                            i.putExtra("count",Count);
                            mView.getContext().startActivity(i);
                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        public void setParty_name(String party_name) {
            name = party_name;
            TextView name = mView.findViewById(R.id.Party_name);
            name.setText(party_name.replace("_dot_","."));
        }

        public void setArea(String area) {
            Area = area;
            reference= FirebaseDatabase.getInstance().getReference().child("workingSheet").child(Area).child(name).child("Bills");
            reference.addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int count = 0,amount = 0;
                    for(DataSnapshot child : dataSnapshot.getChildren()) {
                        count = count+1;
                        data data = child.getValue(com.example.myapplication.data.class);
                        assert data != null;
                        Long pending = data.getPending();
                        int pen = pending.intValue();
                        amount=amount+pen;
                    }
                    bills.setText(""+count);
                    pend.setText("\u20b9 "+amount);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        public void setContact (String contact) {
            Contact = contact;
        }
    }
}