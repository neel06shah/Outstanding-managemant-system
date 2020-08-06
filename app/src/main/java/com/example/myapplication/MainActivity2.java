package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity2 extends AppCompatActivity {

    DatabaseReference to,from,backup,backup_from;
    ImageButton sync,done,report,checking;
    ProgressBar loading;
    LinearLayout buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sync = findViewById(R.id.sync);
        done = findViewById(R.id.done);
        loading = findViewById(R.id.loading);
        buttons = findViewById(R.id.buttons);
        report = findViewById(R.id.report);
        checking = findViewById(R.id.checking);

        checking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity2.this, checkingActivity.class);
                startActivity(i);
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity2.this, reportActivity.class);
                startActivity(i);
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(i);
            }
        });

        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity2.this);
                alertDialog.setTitle("Password verification");
                alertDialog.setMessage("Enter password to start sync");

                final EditText input = new EditText(MainActivity2.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String pass = input.getText().toString();
                                String actualpass = "ketan241269";
                                if(pass.equals(actualpass)) {
                                    dialog.dismiss();
                                    loading.setVisibility(View.VISIBLE);
                                    buttons.setVisibility(View.GONE);

                                    from= FirebaseDatabase.getInstance().getReference().child("masterSheet");
                                    to=FirebaseDatabase.getInstance().getReference().child("checking");
                                    backup_from = FirebaseDatabase.getInstance().getReference().child("workingSheet");

                                    backup_from.removeValue();
                                    to.removeValue();
                                    from.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                                                String area = areaSnapshot.child("Area").getValue(String.class);
                                                int id = areaSnapshot.child("id").getValue(Integer.class);
                                                /*String party = areaSnapshot.child("Party_Name").getValue(String.class).replace(".", "");
                                                String contact = areaSnapshot.child("Contact").getValue(String.class);
                                                int id = areaSnapshot.child("id").getValue(Integer.class);
                                                String area = areaSnapshot.child("Area").getValue(String.class);

                                                to.child(area).child(party).child("contact").setValue(contact);
                                                to.child(area).child(party).child("party_name").setValue(party);
                                                to.child(area).child(party).child("area").setValue(area);*/

                                                to.child(area).child(String.valueOf(id)).setValue(areaSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {
                                                        if (firebaseError != null) {
                                                            Toast.makeText(MainActivity2.this, "Error occurred", Toast.LENGTH_SHORT).show();
                                                        }
                                                        else {
                                                            loading.setVisibility(View.GONE);
                                                            buttons.setVisibility(View.VISIBLE);
                                                            Toast.makeText(MainActivity2.this, "Sync completed successfully", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });

                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                                else {
                                    Toast.makeText(MainActivity2.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            }
                        });

                alertDialog.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();
            }
        });
    }
}