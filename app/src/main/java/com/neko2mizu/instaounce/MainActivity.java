package com.neko2mizu.instaounce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.neko2mizu.instaounce.fragments.ComposePostFragment;
import com.neko2mizu.instaounce.fragments.HomePostFragment;
import com.neko2mizu.instaounce.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";


    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigationView;

    final Fragment fragment_compose = new ComposePostFragment();
    final Fragment fragment_home = new HomePostFragment();
    final Fragment fragment_profile = new ProfileFragment();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        bottomNavigationView.setSelectedItemId(R.id.action_bar_home);




    }


}