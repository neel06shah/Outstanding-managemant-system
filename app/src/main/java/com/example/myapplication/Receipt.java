package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Receipt extends AppCompatActivity {

    TextView Party_name,Total;
    DatabaseReference reference,clear,add;
    ArrayList<String> list;
    ListView listView;
    int total = 0;
    EditText etAmount, etChequeDate, etCheckNo , spBank,etChequeBranch;
    String type;
    Button submit;
    RadioGroup rdGroup;
    ImageButton date;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        setTitle("Receipt Details");

        listView = findViewById(R.id.listView);
        Total = findViewById(R.id.total);
        etCheckNo=findViewById(R.id.etChequeNo);
        etChequeDate=findViewById(R.id.etChequeDate);
        rdGroup = findViewById(R.id.rdGroup);
        spBank=findViewById(R.id.spBank);
        etAmount = findViewById(R.id.etAmount);
        submit = findViewById(R.id.btnSubmit);
        etChequeBranch = findViewById(R.id.etChequeBranch);
        date = findViewById(R.id.date);

        Intent i = getIntent();
        final String party = i.getStringExtra("party");
        id = i.getIntExtra("id",0);

        Party_name = findViewById(R.id.Party_name);
        assert party != null;
        Party_name.setText(party.replace("_dot_","."));

        reference = FirebaseDatabase.getInstance().getReference().child("Receipt").child(String.valueOf(id)).child("Bills");
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot player : dataSnapshot.getChildren()) {
                    data data = player.getValue(com.example.myapplication.data.class);
                    assert data != null;
                    String date = data.getDate();
                    String bill = data.getRef_no();
                    Long pen = data.getPending();
                    int p = Math.toIntExact(pen);
                    total = total+p;
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
                    list.add(formattedDate+"   "+bill+"   "+pen);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Receipt.this, android.R.layout.simple_list_item_1, list);
                listView.setAdapter(arrayAdapter);
                setListViewHeightBasedOnChildren(listView);
                Total.setText("Total : \u20b9"+total);
                etAmount.setText(String.valueOf(total));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
                    AlertDialog.Builder builder = new AlertDialog.Builder(Receipt.this);

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
                    AlertDialog.Builder alert = new AlertDialog.Builder(Receipt.this);
                    alert.setTitle("Online bank payment");
                    alert.setMessage("Beneficiary Name :\n" +
                            "Impression Enterprises\n" +
                            "\n" +
                            "Bank name : \n" +
                            "Shamrao Vithal co-op bank ltd.\n" +
                            "\n" +
                            "Branch name : \n" +
                            "Rajaji Path- Dombivali east\n" +
                            "\n" +
                            "IFSC code :\n" +
                            "SVCB0000182\n" +
                            "\n" +
                            "Account No. :\n" +
                            "118204180000050\n");

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

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dFragment = new DatePickerFragment();
                dFragment.show(getSupportFragmentManager(), "Date Picker");
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add = FirebaseDatabase.getInstance().getReference().child("Receipt").child(String.valueOf(id));

                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                String formattedDate = df.format(c);

                add.child("Party").setValue(party);
                add.child("Type").setValue(type);
                add.child("Amount").setValue(etAmount.getText().toString());
                add.child("ChequeNo").setValue(etCheckNo.getText().toString());
                add.child("ChequeDate").setValue(etChequeDate.getText().toString());
                add.child("Bank").setValue(spBank.getText().toString());
                add.child("date_type").setValue(formattedDate+"_"+type);
                add.child("id").setValue(String.valueOf(id));
                add.child("ChequeBranch").setValue(etChequeBranch.getText().toString());
                add.child("Date").setValue(formattedDate).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Receipt.this.finish();
                    }
                });

            }
        });

    }
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter mAdapter = listView.getAdapter();
        int totalHeight = 0;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, listView);
            mView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            totalHeight += mView.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    public void onBackPressed() {
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        clear = FirebaseDatabase.getInstance().getReference().child("Receipt");
                        clear.child(String.valueOf(id)).removeValue();
                        Receipt.this.finish();
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

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
        @NonNull
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(Objects.requireNonNull(getActivity()),
                    AlertDialog.THEME_HOLO_LIGHT, this,year,month,day);
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void onDateSet(DatePicker view, int year, int month, int day){
            EditText tv = Objects.requireNonNull(getActivity()).findViewById(R.id.etChequeDate);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(0);
            cal.set(year, month, day, 0, 0, 0);
            Date chosenDate = cal.getTime();

            // Format the date using style medium and UK locale
            @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = df.format(chosenDate);
            // Display the formatted date
            tv.setText(formattedDate);
        }
    }

}
