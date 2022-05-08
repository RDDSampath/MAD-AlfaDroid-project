package com.dhanushka.timetable.fragments;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.dhanushka.timetable.R;


public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
    }
}
