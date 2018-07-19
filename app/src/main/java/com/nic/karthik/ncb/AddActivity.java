package com.nic.karthik.ncb;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.UUID;

public class AddActivity extends AppCompatActivity {

    EditText textName, textAddress, textDesc;
    ImageButton photo;
    Button add;
    String name, address, desc;
    Bitmap image1;
    StorageReference storageReference;
    DatabaseReference database;
    FirebaseAuth auth;
    String UID, loc;
    LocationManager locationManager;
    final Location[] l = new Location[1];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        LocationListener locationListener;
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        textName = findViewById(R.id.editText);
        textAddress = findViewById(R.id.editText2);
        textDesc= findViewById(R.id.editText3);
        photo = findViewById(R.id.imageButton);
        database = FirebaseDatabase.getInstance().getReference().child("Data");
        storageReference = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        UID = auth.getCurrentUser().getUid();
        //image1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_settings_black); why is this lne htere?

        add = findViewById(R.id.add);

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String ID = UUID.randomUUID().toString();
                name = textName.getText().toString().trim();
                address = textAddress.getText().toString().trim();
                desc = textDesc.getText().toString().trim();
                double lt = l[0].getLatitude();
                double lg = l[0].getLongitude();
                loc = "Lattitude = " + Double.toString(lt) + "Longitude = " + Double.toString(lg);
                Intent i = getIntent();
                Bundle extras = i.getExtras();
                if (extras != null)
                    image1 = (Bitmap) extras.get("image");
                photo.setImageBitmap(image1);

                ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                image1.compress(Bitmap.CompressFormat.JPEG, 100, byteArray);
                final byte[] thumbByteArray = byteArray.toByteArray();

                StorageReference filePath = storageReference.child("PICS").child(ID + ".jpg");

                UploadTask uploadTask = filePath.putBytes(thumbByteArray);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        HashMap data = new HashMap<String, String>();
                        data.put("Name", name);
                        data.put("Address", address);
                        data.put("Description", desc);
                        data.put("ID", ID);
                        data.put("UID", UID);
                        data.put("URL", downloadUrl.toString());
                        database.child(ID).setValue(data);
                        Toast.makeText(getApplicationContext(), "CUSTOMER ADDED", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AddActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String []{Manifest.permission.ACCESS_FINE_LOCATION},1);
            return;
        } else {
           l[0] = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
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
                });
            }
        }
    }
}




