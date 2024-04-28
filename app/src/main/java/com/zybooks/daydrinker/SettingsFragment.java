package com.zybooks.daydrinker;

import static java.lang.Integer.parseInt;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.zybooks.daydrinker.repo.DayRepository;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        EditTextPreference dailyIntake = (EditTextPreference)
                getPreferenceScreen().findPreference("daily_intake");
        dailyIntake.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                try {
                    int newDailyIntake = Integer.parseInt(newValue.toString());
                    if(newDailyIntake < 50 && newDailyIntake > 1){
                        HomeFragment homeFragment = (HomeFragment) getParentFragmentManager().findFragmentById(R.id.labelGoal);
                        if (homeFragment != null) {
                            homeFragment.setTodayGoal(newDailyIntake);
                        }
                        return true;
                    }else{
                        Toast.makeText(getActivity(), "Invalid Input. Please enter a whole number between 1 and 50.", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
                catch(Exception e){
                    Toast.makeText(getActivity(), "Invalid Input. Please enter a whole number between 1 and 50.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        });
    }


}