package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.SearchView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class checkingList extends AppCompatActivity {

    private RecyclerView list_view;
    private Query databaseReference;
    String area;
    ArrayList<data> myList;
    ArrayList<data> list;
    AdapterClass adapterClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checking_list);

        Intent i = getIntent();
        area = i.getStringExtra("area");
        setTitle(area);


        list_view=findViewById(R.id.list_view);
        list_view.setHasFixedSize(true);
        list_view.setLayoutManager(new LinearLayoutManager(this));

        databaseReference= FirebaseDatabase.getInstance().getReference().child("checking").child(area);


    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    list = new ArrayList<>();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        list.add(ds.getValue(data.class));
                    }
                    adapterClass = new AdapterClass(list);
                    list_view.setAdapter(adapterClass);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                adapterClass.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return true;
            }
        });
        return true;
    }

    private void search(String s) {
        myList = new ArrayList<>();
        for (data object : list) {
            if (object.getRef_no().toLowerCase().contains(s.toLowerCase()) || object.getParty_Name().toLowerCase().contains(s.toLowerCase())) {
                myList.add(object);
            }
        }
        adapterClass = new AdapterClass(myList);
        list_view.setAdapter(adapterClass);
    }
}
