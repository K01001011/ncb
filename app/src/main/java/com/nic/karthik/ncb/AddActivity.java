package com.nic.karthik.ncb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        textName = findViewById(R.id.editText);
        textAddress = findViewById(R.id.editText2);
        textDesc = findViewById(R.id.editText3);
        photo = findViewById(R.id.imageButton);
        database = FirebaseDatabase.getInstance().getReference().child("Data");
        storageReference = FirebaseStorage.getInstance().getReference();

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
                Intent i = getIntent();
                Bundle extras = i.getExtras();
                assert extras != null;
                image1 = (Bitmap) extras.get("data");
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
                        data.put("URL", downloadUrl.toString());
                        database.child(ID).setValue(data);
                    }
                });
            }
        });
    }
}
