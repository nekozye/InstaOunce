package com.neko2mizu.instaounce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.neko2mizu.instaounce.secure.SimpleSecurity;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        if (ParseUser.getCurrentUser() != null)
        {
            //goMainActivity();
        }

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick login button ");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username, password);
            }

        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick login button ");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                signinUser(username, password);
            }

        });
    }

    private void signinUser(String username, String password) {
        Log.i(TAG, "Attempting to sign in new user " + username);

        ParseUser user = new ParseUser();

        user.setUsername(username);
        String hashedpw = SimpleSecurity.getPasswordHash(username,password);
        user.setPassword(hashedpw);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null)
                {
                    Log.e(TAG, "Signup error, username:"+username);
                    Log.e(TAG, "Signup error, password:"+password);
                    Log.e(TAG, "Issues with Signup",e);
                    return;
                }

                //loginUser(username,password);
                goMainActivity();
            }
        });
    }

    private void loginUser(String username, String password) {
        Log.i(TAG, "Attempting to login user "+ username);
        //TODO: main activity link using intents

        String hashedpw = SimpleSecurity.getPasswordHash(username,password);

        ParseUser.logInInBackground(username, hashedpw, new LogInCallback(){
            @Override
            public void done(ParseUser user, ParseException e) {


                if (e != null) {

                    //TODO: requires better login handler.
                    Log.e(TAG, "login error, username:"+username);
                    Log.e(TAG, "login error, password:"+password);
                    Log.e(TAG, "Issues with login",e);
                    return;
                }

                goMainActivity();
                }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}