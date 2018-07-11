package com.nic.karthik.ncb;

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
    String name, address, desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        textName = findViewById(R.id.editText);
        textAddress = findViewById(R.id.editText2);
        textDesc = findViewById(R.id.editText3);
        photo = findViewById(R.id.imageButton);

        add = findViewById(R.id.add);

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle photo event here
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = textName.getText().toString().trim();
                address = textAddress.getText().toString().trim();
                desc = textDesc.getText().toString().trim();
            }
        });
    }
}
