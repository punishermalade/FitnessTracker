package com.punisher.fitnesstracker.fragment;


import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.widget.Toolbar;

import com.punisher.fitnesstracker.R;

/**
 * Created by punisher on 2016-04-18.
 */
public class SettingsFragement extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        addPreferencesFromResource(R.xml.preferences);
    }
}
