package com.example.kanchancollection.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.kanchancollection.R;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIME_OUT=3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(SplashActivity.this,
                        MainActivity.class);
                //Intent is used to switch from one activity to another.

                startActivity(i);
                //invoke the SecondActivity.

                finish();
                //the current activity will get finished.
            }
        }, SPLASH_SCREEN_TIME_OUT);



//        sync = findViewById(R.id.sync);
//        done = findViewById(R.id.done);
//        loading = findViewById(R.id.loading);
//        buttons = findViewById(R.id.buttons);
//        report = findViewById(R.id.report);
//        checking = findViewById(R.id.checking);
//
//        checking.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(SplashActivity.this);
//                alertDialog.setTitle("Password verification");
//                alertDialog.setMessage("Enter password for checking bills.");
//
//                final EditText input = new EditText(SplashActivity.this);
//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.MATCH_PARENT);
//                input.setLayoutParams(lp);
//                alertDialog.setView(input);
//
//                alertDialog.setPositiveButton("YES",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                String pass = input.getText().toString();
//                                String actualpass = "ketan241269";
//                                if(pass.equals(actualpass)) {
//                                    Intent i = new Intent(SplashActivity.this, checkingActivity.class);
//                                    startActivity(i);
//                                }
//                                else {
//                                    Toast.makeText(SplashActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
//                                    dialog.cancel();
//                                }
//                            }
//                        });
//
//                alertDialog.setNegativeButton("NO",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        });
//
//                alertDialog.show();
//            }
//        });
//
//        report.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(SplashActivity.this, reportActivity.class);
//                startActivity(i);
//            }
//        });
//
//        done.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(SplashActivity.this, MainActivity.class);
//                startActivity(i);
//            }
//        });
//
//        sync.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(SplashActivity.this);
//                alertDialog.setTitle("Password verification");
//                alertDialog.setMessage("Enter password to start sync");
//
//                final EditText input = new EditText(SplashActivity.this);
//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.MATCH_PARENT);
//                input.setLayoutParams(lp);
//                alertDialog.setView(input);
//
//                alertDialog.setPositiveButton("YES",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                String pass = input.getText().toString();
//                                String actualpass = "ketan241269";
//                                if(pass.equals(actualpass)) {
//                                    dialog.dismiss();
//                                    loading.setVisibility(View.VISIBLE);
//                                    buttons.setVisibility(View.GONE);
//
//                                    from= FirebaseDatabase.getInstance().getReference().child("masterSheet");
//                                    to=FirebaseDatabase.getInstance().getReference().child("checking");
//                                    backup_from = FirebaseDatabase.getInstance().getReference().child("workingSheet");
//
//                                    backup_from.removeValue();
//                                    to.removeValue();
//                                    from.addValueEventListener(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                            for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
//                                                String area = areaSnapshot.child("Area").getValue(String.class);
//                                                int id = areaSnapshot.child("id").getValue(Integer.class);
//                                                /*String party = areaSnapshot.child("Party_Name").getValue(String.class).replace(".", "");
//                                                String contact = areaSnapshot.child("Contact").getValue(String.class);
//                                                int id = areaSnapshot.child("id").getValue(Integer.class);
//                                                String area = areaSnapshot.child("Area").getValue(String.class);
//
//                                                to.child(area).child(party).child("contact").setValue(contact);
//                                                to.child(area).child(party).child("party_name").setValue(party);
//                                                to.child(area).child(party).child("area").setValue(area);*/
//
//                                                to.child(area).child(String.valueOf(id)).setValue(areaSnapshot.getValue(), new DatabaseReference.CompletionListener() {
//                                                    @Override
//                                                    public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {
//                                                        if (firebaseError != null) {
//                                                            Toast.makeText(SplashActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
//                                                        }
//                                                        else {
//                                                            loading.setVisibility(View.GONE);
//                                                            buttons.setVisibility(View.VISIBLE);
//                                                            Toast.makeText(SplashActivity.this, "Sync completed successfully", Toast.LENGTH_LONG).show();
//                                                        }
//                                                    }
//                                                });
//
//                                            }
//                                        }
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                        }
//                                    });
//                                }
//                                else {
//                                    Toast.makeText(SplashActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
//                                    dialog.cancel();
//                                }
//                            }
//                        });
//
//                alertDialog.setNegativeButton("NO",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        });
//
//                alertDialog.show();
//            }
//        });
    }
}