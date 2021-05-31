package com.example.kanchancollection.views;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kanchancollection.R;
import com.example.kanchancollection.models.data;
import com.example.kanchancollection.models.receipt;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class reportActivity extends AppCompatActivity {

    LinearLayout cash, cheque, upi, bank,llScroll;
    RecyclerView cashList, chequeList, upiList, bankList;
    TextView cashTotal,chequeTotal,upiTotal,bankTotal, finCash, finCheque, finUPI, finBank,finTotal;
    Query cashRef, chqRef, upiRef, bankRef;
    EditText Date;
    ImageButton cal;
    Calendar calendar;
    float cashT = 0;
    float chequeT = 0;
    float upiT = 0;
    float bankT = 0;
    float total = 0;
    private String path;
    private File myPath;
    private String imagesUri;
    private Bitmap b;
    private File filePath;
    int mYear,mMonth,mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        //Date
        Date = findViewById(R.id.Date);
        cal = findViewById(R.id.calender);
        calendar = Calendar.getInstance();
        //LinearLayouts
        cash = findViewById(R.id.cash);
        cheque = findViewById(R.id.cheque);
        upi = findViewById(R.id.upi);
        bank = findViewById(R.id.bank);
        llScroll = findViewById(R.id.llScroll);
        //RecyclerViews
        cashList = findViewById(R.id.cashList);
        chequeList = findViewById(R.id.chequeList);
        upiList = findViewById(R.id.upiList);
        bankList = findViewById(R.id.bankList);
        //Total under RecyclerView
        cashTotal = findViewById(R.id.cashTotal);
        chequeTotal = findViewById(R.id.chequeTotal);
        upiTotal = findViewById(R.id.upiTotal);
        bankTotal = findViewById(R.id.bankTotal);
        //final Totals
        finCash = findViewById(R.id.finCash);
        finBank = findViewById(R.id.finBank);
        finUPI = findViewById(R.id.finUPI);
        finCheque = findViewById(R.id.finCheque);
        finTotal = findViewById(R.id.finTotal);

        java.util.Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        Date.setText(formattedDate);
        cashRef = FirebaseDatabase.getInstance().getReference().child("Receipt").orderByChild("date_type").equalTo(formattedDate+"_Cash");
        chqRef = FirebaseDatabase.getInstance().getReference().child("Receipt").orderByChild("date_type").equalTo(formattedDate+"_Cheque");
        upiRef = FirebaseDatabase.getInstance().getReference().child("Receipt").orderByChild("date_type").equalTo(formattedDate+"_Online UPI");
        bankRef = FirebaseDatabase.getInstance().getReference().child("Receipt").orderByChild("date_type").equalTo(formattedDate+"_Bank");
        getData(cashRef, chqRef, upiRef, bankRef);

        Date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s != null) {
                    total = 0;
                    String formattedDate = s.toString();
                    cashRef = FirebaseDatabase.getInstance().getReference().child("Receipt").orderByChild("date_type").equalTo(formattedDate+"_Cash");
                    chqRef = FirebaseDatabase.getInstance().getReference().child("Receipt").orderByChild("date_type").equalTo(formattedDate+"_Cheque");
                    upiRef = FirebaseDatabase.getInstance().getReference().child("Receipt").orderByChild("date_type").equalTo(formattedDate+"_Online UPI");
                    bankRef = FirebaseDatabase.getInstance().getReference().child("Receipt").orderByChild("date_type").equalTo(formattedDate+"_Bank");
                    getData(cashRef, chqRef, upiRef, bankRef);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(reportActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                        Date.setText(formattedDate);
                    }
                },mYear,mMonth,mDay);
                datePickerDialog.show();
            }
        });
        cashList.setLayoutManager(new LinearLayoutManager(this));
        chequeList.setLayoutManager(new LinearLayoutManager(this));
        upiList.setLayoutManager(new LinearLayoutManager(this));
        bankList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getData(final Query cashRef, final Query chqRef, final Query upiRef, final Query bankRef) {
        //Cash List
        finTotal.setText("0");
        finCash.setText("0");
        finCheque.setText("0");
        finUPI.setText("0");
        finBank.setText("0");
        cashRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    cash.setVisibility(View.VISIBLE);
                    final FirebaseRecyclerAdapter<receipt, receiptViewHolder> recyclerAdapter=new FirebaseRecyclerAdapter<receipt, receiptViewHolder>(
                            receipt.class,
                            R.layout.receipt_view_holder,
                            receiptViewHolder.class,
                            cashRef) {
                        @Override
                        protected void populateViewHolder(receiptViewHolder viewHolder, receipt model, int position ) {
                            viewHolder.setParty(model.getParty());
                            viewHolder.setAmount(model.getAmount());
                            viewHolder.setId(model.getId());
                            viewHolder.setChequeNo(model.getChequeNo());
                            viewHolder.setChequeDate(model.getChequeDate());
                            viewHolder.setChequeBranch(model.getChequeBranch());
                            viewHolder.setBank(model.getBank());
                            viewHolder.setType(model.getType());
                        }
                    };
                    cashList.setAdapter(recyclerAdapter);
                }
                else {
                    cash.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Cheque List
        chqRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    cheque.setVisibility(View.VISIBLE);
                    final FirebaseRecyclerAdapter<receipt, receiptViewHolder> recycler=new FirebaseRecyclerAdapter<receipt, receiptViewHolder>(
                            receipt.class,
                            R.layout.receipt_view_holder,
                            receiptViewHolder.class,
                            chqRef) {
                        @Override
                        protected void populateViewHolder(receiptViewHolder viewHolder, receipt model, int position ) {
                            viewHolder.setParty(model.getParty());
                            viewHolder.setAmount(model.getAmount());
                            viewHolder.setId(model.getId());
                            viewHolder.setChequeNo(model.getChequeNo());
                            viewHolder.setChequeDate(model.getChequeDate());
                            viewHolder.setChequeBranch(model.getChequeBranch());
                            viewHolder.setBank(model.getBank());
                            viewHolder.setType(model.getType());
                        }
                    };
                    chequeList.setAdapter(recycler);
                }
                else {
                    cheque.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //UPI List
        upiRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    upi.setVisibility(View.VISIBLE);
                    final FirebaseRecyclerAdapter<receipt, receiptViewHolder> recyclerAdap=new FirebaseRecyclerAdapter<receipt, receiptViewHolder>(
                            receipt.class,
                            R.layout.receipt_view_holder,
                            receiptViewHolder.class,
                            upiRef) {
                        @Override
                        protected void populateViewHolder(receiptViewHolder viewHolder, receipt model, int position ) {
                            viewHolder.setParty(model.getParty());
                            viewHolder.setAmount(model.getAmount());
                            viewHolder.setId(model.getId());
                            viewHolder.setChequeNo(model.getChequeNo());
                            viewHolder.setChequeDate(model.getChequeDate());
                            viewHolder.setChequeBranch(model.getChequeBranch());
                            viewHolder.setBank(model.getBank());
                            viewHolder.setType(model.getType());
                        }
                    };
                    upiList.setAdapter(recyclerAdap);

                }
                else {
                    upi.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Bank List
        bankRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    bank.setVisibility(View.VISIBLE);
                    final FirebaseRecyclerAdapter<receipt, receiptViewHolder> Adapter=new FirebaseRecyclerAdapter<receipt, receiptViewHolder>(
                            receipt.class,
                            R.layout.receipt_view_holder,
                            receiptViewHolder.class,
                            bankRef) {
                        @Override
                        protected void populateViewHolder(receiptViewHolder viewHolder, receipt model, int position ) {
                            viewHolder.setParty(model.getParty());
                            viewHolder.setAmount(model.getAmount());
                            viewHolder.setId(model.getId());
                            viewHolder.setChequeNo(model.getChequeNo());
                            viewHolder.setChequeDate(model.getChequeDate());
                            viewHolder.setChequeBranch(model.getChequeBranch());
                            viewHolder.setBank(model.getBank());
                            viewHolder.setType(model.getType());
                        }
                    };
                    bankList.setAdapter(Adapter);
                }
                else {
                    bank.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        cashRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cashT = 0;
                if(dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        float t = Float.parseFloat(Objects.requireNonNull(child.child("Amount").getValue(String.class)));
                        cashT += t;
                    }
                    cashTotal.setText("\u20b9 "+new DecimalFormat("##,##,###").format(cashT));
                    finCash.setText(new DecimalFormat("##,##,###").format(cashT));
                }
                total += cashT;
                finTotal.setText("\u20b9 "+new DecimalFormat("##,##,###").format(total));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        chqRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chequeT = 0;
                if(dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        float t = Float.parseFloat(Objects.requireNonNull(child.child("Amount").getValue(String.class)));
                        chequeT += t;
                    }
                    chequeTotal.setText("\u20b9 "+new DecimalFormat("##,##,###").format(chequeT));
                    finCheque.setText(new DecimalFormat("##,##,###").format(chequeT));
                }
                total+=chequeT;
                finTotal.setText("\u20b9 "+new DecimalFormat("##,##,###").format(total));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        upiRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                upiT = 0;
                if(dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        float t = Float.parseFloat(Objects.requireNonNull(child.child("Amount").getValue(String.class)));
                        upiT += t;
                    }
                    upiTotal.setText("\u20b9 "+new DecimalFormat("##,##,###").format(upiT));
                    finUPI.setText(new DecimalFormat("##,##,###").format(upiT));
                }
                total+=upiT;
                finTotal.setText("\u20b9 "+new DecimalFormat("##,##,###").format(total));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        bankRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bankT = 0;
                if(dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        float t = Float.parseFloat(Objects.requireNonNull(child.child("Amount").getValue(String.class)));
                        bankT += t;
                    }
                    bankTotal.setText("\u20b9 "+new DecimalFormat("##,##,###").format(bankT));
                    finBank.setText(new DecimalFormat("##,##,###").format(bankT));
                }
                total += bankT;
                finTotal.setText("\u20b9 "+new DecimalFormat("##,##,###").format(total));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public static class receiptViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView receiptParty, receiptAmount;
        DatabaseReference reference;
        ArrayList<String> list;
        ListView listView;
        String cno,cdate,cbank,cbranch;
        LinearLayout cheque;

        public receiptViewHolder(final View itemView) {
            super(itemView);
            mView=itemView;

            receiptParty = mView.findViewById(R.id.receiptParty);
            receiptAmount = mView.findViewById(R.id.receiptAmount);
            listView = mView.findViewById(R.id.listView);
            cheque = mView.findViewById(R.id.cheque);
        }
        public void setParty(String party) {
            receiptParty.setText(party.replace("_dot_","."));
        }

        @SuppressLint("SetTextI18n")
        public void setAmount(String amount) {
            receiptAmount.setText("\u20b9 "+amount);
        }

        public void setChequeNo(String chequeNo) {
            cno = chequeNo;
        }

        public void setChequeDate(String chequeDate) {
            cdate = chequeDate;
        }

        public void setBank(String bank) {
            cbank = bank;
        }

        public void setChequeBranch (String chequeBranch) {
            cbranch = chequeBranch;
        }
        public void setType (String type) {
            TextView receiptChqNo = mView.findViewById(R.id.receiptChqNo);
            TextView receiptChqDate = mView.findViewById(R.id.receiptChqDate);
            TextView receiptChqBank = mView.findViewById(R.id.receiptChqBank);
            TextView receiptChqBranch = mView.findViewById(R.id.receiptChqBranch);

            if(type.equals("Cheque")) {
                cheque.setVisibility(View.VISIBLE);
                receiptChqNo.setText("Number : "+cno);
                receiptChqDate.setText("Date : "+cdate);
                receiptChqBank.setText("Bank : "+cbank);
                receiptChqBranch.setText("Branch : "+cbranch);
            }
            else {
                cheque.setVisibility(View.GONE);
            }
        }

        public void setId(String id) {
            reference = FirebaseDatabase.getInstance().getReference().child("Receipt").child(id).child("Bills");
            reference.addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    list = new ArrayList<>();
                    for (DataSnapshot player : dataSnapshot.getChildren()) {
                        data data = player.getValue(com.example.kanchancollection.models.data.class);
                        assert data != null;
                        String date = data.getDate();
                        String bill = data.getRef_no();
                        Long pen = data.getPending();
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
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mView.getContext(), android.R.layout.simple_list_item_1, list);
                    listView.setAdapter(arrayAdapter);
                    setListViewHeightBasedOnChildren(listView);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.report_menu, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_pdf :
                takeScreenShot();
                break;
            case R.id.action_calculator :
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.pccomputeramreli.Cash_Calculator");
                if (intent != null) {
                    // We found the activity now start the activity
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } else {
                    // Bring user to the market or let them choose an app?
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setData(Uri.parse("market://details?id=" + "com.pccomputeramreli.Cash_Calculator"));
                }
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void takeScreenShot() {

        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Report/");
        if (!folder.exists()) {
            boolean success = folder.mkdir();
        }

        path = folder.getAbsolutePath();
        path = path + "/" + Date.getText().toString() + ".pdf";// path where pdf will be stored

        int totalHeight = (int)llScroll.getHeight();// parent view height
        int totalWidth = (int)llScroll.getWidth();// parent view width

        //Save bitmap to  below path
        String extr = Environment.getExternalStorageDirectory() + "/Report/";
        File file = new File(extr);
        if (!file.exists())
            file.mkdir();
        String fileName = Date.getText().toString() + ".jpg";
        myPath = new File(extr, fileName);
        imagesUri = myPath.getPath();
        FileOutputStream fos = null;
        b = getBitmapFromView(llScroll, totalHeight, totalWidth);

        try {
            fos = new FileOutputStream(myPath);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        createPdf();// create pdf after creating bitmap and saving
    }

    private Bitmap getBitmapFromView(View view, int totalHeight, int totalWidth) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        }   else{
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

    private void createPdf() {
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(b.getWidth(), b.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#ffffff"));
        canvas.drawPaint(paint);
        Bitmap bitmap = Bitmap.createScaledBitmap(b, b.getWidth(), b.getHeight(), true);
        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);
        filePath = new File(path);
        try {
            document.writeTo(new FileOutputStream(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }


        // close the document
        document.close();
        openPdf();// You can open pdf after complete
    }

    private void openPdf() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri apkURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getApplicationContext()
                        .getPackageName() + ".provider", filePath);
        intent.setDataAndType(apkURI, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

}
