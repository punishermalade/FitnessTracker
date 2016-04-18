package com.punisher.fitnesstracker.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import com.punisher.fitnesstracker.R;

/**
 * represents a SettingsFragment that can be used in any other activities. This method follows the
 * Android development guidelines
 */
public class SettingsFragement extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        addPreferencesFromResource(R.xml.preferences);
    }
}
