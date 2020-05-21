package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Splash extends AppCompatActivity {

    ImageView imgView;
    DatabaseReference to,from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imgView=findViewById(R.id.imgView);

        from= FirebaseDatabase.getInstance().getReference().child("masterSheet");
        to=FirebaseDatabase.getInstance().getReference().child("workingSheet");

        to.removeValue();
        from.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String party = areaSnapshot.child("Party_Name").getValue(String.class).replace(".", "");
                    String area = areaSnapshot.child("Area").getValue(String.class);
                    String contact = areaSnapshot.child("Contact").getValue(String.class);
                    int id = areaSnapshot.child("id").getValue(Integer.class);

                    to.child(area).child(party).child("contact").setValue(contact);
                    to.child(area).child(party).child("party_name").setValue(party);
                    to.child(area).child(party).child("area").setValue(area);

                    to.child(area).child(party).child("Bills").push().setValue(areaSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {
                            if (firebaseError != null) {
                                Toast.makeText(Splash.this, "Error occurred", Toast.LENGTH_SHORT).show();
                            }
                            else {
                            }
                        }
                    });
                }
                Intent i = new Intent(Splash.this, MainActivity.class);
                startActivity(i);
                finish();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }
}
