package com.example.kanchancollection.views;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kanchancollection.R;
import com.example.kanchancollection.adapters.PartyAdapter;
import com.example.kanchancollection.models.data;
import com.example.kanchancollection.models.party;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PartyListActivity extends AppCompatActivity {

    private RecyclerView list_view;
    private DatabaseReference databaseReference;
    String area;
    ProgressBar progressBar;
    SearchView searchView;
    TextView bills, pend;
    ArrayList<party> list;

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

        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String date = dateFormat.format(calendar.getTime());

        databaseReference= FirebaseDatabase.getInstance().getReference().child("workingSheet").child(date).child(area);
        databaseReference.keepSynced(true);

    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                int amount = 0;
                list = new ArrayList<>();
                if(dataSnapshot.exists()) {
                    for (DataSnapshot children : dataSnapshot.getChildren()) {
                        list.add(children.getValue(party.class));
                        for (DataSnapshot child : children.child("Bills").getChildren()) {
                            count = count + 1;
                            data data = child.getValue(data.class);
                            assert data != null;
                            Long pending = data.getPending();
                            int pen = pending.intValue();
                            amount = amount + pen;
                        }
                    }
                    bills.setText("" + count);
                    pend.setText("\u20b9 " + new DecimalFormat("##,##,###").format(amount));
                    PartyAdapter adapter = new PartyAdapter(list, PartyListActivity.this);
                    list_view.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PartyListActivity.this, "Please connect internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void search(String newText) {
        ArrayList<party> newList = new ArrayList<>();
        for (party object : list) {
            if(object.getParty_name().toLowerCase().contains(newText)) {
                newList.add(object);
            }
        }
        PartyAdapter partyAdapter = new PartyAdapter(newList, PartyListActivity.this);
        list_view.setAdapter(partyAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return true;
            }
        });
        return true;
    }
}