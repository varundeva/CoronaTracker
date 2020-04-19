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
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.initydev.coronatracker.Fragments.aboutFragment;
import com.initydev.coronatracker.Fragments.countryFragment;
import com.initydev.coronatracker.Fragments.homeFragment;
import com.initydev.coronatracker.Fragments.statesFragment;
import com.onesignal.OneSignal;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private long mBackPressed;
    private static final int TIME_INTERVAL = 2000;
    ArrayList<String> quoteList = new ArrayList<>();
    Integer i = 0;
    Thread thread;
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

        FloatingActionButton fab = findViewById(R.id.bug);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Give Suggestions or Report Bugs", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String subject = "Mail From Corona Tracker";
                Uri data = Uri.parse("mailto:world4us.in@gmail.com?subject=" + subject);
                intent.setData(data);
                startActivity(intent);
            }
        });


        TextView quotetext = findViewById(R.id.quoteText);

        quoteList.add("Lockdown means LOCKDOWN! Avoid going out unless absolutely necessary. Stay safe!  ");
        quoteList.add("Don't Hoard groceries and essentials. Please ensure that people who are in need don't face a shortage because of you!");
        quoteList.add("Plan ahead! Take a minute and check how much you have at home. Planning ahead let's you buy exactly what you need!");
        quoteList.add("If you have symptoms and suspect you have coronavirus - reach out to your doctor or call state helplines. \uD83D\uDCDE Get help.");
        quoteList.add("Panic mode : OFF! ❌ ESSENTIALS ARE ON! ✔️");
        quoteList.add("Help out the elderly by bringing them their groceries and other essentials.");
        quoteList.add("Be considerate : While buying essentials remember : You need to share with 130 Crore Others!  ");
        quoteList.add("Stand Against FAKE News and WhatsApp Forwards! Do NOT ❌ forward a message until you verify the content it contains.");
        quoteList.add("Be compassionate! Help those in need like the elderly and poor. They are facing a crisis you cannot even imagine!  ");
        quoteList.add("If you have any queries, reach out to your district administration or doctors!  ");
        quoteList.add("The hot weather will not stop the virus! You can! Stay home, stay safe. ");
        quoteList.add("Avoid going out during the lockdown. Help break the chain of spread.");
        quoteList.add("Help the medical fraternity by staying at home!");
        quoteList.add("Plan and calculate your essential needs for the next three weeks and get only what is bare minimum needed.");
        quoteList.add("Call up your loved ones during the lockdown, support each other through these times.");
        quoteList.add("Our brothers from the north east are just as Indian as you! Help everyone during this crisis ❤");

        thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!thread.isInterrupted()) {
                        Thread.sleep(5000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (i < quoteList.size()) {
                                    quotetext.setText(quoteList.get(i));
                                    i++;
                                } else {
                                    i = 0;
                                }
                            }
                        });
                    }
                } catch (InterruptedException ignored) {
                }
            }
        };
        thread.start();
        
        
        
        
        

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

        String msg = "❗ Get latest Update of Corona Virus in \"Corona Tracker\uD83C\uDF0D\uD83E\uDDA0\" !\n" +
                "\n" +
                "➡️Corona Tracker App for Android\n" +
                "➡️App Contains Global Statistics, Country-wise,State-wise Statistics and District-wise Statistics\n" +
                "➡️Lightweight App (Internet Required)\n" +
                "\n" +
                "FAQ\n" +
                "1️⃣ Why App is Not in Playstore?\n" +
                "▶️ Playstore is not Allowing to Upload apps related to Covid19, Coronavirus Due to some Security Reason\n" +
                "2️⃣ Which is the Data Source?\n" +
                "▶️ All the Data is Taken from Govt Websites and Worldometer Website\n" +
                "3️⃣ What are the Permission Required to Use this App?\n" +
                "▶️ Just Internet Permission is Enough to Use\n" +
                "\n" +
                "How to Install App?\n" +
                "\uD83E\uDDA0App is Uploaded in Google Drive.\n" +
                "➖ Download the App from Given Link Belo\n" +
                "➖ Allow 3rd Party App Install Permission on your phone (Pop's up usually)\n" +
                "➖ Click on APK and Install. If it asks Package Installer when you click on link then select that\n" +
                "\n" +
                "‼️ Download here ‼️\n" +
                "Drive Link -https://bit.ly/CoronaTrackerApp \n" +
                "\n" +
                "Direct Download -https://bit.ly/DownloadCoronaTrackerApp\n" +
                "\n" +
                "Mirror Link - http://tiny.cc/CoronaTracker\n" +
                "\n" +
                "\uD83D\uDCF1App by Student Developer";

        Intent shareintent = new Intent();

        shareintent.setAction(Intent.ACTION_SEND);

        shareintent.putExtra(Intent.EXTRA_TEXT,
                msg);
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
                        case R.id.states:
                            fragment = new statesFragment();
                            break;
                        case R.id.about:
                            fragment = new aboutFragment();
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + menuItem.getItemId());
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
