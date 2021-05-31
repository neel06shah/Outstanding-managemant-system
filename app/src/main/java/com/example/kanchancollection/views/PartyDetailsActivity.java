package com.example.kanchancollection.views;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kanchancollection.R;
import com.example.kanchancollection.fragments.MessageFragment;
import com.example.kanchancollection.models.data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PartyDetailsActivity extends AppCompatActivity {

    RecyclerView listView;
    TextView tl,bills,cont;
    int Total = 0;
    int Count = 0;
    DatabaseReference myRef,reference,add,clear;
    ArrayList<String> bill;
    String d="";
    int c;
    String contact, party, type, area;
    EditText etAmount, etChequeDate, etCheckNo , spBank,etChequeBranch;
    Button submit;
    RadioGroup rdGroup;
    ImageButton date;
    int mYear,mMonth,mDay;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        party = intent.getStringExtra("party");
        area = intent.getStringExtra("area");
        contact = intent.getStringExtra("contact");
        c = intent.getIntExtra("count",0);

        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String dateData = dateFormat.format(calendar.getTime());

        assert party != null;
        setTitle(party.replace("_dot_","."));
        assert area != null;
        myRef= FirebaseDatabase.getInstance().getReference().child("workingSheet").child(dateData).child(area).child(party).child("Bills");
        myRef.keepSynced(true);

        listView = findViewById(R.id.listView);
        tl = findViewById(R.id.pending);
        bills = findViewById(R.id.bills);
        cont = findViewById(R.id.contact);
        etCheckNo=findViewById(R.id.etChequeNo);
        etChequeDate=findViewById(R.id.etChequeDate);
        rdGroup = findViewById(R.id.rdGroup);
        spBank=findViewById(R.id.spBank);
        etAmount = findViewById(R.id.etAmount);
        submit = findViewById(R.id.btnSubmit);
        etChequeBranch = findViewById(R.id.etChequeBranch);
        date = findViewById(R.id.date2);
        cont.setText("Contact number : "+contact);

        listView.setLayoutManager(new LinearLayoutManager(this));

        rdGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.rdCash) {
                    type="Cash";
                    etCheckNo.setVisibility(View.GONE);
                    etChequeDate.setVisibility(View.GONE);
                    spBank.setVisibility(View.GONE);
                    etChequeBranch.setVisibility(View.GONE);
                    date.setVisibility(View.GONE);
                }
                else if (checkedId==R.id.rdCheque) {
                    type="Cheque";
                    etCheckNo.setVisibility(View.VISIBLE);
                    etChequeDate.setVisibility(View.VISIBLE);
                    spBank.setVisibility(View.VISIBLE);
                    etChequeBranch.setVisibility(View.VISIBLE);
                    date.setVisibility(View.VISIBLE);
                }
                else if(checkedId==R.id.rdOnlineUPI) {
                    type="Online UPI";
                    etCheckNo.setVisibility(View.GONE);
                    etChequeDate.setVisibility(View.GONE);
                    spBank.setVisibility(View.GONE);
                    etChequeBranch.setVisibility(View.GONE);
                    date.setVisibility(View.GONE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(PartyDetailsActivity.this);

                    //Yes Button
                    builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("Share", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    LayoutInflater inflater = getLayoutInflater();
                    @SuppressLint("InflateParams") View dialoglayout = inflater.inflate(R.layout.custom_dialog, null);

                    builder.setView(dialoglayout);
                    builder.show();
                }
                else if(checkedId==R.id.rdOnlineBank){
                    type="Bank";
                    etCheckNo.setVisibility(View.GONE);
                    etChequeDate.setVisibility(View.GONE);
                    spBank.setVisibility(View.GONE);
                    etChequeBranch.setVisibility(View.GONE);
                    date.setVisibility(View.GONE);
                    AlertDialog.Builder alert = new AlertDialog.Builder(PartyDetailsActivity.this);
                    alert.setTitle("Online bank payment");
                    alert.setMessage("Beneficiary Name :\n" +
                            "Kanchan Traders\n\n" +
                            "Bank name : \n" +
                            "SVC Co-operative bank ltd.\n\n" +
                            "Branch name : \n" +
                            "Rajaji Path- Dombivali east\n\n" +
                            "IFSC code :\n" +
                            "SVCB0000182\n\n" +
                            "Account No. :\n" +
                            "118204180000189");

                    alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });

                    alert.setNegativeButton("Share", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
                    alert.show();
                }
            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("Receipt").child(String.valueOf(c)).child("Bills");
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long total = 0;
                for (DataSnapshot player : dataSnapshot.getChildren()) {
                    data data = player.getValue(com.example.kanchancollection.models.data.class);
                    assert data != null;
                    Long pen = data.getPending();
                    total = total+pen;
                }
                etAmount.setText(String.valueOf(total));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(PartyDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(0);
                        cal.set(year, month, dayOfMonth, 0, 0, 0);
                        Date chosenDate = cal.getTime();

                        // Format the date using style medium and UK locale
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        String formattedDate = df.format(chosenDate);
                        // Display the formatted date
                        etChequeDate.setText(formattedDate);
                    }
                },mYear,mMonth,mDay);
                datePickerDialog.show();

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type != null) {
                    add = FirebaseDatabase.getInstance().getReference().child("Receipt").child(String.valueOf(c));
                    Date d = Calendar.getInstance().getTime();
                    System.out.println("Current time => " + d);
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                    String formattedDate = df.format(d);
                    add.child("Party").setValue(party);
                    add.child("Type").setValue(type);
                    add.child("Amount").setValue(etAmount.getText().toString());
                    add.child("ChequeNo").setValue(etCheckNo.getText().toString());
                    add.child("ChequeDate").setValue(etChequeDate.getText().toString());
                    add.child("Bank").setValue(spBank.getText().toString());
                    add.child("date_type").setValue(formattedDate + "_" + type);
                    add.child("id").setValue(String.valueOf(c));
                    add.child("ChequeBranch").setValue(etChequeBranch.getText().toString());
                    add.child("Date").setValue(formattedDate).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            PartyDetailsActivity.this.finish();
                        }
                    });
                }
                else {
                    Toast.makeText(PartyDetailsActivity.this, "Please select the Type", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        final FirebaseRecyclerAdapter<data,dataViewHolder> recyclerAdapter=new FirebaseRecyclerAdapter<data,dataViewHolder>(
                data.class,
                R.layout.details_view_holder,
                dataViewHolder.class,
                myRef) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            protected void populateViewHolder(dataViewHolder viewHolder, data model, int position ) {
                viewHolder.setRef_no(model.getRef_no());
                viewHolder.setPending(model.getPending());
                viewHolder.setDate(model.getDate());
               // viewHolder.setOverdue(model.getOverdue());
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

        void setDate(String date) {
                d = date;
                @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy");
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
                    }
                    else {
                        DatabaseReference remove;
                        remove = reference.child(ref.replace("/","_"));
                        remove.removeValue();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.call_message_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        MessageFragment m = new MessageFragment();
        switch (item.getItemId()) {
            case R.id.action_call :
                m.callParty(contact,PartyDetailsActivity.this);
                break;
            case R.id.action_message :
                m.smsParty(contact,PartyDetailsActivity.this,area,party);
                break;
            case R.id.action_whatsapp :
                m.whatsappParty(contact,PartyDetailsActivity.this,area,party);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        clear = FirebaseDatabase.getInstance().getReference().child("Receipt");
                        clear.child(String.valueOf(c)).removeValue();
                        PartyDetailsActivity.this.finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Conformation");
        builder.setMessage("Are you sure you want to cancel your transaction?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }
}
