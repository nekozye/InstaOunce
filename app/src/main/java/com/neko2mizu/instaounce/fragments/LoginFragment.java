package com.neko2mizu.instaounce.fragments;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.neko2mizu.instaounce.MainActivity;
import com.neko2mizu.instaounce.R;
import com.neko2mizu.instaounce.objects.Post;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnRegister;


    public LoginFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceStale){
        return inflater.inflate(R.layout.fragment_login, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (ParseUser.getCurrentUser() != null && ParseUser.getCurrentUser().isAuthenticated())
        {
            startIfCorrectSession();
        }

        etUsername = view.findViewById(R.id.etUsername);
        etPassword = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
        btnRegister = view.findViewById(R.id.btnRegister);

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

    private void startIfCorrectSession()
    {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null){
                    if(e.getCode() == ParseException.INVALID_SESSION_TOKEN)
                    {
                        ParseUser.getCurrentUser().logOut();
                    }
                    else
                    {
                        Log.e(TAG,"Error that is not handled:",e);
                    }
                    return;
                }
                goMainActivity();
            }
        });
    }

    private void signinUser(String username, String password) {
        Log.i(TAG, "Attempting to sign in new user " + username);

        if (ParseUser.getCurrentUser() != null)
        {
            //Bugfix of failure of valid session.
            ParseUser.getCurrentUser().logOut();
        }

        ParseUser user = new ParseUser();

        user.setUsername(username);
        user.setPassword(password);

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

                Toast.makeText(getContext(), "Sign in successful", Toast.LENGTH_SHORT).show();
                goMainActivity();
            }
        });
    }

    private void loginUser(String username, String password) {
        Log.i(TAG, "Attempting to login user "+ username);
        //TODO: main activity link using intents

        if (ParseUser.getCurrentUser() != null)
        {
            //Bugfix of failure of valid session.
            ParseUser.getCurrentUser().logOut();
        }

        ParseUser.logInInBackground(username, password, new LogInCallback(){
            @Override
            public void done(ParseUser user, ParseException e) {


                if (e != null) {

                    //TODO: requires better login handler.
                    Log.e(TAG, "login error, username:"+username);
                    Log.e(TAG, "login error, password:"+password);
                    Log.e(TAG, "Issues with login",e);

                    return;
                }

                Toast.makeText(getContext(), "Log in successful", Toast.LENGTH_SHORT).show();
                goMainActivity();
                }
        });
    }

    private void goMainActivity() {
        ((MainActivity)(getActivity())).swapToLoggedInMain();
    }
}