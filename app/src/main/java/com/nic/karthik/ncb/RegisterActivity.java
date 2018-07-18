package com.nic.karthik.ncb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    EditText username,password, privateKey;
    Button Register;
    private FirebaseAuth mAuth;
    String email,pass, key;
    private ProgressDialog progressDialog;
    DatabaseReference database,f_data;
    int x=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        privateKey = findViewById(R.id.privateKey);
        Register = findViewById(R.id.registerButton);
        database = FirebaseDatabase.getInstance().getReference("Key");
        f_data = FirebaseDatabase.getInstance().getReference("Key");

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = username.getText().toString().trim();
                pass = password.getText().toString().trim();
                key = privateKey.getText().toString().trim();
                if (email.isEmpty() || pass.isEmpty()) {
                    x = 3;
                }
                if (pass.length() < 6)
                    x = 5;
                checkValue(x);
            }
        });
    }

    private void createAccount(String email, String password)
    {
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if(user!=null) {
                                String currentUser = mAuth.getCurrentUser().getUid();
                                //f_users.child(currentUser).setValue(currentUser);
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "AUTHENTICATION FAILED",
                                    Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    public void checkValue(int x)
    {
        if(x==0)
        {
            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String dataKey = dataSnapshot.getValue(String.class);
                    if(key.equals(dataKey))
                    {
                        String newKey = UUID.randomUUID().toString().substring(0, 10);
                        Log.d("inside", "onDataChange: " + newKey);
                        f_data.setValue(newKey);
                        progressDialog.setMessage("PLEASE WAIT");
                        progressDialog.show();
                        createAccount(email, pass);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else if(x==3)
        {
            Toast.makeText(RegisterActivity.this, "FIELDS CANNOT BE EMPTY",
                    Toast.LENGTH_SHORT).show();
        }
        else if(x==5)
        {
            Toast.makeText(RegisterActivity.this, "PASSWORD MUST BE MINIMUM 6 CHARACTERS",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        username.setText("");
        password.setText("");
    }
}
