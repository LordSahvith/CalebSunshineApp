package com.example.android.sunshine.app;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // add 'general' preferences, defined in the XML file
        addPreferencesFromResource(R.xml.pref_general);

        // for all preferences, attach an OnPreferenceChangeListener so the UI summary can be
        // updated when the preference changes
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_location_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_units_key)));

        // Display the fragment as teh main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    /**
     * attaches a listener so the sumary is always updated with the preference value.
     * also fires the lstener once, to intialize the summary (so it shows up before the value
     * is changed.)
     */
    private void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(this);

        // Trigger the listener immediately with the preference's current value
        onPreferenceChange(preference, PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), ""));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        String stringValue = value.toString();

        System.out.println(preference.getKey() + " : " + stringValue);

        if (preference instanceof ListPreference) {
            // for list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have seperate labels/values).
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            // for other preferneces, set the summary to the value's simple string representation
            preference.setSummary(stringValue);
        }
        return true;
    }

}// end of SettingsActivity class
