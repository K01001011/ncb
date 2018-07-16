            package com.example.vijay.myapplication;

            import android.Manifest;
            import android.app.ProgressDialog;
            import android.content.Context;
            import android.content.Intent;
            import android.content.pm.PackageManager;
            import android.graphics.Bitmap;
            import android.graphics.Camera;
            import android.net.Uri;
            import android.os.Build;
            import android.os.Environment;
            import android.provider.MediaStore;
            import android.support.annotation.NonNull;
            import android.support.annotation.Nullable;
            import android.support.v7.app.AppCompatActivity;
            import android.os.Bundle;
            import android.view.View;
            import android.widget.Button;
            import android.widget.ImageView;
            import android.widget.Toast;

            import com.google.firebase.database.DatabaseReference;
            import com.google.firebase.database.FirebaseDatabase;
            import com.google.firebase.storage.FirebaseStorage;
            import com.google.firebase.storage.StorageReference;

            import java.io.File;
            import java.io.IOException;
            import java.net.URI;
            import java.text.SimpleDateFormat;
            import java.util.ArrayList;
            import java.util.Date;

            public class Main2Activity extends AppCompatActivity {
                Context c;
                Uri imageuri;
                ImageView imageview;
                String imageloc;
                Camera letstrythis;
                @Override
                protected void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.activity_main2);
                    Button Takepic = findViewById(R.id.next);
                    Button FromGallery = findViewById(R.id.upload);
                    Button return1 = findViewById(R.id.previous);
                    imageview = findViewById(R.id.imageView);
                    checkFilePremisssion();
                    checkcameraPremisssion();
                    Takepic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent,1);

                        }
                    });
                    FromGallery.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            opengallery();
                        }
                    });
                    return1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Main2Activity.this,Main3Activity.class);
                            startActivityForResult(intent,3);
                        }
                    });

                }
                private void checkcameraPremisssion() {
                    if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M){
                        int per = this.checkSelfPermission(Manifest.permission.CAMERA);
                        if(per!=0){
                            this.requestPermissions(new String[]{Manifest.permission.CAMERA},1);
                        }
                    }
                }
                @Override

                public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

                    if (requestCode == 1) {

                        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                            Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();

                        } else {

                            Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();

                        }

                    }}



                private void opengallery() {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(intent,100);
                }




                private void checkFilePremisssion() {
                    if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M){
                        int per = this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)+this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if(per!=0){
                         this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
                        }
                    }

                }

                @Override
                protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                    super.onActivityResult(requestCode, resultCode, data);
                     if(resultCode==RESULT_OK)
                     {
                         if(requestCode==1)
                         {
                             Bundle extras = data.getExtras();
                             assert extras != null;
                             Bitmap image = (Bitmap) extras.get("data");
                             ImageView imageview = (ImageView) findViewById(R.id.imageView); //sets imageview as the bitmap
                             imageview.setImageBitmap(image);
                         }
                         if((requestCode== 0b1100100))
                         {
                             imageuri =data.getData();
                             imageview.setImageURI(imageuri);


                         }
                         if(requestCode == 3){

                         }
                    }
                }
            }
