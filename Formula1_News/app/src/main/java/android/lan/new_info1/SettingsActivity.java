package android.lan.new_info1;

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

    public static class NewsPreferenceFragment extends PreferenceFragment implements Preference
            .OnPreferenceChangeListener {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            //this sets the minimum date for the search query
            Preference minDate = findPreference(getString(R.string.settings_earliest_date_key));
            bindPreferenceSummaryToValue(minDate);

            //this sets the orderby date for the date query
            Preference orderBy = findPreference(getString(R.string.settings_orderby_key));
            bindPreferenceSummaryToValue(orderBy);

            //this sets the maximum date for the search query
            Preference maxDate = findPreference(getString(R.string.settings_max_date_key));
            bindPreferenceSummaryToValue(maxDate);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();
            preference.setSummary(stringValue);

            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            }
            return true;
        }

        private void bindPreferenceSummaryToValue(Preference preference) {

            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }
    }
}