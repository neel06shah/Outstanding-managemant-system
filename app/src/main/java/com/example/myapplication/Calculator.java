package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.net.URLEncoder;


public class Calculator extends AppCompatActivity{

    int AssumeTotal, ActualTotal; //assumed total cash
    int S10, S20, S50, S100, S200, S500, S2000,S1; //for storing total value for number of each note
    EditText eT2000,eT500,eT200,eT100,eT50,eT20,eT10,eT1;
    TextView tV2000,tV500,tV200,tV100,tV50,tV20,tV10,tV1,tactualcash;

    int getActualTotal() {
        ActualTotal = S10 + S20 + S50 + S100 + S200 + S500 + S2000+S1;
        return ActualTotal;
    }

    boolean matchOrNot() {
        return ActualTotal == AssumeTotal;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        Intent i = getIntent();
        String cash = i.getStringExtra("cash");

        EditText etotalCash = findViewById(R.id.totalcash);
        etotalCash.setText(cash);
        assert cash != null;
        AssumeTotal = Integer.parseInt(cash);

        etotalCash.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    AssumeTotal = Integer.parseInt(s.toString());
                } catch (NumberFormatException e) {
                    AssumeTotal = 0;
                    System.out.println("Assume Cash Exception :" + AssumeTotal + " |" + e);
                }
            }
        });

        tactualcash = findViewById(R.id.actualCash);

        eT2000 = findViewById(R.id.N_two3zero);
        tV2000 = findViewById(R.id.A_two3zero);

        eT2000.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //You can also apply your logic here instead of afterTextChanged

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    S2000 = 2000 * Integer.parseInt(s.toString());
                    tV2000.setText(S2000 + "");
                    tactualcash.setText(getActualTotal() + "");
                } catch (NumberFormatException e) {
                    S2000 = 0;
                    tV2000.setText(S2000 + "");
                    tactualcash.setText(getActualTotal() + "");
                    System.out.println("exception123:" + e);
                }


            }
        });


        eT500 = findViewById(R.id.N_five2zero);
        tV500 = findViewById(R.id.A_five2zero);

        eT500.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //You can also apply your logic here instead of afterTextChanged

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    S500 = 500 * Integer.parseInt(s.toString());
                    tV500.setText(S500 + "");
                    tactualcash.setText(getActualTotal() + "");
                } catch (NumberFormatException e) {
                    S50 = 0;
                    tV500.setText(S500 + "");
                    tactualcash.setText(getActualTotal() + "");
                    System.out.println("exception123:" + e);
                }


            }
        });

        eT200 = findViewById(R.id.N_two2zero);
        tV200 = findViewById(R.id.A_two2zero);

        eT200.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //You can also apply your logic here instead of afterTextChanged

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    S200 = 200 * Integer.parseInt(s.toString());
                    tV200.setText(S200 + "");
                    tactualcash.setText(getActualTotal() + "");
                } catch (NumberFormatException e) {
                    S200 = 0;
                    tV200.setText(S200 + "");
                    tactualcash.setText(getActualTotal() + "");
                    System.out.println("exception123:" + e);
                }


            }
        });


        eT1 = findViewById(R.id.N_one);
        tV1 = findViewById(R.id.A_one);

        eT1.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //You can also apply your logic here instead of afterTextChanged

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    S1 = Integer.parseInt(s.toString());
                    tV1.setText(S1 + "");
                    tactualcash.setText(getActualTotal() + "");
                } catch (NumberFormatException e) {
                    S1 = 0;
                    tV1.setText(S1 + "");
                    tactualcash.setText(getActualTotal() + "");
                    System.out.println("exception123:" + e);
                }
            }
        });

        eT100 = findViewById(R.id.N_one2zero);
        tV100 = findViewById(R.id.A_one2zero);

        eT100.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //You can also apply your logic here instead of afterTextChanged
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    S100 = 100 * Integer.parseInt(s.toString());
                    tV100.setText(S100 + "");
                    tactualcash.setText(getActualTotal() + "");
                } catch (NumberFormatException e) {
                    S100 = 0;
                    tV100.setText(S100 + "");
                    tactualcash.setText(getActualTotal() + "");
                    System.out.println("exception123:" + e);
                }


            }
        });

        eT50 = findViewById(R.id.N_fivezero);
        tV50 = findViewById(R.id.A_fivezero);

        eT50.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //You can also apply your logic here instead of afterTextChanged

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    S50 = 50 * Integer.parseInt(s.toString());
                    tV50.setText(S50 + "");
                    tactualcash.setText(getActualTotal() + "");
                } catch (NumberFormatException e) {
                    S50 = 0;
                    tV50.setText(S50 + "");
                    tactualcash.setText(getActualTotal() + "");
                    System.out.println("exception123:" + e);
                }
            }
        });

        eT20 = findViewById(R.id.N_twozero);
        tV20 = findViewById(R.id.A_twozero);

        eT20.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //You can also apply your logic here instead of afterTextChanged
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    S20 = 20 * Integer.parseInt(s.toString());
                    tV20.setText(S20 + "");
                    tactualcash.setText(getActualTotal() + "");
                } catch (NumberFormatException e) {
                    S20 = 0;
                    tV20.setText(S20 + "");
                    tactualcash.setText(getActualTotal() + "");
                    System.out.println("exception123:" + e);
                }
            }
        });

        eT10 = findViewById(R.id.N_onezero);
        tV10 = findViewById(R.id.A_onezero);

        eT10.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //You can also apply your logic here instead of afterTextChanged
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    S10 = 10 * Integer.parseInt(s.toString());
                    tV10.setText(S10 + "");
                    tactualcash.setText(getActualTotal() + "");
                } catch (NumberFormatException e) {
                    S10 = 0;
                    tV10.setText(S10 + " ");
                    tactualcash.setText(getActualTotal() + "");
                    System.out.println("exception123:" + e);
                }
            }
        });

        TextView Buttoncheck = findViewById(R.id.chequetotal);
        final TextView tmatch = findViewById(R.id.matchCash);
        final TextView tdiff = findViewById(R.id.diffCash);

        Buttoncheck.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int diffval= ActualTotal - AssumeTotal;
                if (matchOrNot()){
                    tmatch.setText("Matched");
                    tdiff.setText(diffval+"");}
                else{
                    tmatch.setText("NOT matched");
                    if(diffval>0)
                        tdiff.setText("+"+diffval+"");
                    else
                        tdiff.setText(diffval+"");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_menu, menu);
        return true;
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            String s_2000 = "2000 * "+eT2000.getText().toString()+" = "+tV2000.getText().toString();
            String s_500 = "500 * "+eT500.getText().toString()+" = "+tV500.getText().toString();
            String s_200 = "200 * "+eT200.getText().toString()+" = "+tV200.getText().toString();
            String s_100 = "100 * "+eT100.getText().toString()+" = "+tV100.getText().toString();
            String s_50 = "50 * "+eT50.getText().toString()+" = "+tV50.getText().toString();
            String s_20 = "20 * "+eT20.getText().toString()+" = "+tV20.getText().toString();
            String s_10 = "10 * "+eT10.getText().toString()+" = "+tV10.getText().toString();
            String s_1 = "1 * "+eT1.getText().toString()+" = "+tV1.getText().toString();
            String s_total = "Total : "+tactualcash.getText().toString();

            final String message = s_2000+"\n"+s_500+"\n"+s_200+"\n"+s_100+"\n"+s_50+"\n"+s_20+"\n"
                    +s_10+"\n"+s_1+"\n\n"+s_total;

            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Calculator.this);
            alertDialog.setTitle("Choose contact number");
            alertDialog.setMessage("Enter contact number on which you want to send the Message");

            final EditText input = new EditText(Calculator.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            alertDialog.setView(input);
            input.setText("9323610419");

            alertDialog.setPositiveButton("Send",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String phone = "+91"+input.getText().toString();
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
        return super.onOptionsItemSelected(item);
    }

}