package com.debasish.guitardhun.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.debasish.guitardhun.R;

public class HomeScreen extends AppCompatActivity {

    private TextView mTextMessage;
    ActionBar ab;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    ab.setTitle("Home");
                    return true;
                case R.id.navigation_dashboard:
                    ab.setTitle("DashBoard");
                    return true;
                case R.id.navigation_notifications:
                    ab.setTitle("Profile");
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //Initializing the action bar and setting the title
        ab = getSupportActionBar();
        ab.setTitle(R.string.title_home);

        BottomNavigationView navigation =
                (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
