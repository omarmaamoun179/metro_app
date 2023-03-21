package com.example.metroapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.text.CollationElementIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mumayank.com.airlocationlibrary.AirLocation;

public class MainActivity extends AppCompatActivity implements AirLocation.Callback {
    Spinner spinner1, spinner2;
    Button CalculateButton , reverse;
    ArrayList<String> items = new ArrayList<>();
    String start, arrival , direction;
    List<String> route=new ArrayList<>();
    int price , time , index , index1;
    SharedPreferences pref;
    double lat ,lon;
    ArrayList<Double> latitude = new ArrayList<>();
    ArrayList<Double> longitude = new ArrayList<>();
    ArrayList<Double> distance = new ArrayList<>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        CalculateButton = findViewById(R.id.CalculateButton);
        reverse = findViewById(R.id.reverse);
        pref = getPreferences(MODE_PRIVATE);






//spinner1
        Collections.addAll(items, "please select",
                "Helwan", "Ain-Helwan", "Helwan-University", "Wadi-Hof",
                "Hadayek-Helwan", "El-Maasara", "Tora-ElAsmant", "Kozzika", "Tora-El-Balad", "Sakanat-El-Maadi",
                "Maadi", "Hadayek-ElMaadi", "Dar-El-Salam", "El-Zahraa", "Mar-Girgis", "El-Malek-El-Saleh",
                "Al-Sayeda-Zeinab", "Saad-Zaghloul", "Sadat", "Nasser", "Orabi", "Al-Shohadaa", "Ghamra",
                "El-Demerdash", "Manshiet-El-Sadr", "Kobri-El-Qobba", "Hammamat El-Qobba", "Saray-El-Qobba",
                "Hadayeq-El-Zaitoun", "Helmeyet-El-Zaitoun", "El-Matareyya", "Ain-Shams", "Ezbet-El-Nakhl",
                "El-Marg", "New-El-Marg");




        Collections.addAll(latitude,29.8442,29.8579,29.8643,29.8747,29.8924,29.9014,29.9212,29.9314,29.9417,
                 29.9481,29.9552,29.9653,29.9772,29.9906,30.0012,30.0122,30.0245,30.0319,30.0397);

        Collections.addAll(longitude,31.3344,31.3249,31.3203,31.3134,31.304,31.2997,31.2877,31.2817,31.2736,
                31.2635,31.258,31.2506,31.2422,31.2316,31.2295,31.231,31.2353,31.2381,31.2357);










        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);


    }

    @Override
    public void onBackPressed() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("spinner1", String.valueOf(spinner1));
        editor.putString("spinner2", String.valueOf(spinner2));
        spinner1.getSelectedItemPosition();
        spinner2.getSelectedItemPosition();
        editor.apply();
        super.onBackPressed();
    }

    public void clac(View view) {
        start = spinner1.getSelectedItem().toString();
        arrival = spinner2.getSelectedItem().toString();

        if(start.equals("please select")|| arrival.equals("please select")){
            Toast.makeText(this, "please answer", Toast.LENGTH_SHORT).show();
            return;
        }
         index = items.indexOf(start);
         index1 = items.indexOf(arrival);
        int count = index1 - index;
        if (count == 0 ){
            Toast.makeText(this, "you are in the same station", Toast.LENGTH_SHORT).show();
            return;
        }

        if(count<0){
            count=count*(-1);
            route=items.subList(index1,index+1);
            Collections.reverse(route);
            direction = "-Your Direction : Helwan ";

        }
        else {


            route=items.subList(index,index1+1);
                direction = "-Your Direction : Elmarg";


        }
//Time
        time = count * 2 ;
//Price


        if(count<8)
        {

             price =5;
        } else if (count<16)
        {

            price = 7;
        }
        else
            price = 10 ;

    //Naviagtion
        Intent in=new Intent(this,DataActivity.class);
        in.putExtra("route", String.valueOf(route));
        in.putExtra("direction",direction);
        in.putExtra("time",time);
        in.putExtra("price",price);
        in.putExtra("count",count);


        startActivity(in);
    }


    public void reverse(View view) {


        start = spinner1.getSelectedItem().toString();
        arrival = spinner2.getSelectedItem().toString();

        int index = items.indexOf(start);
        int index1 = items.indexOf(arrival);

        if(start.equals("please select")|| arrival.equals("please select")) {
            Toast.makeText(this, "please answer", Toast.LENGTH_SHORT).show();
            return;
        }
        spinner2.setSelection(index);
        spinner1.setSelection(index1);


    }


    public void startmap(View view) {
        if(spinner1.getSelectedItem().toString().equals("please select")) {
            Toast.makeText(this, "please answer", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="+spinner1.getSelectedItem().toString()+" metro+station+egypt"));
        startActivity(in);

    }

    public void arrivalmap(View view) {
        if(spinner2.getSelectedItem().toString().equals("please select")) {
            Toast.makeText(this, "please answer", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="+spinner2.getSelectedItem().toString()+" metro+station+egypt"));
        startActivity(in);

    }

    public void nearst(View view) {
        AirLocation airlocation = new AirLocation(this,this,true,0,"");

        airlocation.start();



    }

    @Override
    public void onFailure(AirLocation.LocationFailedEnum locationFailedEnum) {
        Toast.makeText(this, "Location Error ",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onSuccess(ArrayList<Location> arrayList) {
        lat = arrayList.get(0).getLatitude();
        lon =arrayList.get(0).getLongitude();
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addreslist = geocoder.getFromLocation(lat , lon , 1);
            String line = addreslist.get(0).getAddressLine(0);


            for (int i = 0; i < latitude.size(); i++)
            {
                for (int j = 0; j < longitude.size(); j++) {



                    Location loc1 = new Location("");
                    loc1.setLatitude(lat);
                    loc1.setLongitude(lon);

                    Location loc2 = new Location("");
                    loc2.setLatitude(latitude.get(0));
                    loc2.setLongitude(longitude.get(0));



                   double distance2 =loc1.distanceTo(loc2);

                    distance.add( distance2);
                    Collections.min(distance);

                    Toast.makeText(this,""+ Collections.min(distance),Toast.LENGTH_LONG).show();






                }

            }
        }
        catch (IOException e) {
            Toast.makeText(this, "connection Error ",Toast.LENGTH_LONG).show();

        }


    }
}


