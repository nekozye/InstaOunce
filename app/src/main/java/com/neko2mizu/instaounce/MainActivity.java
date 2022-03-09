package com.neko2mizu.instaounce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.neko2mizu.instaounce.fragments.ComposePostFragment;
import com.neko2mizu.instaounce.fragments.HomePostFragment;
import com.neko2mizu.instaounce.fragments.LoginFragment;
import com.neko2mizu.instaounce.fragments.ProfileFragment;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";


    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigationView;

    final Fragment fragment_compose = new ComposePostFragment();
    final Fragment fragment_home = new HomePostFragment();
    final Fragment fragment_profile = new ProfileFragment();

    final Fragment fragment_login = new LoginFragment();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.instagram_action_bar);


        bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_bar_compose:
                        fragment = fragment_compose;
                        break;
                    case R.id.action_bar_home:
                        fragment = fragment_home;
                        break;
                    case R.id.action_bar_profile:
                    default:
                        fragment = fragment_profile;
                        break;
                }

                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });





        if(ParseUser.getCurrentUser() != null)
        {
            //there is a user. proceed as normal.
            getSupportActionBar().show();
            bottomNavigationView.setVisibility(View.VISIBLE);
            fragmentManager.beginTransaction().replace(R.id.flContainer, fragment_compose).commit();
            bottomNavigationView.setSelectedItemId(R.id.action_bar_home);
        }
        else
        {
            //no user detected. need to have a login fragment on.
            bottomNavigationView.setVisibility(View.GONE);
            fragmentManager.beginTransaction().replace(R.id.flContainer, fragment_login).commit();
        }


    }


    public void swapToLoggedInMain() {
        getSupportActionBar().show();
        bottomNavigationView.setVisibility(View.VISIBLE);
        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment_compose).commit();
        bottomNavigationView.setSelectedItemId(R.id.action_bar_home);
    }
}