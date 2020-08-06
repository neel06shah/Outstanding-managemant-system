package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class Calculator extends AppCompatActivity{

    int AssumeTotal, ActualTotal; //assumed total cash
    int S10, S20, S50, S100, S200, S500, S2000,S1; //for storing total value for number of each note


    int getActualTotal() {
        ActualTotal = S10 + S20 + S50 + S100 + S200 + S500 + S2000+S1;
        return ActualTotal;
    }

    boolean matchOrNot() {
        return ActualTotal == AssumeTotal ? true : false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        Intent i = getIntent();
        float cash = i.getFloatExtra("cash",0);

        EditText etotalCash = (EditText) findViewById(R.id.totalcash);
        etotalCash.setText(""+cash);

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

        final TextView tactualcash = (TextView) findViewById(R.id.actualCash);


        final EditText eT2000 = (EditText) findViewById(R.id.N_two3zero);

        final TextView tV2000 = (TextView) findViewById(R.id.A_two3zero);

        eT2000.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //You can also apply your logic here instead of afterTextChanged

            }

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


        final EditText eT500 = (EditText) findViewById(R.id.N_five2zero);

        final TextView tV500 = (TextView) findViewById(R.id.A_five2zero);

        eT500.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //You can also apply your logic here instead of afterTextChanged

            }

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
        final EditText eT200 = (EditText) findViewById(R.id.N_two2zero);

        final TextView tV200 = (TextView) findViewById(R.id.A_two2zero);

        eT200.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //You can also apply your logic here instead of afterTextChanged

            }

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


        final EditText eT1 = (EditText) findViewById(R.id.N_one);

        final TextView tV1 = (TextView) findViewById(R.id.A_one);

        eT1.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //You can also apply your logic here instead of afterTextChanged

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    S1 = 1 * Integer.parseInt(s.toString());
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



        final EditText eT100 = (EditText) findViewById(R.id.N_one2zero);

        final TextView tV100 = (TextView) findViewById(R.id.A_one2zero);

        eT100.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //You can also apply your logic here instead of afterTextChanged

            }

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

        final EditText eT50 = (EditText) findViewById(R.id.N_fivezero);

        final TextView tV50 = (TextView) findViewById(R.id.A_fivezero);

        eT50.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //You can also apply your logic here instead of afterTextChanged

            }

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

        final EditText eT20 = (EditText) findViewById(R.id.N_twozero);

        final TextView tV20 = (TextView) findViewById(R.id.A_twozero);

        eT20.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //You can also apply your logic here instead of afterTextChanged

            }

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
        final EditText eT10 = (EditText) findViewById(R.id.N_onezero);

        final TextView tV10 = (TextView) findViewById(R.id.A_onezero);

        eT10.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //You can also apply your logic here instead of afterTextChanged

            }

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

        TextView Buttoncheck = (TextView) findViewById(R.id.chequetotal);
        final TextView tmatch = (TextView) findViewById(R.id.matchCash);
        final TextView tdiff = (TextView) findViewById(R.id.diffCash);

        Buttoncheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int diffval= ActualTotal - AssumeTotal;
                if (matchOrNot() == true){
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

}