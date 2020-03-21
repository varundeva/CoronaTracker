package com.initydev.coronatracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.onesignal.OneSignal;

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
                            Toast.makeText(MainActivity.this, "Working On..!", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.about:
                            fragment = new aboutFragment();
                            Toast.makeText(MainActivity.this, "Working On..!", Toast.LENGTH_SHORT).show();
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
