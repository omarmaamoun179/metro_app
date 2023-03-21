package com.example.metroapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DataActivity extends AppCompatActivity {
    TextView datacode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        datacode = findViewById(R.id.datacode);



        Intent intent = getIntent();
        String route =  intent.getStringExtra("route");
        String direction = intent.getStringExtra("direction");
        int time =  intent.getIntExtra("time", -1);
        int price =  intent.getIntExtra("price",-1);
        int count =  intent.getIntExtra("count",-1);






        datacode.append("-The Price :  "+price + "L.E");
        datacode.append("\n-Number of Stations : "+count + " Stations.");
        datacode.append("\n" +direction+".");
        datacode.append("\n-The Time : "+time + "minutes"+".");
        datacode.append("\n-Your Route : \n"+route);






    }
}