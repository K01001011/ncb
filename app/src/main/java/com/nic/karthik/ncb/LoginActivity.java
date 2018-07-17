package com.nic.karthik.ncb;

import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText username,password;
    Button Login,BRegister;
    FirebaseAuth Auth;
    FirebaseAuth.AuthStateListener Authlistener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Auth = FirebaseAuth.getInstance();
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        Login = findViewById(R.id.loginButton);
        BRegister = findViewById(R.id.register);

        BRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        Authlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null)
                {
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        };

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        username.setText("");
        password.setText("");
        Auth.addAuthStateListener(Authlistener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(Auth!=null)
        {
            Auth.removeAuthStateListener(Authlistener);
        }
    }

    private void signIn() {
        String email = username.getText().toString();
        String pass = password.getText().toString();
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass))
        {
            Toast.makeText(LoginActivity.this, "FIELDS CANNOT BE EMPTY", Toast.LENGTH_LONG).show();
        }
        else {
            Auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful())
                    {
                        Toast.makeText(LoginActivity.this, "PROBLEM SIGNING IN", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
