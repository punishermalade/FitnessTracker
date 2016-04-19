package com.punisher.fitnesstracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.punisher.fitnesstracker.fragment.SettingsFragement;

/**
 * encapsulates an Activity to display a list of preference. This activity uses a Fragment to build
 * and display the list as recommended by the Android Development Guidelines
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(
                android.R.id.content,
                new SettingsFragement()).commit();
    }
}