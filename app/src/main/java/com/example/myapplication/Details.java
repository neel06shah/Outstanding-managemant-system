package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Details extends AppCompatActivity {

    RecyclerView listView;
    TextView tl;
    int Total = 0;
    DatabaseReference myRef;
    ImageButton call, sms, wapp;
    String d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        String party = intent.getStringExtra("party");
        String area = intent.getStringExtra("area");
        final String contact = intent.getStringExtra("contact");

        setTitle(party);
        myRef= FirebaseDatabase.getInstance().getReference().child("workingSheet").child(area).child(party).child("Bills");
        myRef.keepSynced(true);

        listView = findViewById(R.id.listView);
        tl = findViewById(R.id.total);
        call = findViewById(R.id.call);
        sms = findViewById(R.id.sms);
        wapp =findViewById(R.id.whatsapp);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    data data = child.getValue(com.example.myapplication.data.class);
                    String date = data.getDate();
                    String ref = data.getRef_no();
                    Long pen = data.getPending();

                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
                    Date Date = null;
                    try {
                        Date = inputFormat.parse(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String formattedDate = outputFormat.format(Date);

                    if (d == null) {
                        d = "\n\nDate : " + formattedDate + "\nRef. no. : " + ref + "\nPending amount :" + pen+".00";
                    } else {
                        d = d + "\n\nDate : " + formattedDate + "\nRef. no. : " + ref + "\nPending amount :" + pen+".00";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone2 = "+91"+contact;
                Intent intent2 = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone2, null));
                startActivity(intent2);
            }
        });

        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("smsto:"+"+91"+contact);
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra("sms_body", "Dear Customer,\nHope you and your family are safe." +
                        " This is to inform you  that your outstanding with us is \u20b9"+Total+
                        " You can send payment by UPI or by direct bank transfer. " +
                        "Please let us know if you have any questions.\n\n"+"Bill details : "+d+"\n\n"+
                        "Total Amount : \u20b9"+Total+".00\n\n"+
                        "Thanking you,\nImpression Enterprises\nDombivali");
                startActivity(intent);
            }
        });

        wapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = "+91"+contact;
                String message = "Dear Customer,\nHope you and your family are safe." +
                        " This is to inform you  that your outstanding with us is \u20b9"+Total+
                        " You can send payment by UPI or by direct bank transfer. " +
                        "Please let us know if you have any questions.\n\n"+"Bill details : "+d+"\n\n"+
                        "*Total Amount : \u20b9"+Total+".00*\n\n"+
                        "Thanking you,\nImpression Enterprises\nDombivali";
                PackageManager packageManager = getPackageManager();
                Intent i = new Intent(Intent.ACTION_VIEW);

                try {
                    String url = "https://api.whatsapp.com/send?phone="+ phone +"&text=" + URLEncoder.encode(message, "UTF-8");
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    if (i.resolveActivity(packageManager) != null) {
                        startActivity(i);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerAdapter<data,dataViewHolder> recyclerAdapter=new FirebaseRecyclerAdapter<data,dataViewHolder>(
                data.class,
                R.layout.party_wise,
                dataViewHolder.class,
                myRef) {
            @Override
            protected void populateViewHolder(dataViewHolder viewHolder, data model, int position ) {
                    viewHolder.setRef_no(model.getRef_no());
                    viewHolder.setPending(String.valueOf(model.getPending()));
                    viewHolder.setDate(model.getDate());
                    viewHolder.setOverdue(String.valueOf(model.getOverdue()));

                    Long t = model.getPending();
                    int total = t.intValue();
                    Total = Total + total;
                    tl.setText("Total : "+Total);
            }
        };
        listView.setAdapter(recyclerAdapter);

    }

    public static class dataViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public dataViewHolder(View itemView) {
            super(itemView);
            mView=itemView;

        }

        public void setPending(String Pending) {
            TextView tvPrintBranch = mView.findViewById(R.id.pending);
            tvPrintBranch.setText(""+Pending);
        }

        public void setOverdue (String Overdue) {
            TextView Over = mView.findViewById(R.id.overdue);
            Over.setText(""+Overdue);
        }

        public void setDate(String date) {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date Date = null;
                try {
                    Date = inputFormat.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formattedDate = outputFormat.format(Date);
                TextView tvPrintBranch = mView.findViewById(R.id.date);
                tvPrintBranch.setText(formattedDate);
        }
        public void setRef_no(String ref_no) {
            TextView tvPrintBranch = mView.findViewById(R.id.bill);
            tvPrintBranch.setText(ref_no);
        }


    }
}
