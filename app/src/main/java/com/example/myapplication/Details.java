package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.google.firebase.database.ValueEventListener;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Details extends AppCompatActivity {

    RecyclerView listView;
    TextView tl,bills,cont;
    int Total = 0;
    int Count = 0;
    DatabaseReference myRef;
    ImageButton call, sms, wapp, Receipt;
    ArrayList<String> bill;
    String d="";
    int c;
    Uri uri;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        final String party = intent.getStringExtra("party");
        final String area = intent.getStringExtra("area");
        final String contact = intent.getStringExtra("contact");
        c = intent.getIntExtra("count",0);

        assert party != null;
        setTitle(party.replace("_dot_","."));
        assert area != null;
        myRef= FirebaseDatabase.getInstance().getReference().child("workingSheet").child(area).child(party).child("Bills");
        myRef.keepSynced(true);

        listView = findViewById(R.id.listView);
        tl = findViewById(R.id.pending);
        call = findViewById(R.id.call);
        sms = findViewById(R.id.sms);
        bills = findViewById(R.id.bills);
        wapp =findViewById(R.id.whatsapp);
        Receipt = findViewById(R.id.Receipt);
        cont = findViewById(R.id.contact);

        cont.setText("Contact number : "+contact);

        Receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Details.this, com.example.myapplication.Receipt.class);
                i.putExtra("id",c);
                i.putExtra("party",party);
                i.putExtra("area",area);
                startActivity(i);
                finish();
            }
        });

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

                tl.setText("\u20b9 "+new DecimalFormat("##,##,###").format(Total));
                bills.setText(""+Count);

                for (int i=0;i<bill.size();i++) {
                    d = d + "\n\n"+bill.get(i);
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
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Details.this);
                alertDialog.setTitle("Choose contact number");
                alertDialog.setMessage("Enter contact number on which you want to send the Message");

                final EditText input = new EditText(Details.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                input.setText(contact);

                alertDialog.setPositiveButton("Send",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String message = "Dear Customer,\nHope you and your family are safe." +
                                        " This is to inform you  that your outstanding with us is \u20b9"+Total+
                                        ".00. You can send payment by *UPI or by direct bank transfer*. " +
                                        "Please let us know if you have any questions.\n\n"+"Bill details : "+d+"\n\n"+
                                        "Total Amount : \u20b9"+Total+".00\n\n"+"*Payment options :*\n\nUPI Payment :\nName : Impression Enterprises\nNumber : 9323610419\nUPI ID : 9323610419-1@okbizaxis \n\nBank Payment : "+
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
                                        "\n\nThanking you,\nImpression Enterprises\nDombivali";
                                Uri uri = Uri.parse("smsto:"+"+91"+input.getText().toString());
                                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                                intent.putExtra("sms_body", message);
                                startActivity(intent);
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

        wapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Details.this);
                alertDialog.setTitle("Choose contact number");
                alertDialog.setMessage("Enter contact number on which you want to send the Message");

                final EditText input = new EditText(Details.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                input.setText(contact);

                alertDialog.setPositiveButton("Send",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String phone = "+91"+input.getText().toString();
                                String message = "Dear Customer,\nHope you and your family are safe." +
                                        " This is to inform you  that your outstanding with us is \u20b9"+Total+
                                        ".00. You can send payment by *UPI or by direct bank transfer*. " +
                                        "Please let us know if you have any questions.\n\n"+"Bill details : "+d+"\n\n"+
                                        "Total Amount : \u20b9"+Total+".00\n\n"+"*Payment options :*\n\nUPI Payment :\nName : Impression Enterprises\nNumber : 9323610419\nUPI ID : 9323610419-1@okbizaxis \n\nBank Payment : "+
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
                                        "\n\nThanking you,\nImpression Enterprises\nDombivali";
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

                alertDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();
            }
        });

        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        final FirebaseRecyclerAdapter<data,dataViewHolder> recyclerAdapter=new FirebaseRecyclerAdapter<data,dataViewHolder>(
                data.class,
                R.layout.party_wise,
                dataViewHolder.class,
                myRef) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            protected void populateViewHolder(dataViewHolder viewHolder, data model, int position ) {
                viewHolder.setRef_no(model.getRef_no());
                viewHolder.setPending(model.getPending());
                viewHolder.setDate(model.getDate());
                viewHolder.setOverdue(model.getOverdue());
                viewHolder.setID(c);
            }
        };
        listView.setAdapter(recyclerAdapter);
    }

    public static class dataViewHolder extends RecyclerView.ViewHolder {
        View mView;
        DatabaseReference reference;
        String d,ref;
        Long p,o;
        CheckBox selection;

        public dataViewHolder(final View itemView) {
            super(itemView);
            mView=itemView;

            selection = mView.findViewById(R.id.selection);
        }

        @SuppressLint("SetTextI18n")
        public void setPending(Long Pending) {
            p = Pending;
            TextView tvPrintBranch = mView.findViewById(R.id.pending);
            tvPrintBranch.setText(""+Pending);
        }

        @SuppressLint("SetTextI18n")
        @RequiresApi(api = Build.VERSION_CODES.N)
        void setOverdue(Long Overdue) {
            o = Overdue;
            TextView Over = mView.findViewById(R.id.overdue);
            Over.setText(""+Overdue);
            int l = Math.toIntExact(Overdue);
            if(l > 31) {
                Over.setTextColor(Color.parseColor("#ff0000"));
            }
            else {
                Over.setTextColor(Color.parseColor("#000000"));
            }
        }

        void setDate(String date) {
                d = date;
                @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date Date = null;
                try {
                    Date = inputFormat.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            assert Date != null;
            String formattedDate = outputFormat.format(Date);
                TextView tvPrintBranch = mView.findViewById(R.id.date);
                tvPrintBranch.setText(formattedDate);
        }
        void setRef_no(final String ref_no) {
            ref = ref_no;
            TextView tvPrintBranch = mView.findViewById(R.id.bill);
            tvPrintBranch.setText(ref_no);
        }
        void setID(int ID) {
            reference = FirebaseDatabase.getInstance().getReference().child("Receipt").child(String.valueOf(ID)).child("Bills");
            selection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        data data = new data(null,d,null,null,null,ref,o,p,null,null);
                        reference.child(ref.replace("/","_")).setValue(data);
                        selection.setText("Selected");
                    }
                    else {
                        DatabaseReference remove;
                        remove = reference.child(ref.replace("/","_"));
                        remove.removeValue();
                        selection.setText("Select here");
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }


    }
}
