package com.initydev.coronatracker.Fragments;

import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.initydev.coronatracker.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;


public class aboutFragment extends Fragment {
    String versionName;
    public aboutFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Set Title of Activity
        getActivity().setTitle("Corona Tracker - About");
        try {
            versionName = getContext().getPackageManager()
                    .getPackageInfo(getContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String ver = "Version "+versionName;
        String about = "A simple Android application which will help you track the latest spread of corona virus and other useful features.\n" +
                "Corona Tracker App Uses API's Available in the GITHUB. \n Connect Us through GITHUB for More \n\n"+ver;


        return new AboutPage(getContext())
                .isRTL(false)
                .setDescription(about)
                .setImage(R.drawable.corona)
                .addGroup("Connect with us")
                .addEmail("world4us.in@gmail.com", "Corona Tracker")
                .addGitHub("varundeva/CoronaTracker")
                .addGroup("Check Out")
                .addWebsite("https://covindia.herokuapp.com/", "Corona Tracker Web App")
                .addWebsite("https://covindia.herokuapp.com/policy", "Privacy and Policy")

                .create();


    }

}
