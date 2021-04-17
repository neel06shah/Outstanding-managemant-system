package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        ArrayList<String> bill;
        String d="";
        Button call , sms, whatsapp;
        private int Total=0;

        public partyViewHolder(final View itemView) {
            super(itemView);
            mView=itemView;

            pend = mView.findViewById(R.id.pending);
            bills = mView.findViewById(R.id.bills);
            call = mView.findViewById(R.id.call);
            sms = mView.findViewById(R.id.sms);
            whatsapp = mView.findViewById(R.id.whatsapp);

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

        public void setContact (final String contact) {
            Contact = contact;
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

            DatabaseReference myRef= FirebaseDatabase.getInstance().getReference().child("workingSheet").child(area).child(name).child("Bills");
            myRef.addValueEventListener(new ValueEventListener() {
                @SuppressLint({"SetTextI18n", "DefaultLocale"})
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    bill = new ArrayList<>();
                    for(DataSnapshot child : dataSnapshot.getChildren()) {
                        Count = Count+1;
                        data model = child.getValue(data.class);
                        assert model != null;
                        Long t = model.getPending();
                        String id = model.getRef_no();
                        String date = model.getDate();

                        @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
                        Date Date = null;
                        try {
                            Date = inputFormat.parse(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        assert Date != null;
                        String formattedDate = outputFormat.format(Date);
                        bill.add("Date : "+formattedDate+"\nRef no. : "+id+"\nAmount : \u20b9"+t);

                        int total = t.intValue();
                        Total = Total + total;
                    }

                    for (int i=0;i<bill.size();i++) {
                        d = d.concat("\n\n"+bill.get(i));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            //Button Code HERE

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phone2 = "+91"+Contact;
                    Intent intent2 = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone2, null));
                    mView.getContext().startActivity(intent2);
                }
            });

            sms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(mView.getContext());
                    alertDialog.setTitle("Choose contact number");
                    alertDialog.setMessage("Enter contact number on which you want to send the Message");

                    final EditText input = new EditText(mView.getContext());
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);
                    alertDialog.setView(input);
                    input.setText(Contact);

                    alertDialog.setPositiveButton("Send",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String message = "Dear Customer, \n"+name+",\n\nHope everything is fine at your end considering the present Covid 19 situation. Take care of yourself and your family" +
                                            "\n\nThis is to inform you  that your outstanding with us is \u20b9"+Total+
                                            ".00. You can send payment by *UPI or by direct bank transfer*. " +
                                            "Please let us know if you have any questions.\n\n"+"Bill details : "+d+"\n\n"+
                                            "Total Amount : \u20b9"+Total+".00\n\n"+"*Payment options :*\n\nUPI Payment :\nName : Impression Enterprises\nNumber : 9323610419\n\nBank Payment : "+
                                            "\nBeneficiary Name :\n" +
                                            "Impression Enterprises\n" +
                                            "Bank name : \n" +
                                            "Shamrao Vithal co-op bank ltd.\n" +
                                            "Branch name : \n" +
                                            "Rajaji Path- Dombivali east\n" +
                                            "IFSC code :\n" +
                                            "SVCB0000182\n" +
                                            "Account No. :\n" +
                                            "118204180000050"+
                                            "\n\nIgnore if already Paid\n\nThanking you,\nImpression Enterprises\nDombivali";
                                    Uri uri = Uri.parse("smsto:"+"+91"+input.getText().toString());
                                    Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                                    intent.putExtra("sms_body", message);
                                    mView.getContext().startActivity(intent);
                                }
                            });

                    alertDialog.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.show();
                }
            });

            whatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder alertDialoge = new AlertDialog.Builder(mView.getContext());
                    alertDialoge.setTitle("Choose contact number");
                    alertDialoge.setMessage("Enter contact number on which you want to send the Message");

                    final EditText in = new EditText(mView.getContext());
                    LinearLayout.LayoutParams lpl = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    in.setLayoutParams(lpl);
                    alertDialoge.setView(in);
                    in.setText(Contact);

                    alertDialoge.setPositiveButton("Send",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String phone = "+91"+in.getText().toString();
                                    String message = "Dear Customer, \n"+name+",\n\nHope everything is fine at your end considering the present Covid 19 situation. Take care of yourself and your family" +
                                            "\n\nThis is to inform you  that your outstanding with us is \u20b9"+Total+
                                            ".00. You can send payment by *UPI or by direct bank transfer*. " +
                                            "Please let us know if you have any questions.\n\n"+"Bill details : "+d+"\n\n"+
                                            "Total Amount : \u20b9"+Total+".00\n\n"+"*Payment options :*\n\nUPI Payment :\nName : Impression Enterprises\nNumber : 9323610419 \n\nBank Payment : "+
                                            "\nBeneficiary Name :\n" +
                                            "Impression Enterprises\n" +
                                            "Bank name : \n" +
                                            "Shamrao Vithal co-op bank ltd.\n" +
                                            "Branch name : \n" +
                                            "Rajaji Path- Dombivali east\n" +
                                            "IFSC code :\n" +
                                            "SVCB0000182\n" +
                                            "Account No. :\n" +
                                            "118204180000050"+
                                            "\n\nIgnore if already Paid\n\nThanking you,\nImpression Enterprises\nDombivali";
                                    PackageManager packageManager = mView.getContext().getPackageManager();
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    try {
                                        String url = "https://api.whatsapp.com/send?phone="+ phone +"&text=" + URLEncoder.encode(message, "UTF-8");
                                        i.setPackage("com.whatsapp");
                                        i.setData(Uri.parse(url));
                                        if (i.resolveActivity(packageManager) != null) {
                                            mView.getContext().startActivity(i);
                                        }
                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            });

                    alertDialoge.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialoge.show();

                }
            });
        }

    }
}