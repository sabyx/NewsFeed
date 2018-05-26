package com.example.sabina.newsfeed;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public static class NewsFeedPreferenceFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            Preference q = findPreference(getString(R.string.settings_q_key));
            bindPreferenceSummaryToValue(q);

            Preference orderBy = findPreference(getString(R.string.settings_order_by_key));
            bindPreferenceSummaryToValue(orderBy);

            Preference apiKey = findPreference(getString(R.string.settings_apikey_key));
            bindPreferenceSummaryToValue(apiKey);

            Preference type = findPreference(getString(R.string.settings_type_key));
            bindPreferenceSummaryToValue(type);
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferencesString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferencesString);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String valueString = newValue.toString();
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(valueString);
                if (index >= 0) {
                    CharSequence[] entries = listPreference.getEntries();
                    preference.setSummary(entries[index]);
                }
            } else {
                preference.setSummary(valueString);
            }
            return true;
        }
    }
}
