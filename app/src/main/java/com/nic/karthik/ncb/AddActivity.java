package com.nic.karthik.ncb;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class AddActivity extends AppCompatActivity {

    EditText textName, textAddress, textDesc;
    ImageButton photo;
    Button add;
    String name, address, desc, Loc;
    Bitmap image1;
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        textName = findViewById(R.id.editText);
        textAddress = findViewById(R.id.editText2);
        textDesc = findViewById(R.id.editText3);
        photo = findViewById(R.id.imageButton);
        final Location[] l = new Location[1];
        add = findViewById(R.id.add);
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                l[0] = location;
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                return;
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = textName.getText().toString().trim();
                address = textAddress.getText().toString().trim();
                desc = textDesc.getText().toString().trim();
                Intent i = getIntent();
                Bundle extras = i.getExtras();
                assert extras != null;
                image1 = (Bitmap) extras.get("data");
                photo.setImageBitmap(image1);
                double lat = l[0].getLatitude();
                double lon = l[0].getLongitude();
                Loc = "Latitude = " + Double.toString(lat) + "Location = " + Double.toString(lon);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }
}
