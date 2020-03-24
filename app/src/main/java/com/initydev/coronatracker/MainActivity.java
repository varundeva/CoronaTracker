package com.initydev.coronatracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.initydev.coronatracker.Fragments.aboutFragment;
import com.initydev.coronatracker.Fragments.countryFragment;
import com.initydev.coronatracker.Fragments.homeFragment;
import com.onesignal.OneSignal;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private long mBackPressed;
    private static final int TIME_INTERVAL = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavView= findViewById(R.id.bottom_navigation);
        bottomNavView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        Fragment fragment = new homeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,fragment).commit();


        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.shareApk:
                ShareApk();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void ShareApk() {

        Intent shareintent = new Intent();

        shareintent.setAction(Intent.ACTION_SEND);

        shareintent.putExtra(Intent.EXTRA_TEXT,
                "Get the latest Update of Corona Virus Spread on \"Corona Tracker\" ! \n\n Download here :\n"
                        + "https://bit.ly/CoronaTrackerApp \n" + "Or \n"+"https://bit.ly/DownloadCoronaTrackerApp");
        shareintent.setType("text/plain");
        startActivity(Intent.createChooser(shareintent, "Share via"));
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment fragment = null;
                    switch (menuItem.getItemId()){
                        case R.id.home:
                            fragment = new homeFragment();
                            break;
                        case R.id.country:
                            fragment = new countryFragment();
                            break;
                        case R.id.about:
                            fragment = new aboutFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,fragment).commit();
                    return true;
                }
            };
    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            System.exit(1);
            return;
        } else {
            Toast.makeText(getBaseContext(), "Press two times to close app",    Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }
}
