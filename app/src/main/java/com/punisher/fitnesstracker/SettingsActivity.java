package com.punisher.fitnesstracker;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;

import com.punisher.fitnesstracker.fragment.SettingsFragement;

/**
 * Created by punisher on 2016-04-18.
 */
public class SettingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(
                android.R.id.content,
                new SettingsFragement()).commit();

    }

}
