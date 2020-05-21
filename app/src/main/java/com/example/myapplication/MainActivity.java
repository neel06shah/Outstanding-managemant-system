package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnDEG, btnDEK, btnDEM,btnDES,btnDET, btnDETA, btnDWK, btnDWG, btnKEL, btnKEK, btnKWK, btnKWR, btnKWS, btnKWSC, btnOTH, btnTIT, btnIBALL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Choose your Area");

        btnDEG=findViewById(R.id.btnDEG);
        btnDEK=findViewById(R.id.btnDEK);
        btnDEM=findViewById(R.id.btnDEM);
        btnDES=findViewById(R.id.btnDES);
        btnDET=findViewById(R.id.btnDET);
        btnDETA=findViewById(R.id.btnDETA);
        btnDWK=findViewById(R.id.btnDWK);
        btnDWG=findViewById(R.id.btnDWG);
        btnKEL=findViewById(R.id.btnKEL);
        btnKEK=findViewById(R.id.btnKEK);
        btnKWK=findViewById(R.id.btnKWK);
        btnKWR=findViewById(R.id.btnKWR);
        btnKWS=findViewById(R.id.btnKWS);
        btnKWSC=findViewById(R.id.btnKWSC);
        btnOTH=findViewById(R.id.btnOTH);
        btnTIT=findViewById(R.id.btnTIT);
        btnIBALL=findViewById(R.id.btnIBALL);


        btnDEG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Party_list.class);
                i.putExtra("Area","DE - Ghandinagar");
                startActivity(i);
            }
        });
        btnDEM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String area = btnDEM.getText().toString();
                Intent i = new Intent(MainActivity.this, Party_list.class);
                i.putExtra("Area",area);
                startActivity(i);
            }
        });

        btnDEK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Party_list.class);
                i.putExtra("Area","DE -  Kasturiplaza");
                startActivity(i);
            }
        });

        btnDES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String area = btnDES.getText().toString();
                Intent i = new Intent(MainActivity.this, Party_list.class);
                i.putExtra("Area",area);
                startActivity(i);
            }
        });

        btnDET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String area = btnDET.getText().toString();
                Intent i = new Intent(MainActivity.this, Party_list.class);
                i.putExtra("Area",area);
                startActivity(i);
            }
        });

        btnDETA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String area = btnDETA.getText().toString();
                Intent i = new Intent(MainActivity.this, Party_list.class);
                i.putExtra("Area",area);
                startActivity(i);
            }
        });

        btnDWK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String area = btnDWK.getText().toString();
                Intent i = new Intent(MainActivity.this, Party_list.class);
                i.putExtra("Area",area);
                startActivity(i);
            }
        });

        btnDWG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String area = btnDWG.getText().toString();
                Intent i = new Intent(MainActivity.this, Party_list.class);
                i.putExtra("Area",area);
                startActivity(i);
            }
        });

        btnKEL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String area = btnKEL.getText().toString();
                Intent i = new Intent(MainActivity.this, Party_list.class);
                i.putExtra("Area",area);
                startActivity(i);
            }
        });

        btnKEK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String area = btnKEK.getText().toString();
                Intent i = new Intent(MainActivity.this, Party_list.class);
                i.putExtra("Area",area);
                startActivity(i);
            }
        });

        btnKWK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String area = btnKWK.getText().toString();
                Intent i = new Intent(MainActivity.this, Party_list.class);
                i.putExtra("Area",area);
                startActivity(i);
            }
        });

        btnKWR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String area = btnKWS.getText().toString();
                Intent i = new Intent(MainActivity.this, Party_list.class);
                i.putExtra("Area",area);
                startActivity(i);
            }
        });

        btnKWSC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String area = btnKWSC.getText().toString();
                Intent i = new Intent(MainActivity.this, Party_list.class);
                i.putExtra("Area",area);
                startActivity(i);
            }
        });

        btnOTH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String area = btnOTH.getText().toString();
                Intent i = new Intent(MainActivity.this, Party_list.class);
                i.putExtra("Area",area);
                startActivity(i);
            }
        });
        btnTIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String area = btnTIT.getText().toString();
                Intent i = new Intent(MainActivity.this, Party_list.class);
                i.putExtra("Area",area);
                startActivity(i);
            }
        });

        btnIBALL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String area = btnIBALL.getText().toString();
                Intent i = new Intent(MainActivity.this, Party_list.class);
                i.putExtra("Area",area);
                startActivity(i);
            }
        });



    }
}
