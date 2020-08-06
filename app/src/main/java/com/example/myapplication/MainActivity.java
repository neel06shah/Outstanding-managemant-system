package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    DatabaseReference reference;
    ProgressBar progressBar;
    TextView bills, pend;
    final ArrayList<String> area = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Choose your Area");

        progressBar = findViewById(R.id.progressBar);
        listView = findViewById(R.id.listView);

        pend = findViewById(R.id.pending);
        bills = findViewById(R.id.bills);
        reference = FirebaseDatabase.getInstance().getReference().child("workingSheet");

        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot datas) {
                int count = 0;
                int amount = 0;
                for(DataSnapshot dataSnapshot : datas.getChildren()) {
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot player : dataSnapshot.getChildren()) {
                    area.add(player.getKey());
                }
                progressBar.setVisibility(View.GONE);
                ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, area);
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = area.get(position);
                Intent i = new Intent(MainActivity.this,Party_list.class);
                i.putExtra("Area",selected);
                startActivity(i);
            }
        });

    }
}
