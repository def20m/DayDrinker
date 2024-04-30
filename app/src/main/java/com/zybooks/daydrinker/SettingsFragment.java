package com.zybooks.daydrinker;

import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.zybooks.daydrinker.model.Day;
import com.zybooks.daydrinker.repo.DayRepository;

public class SettingsFragment extends PreferenceFragmentCompat {

    DayRepository dayRepo;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        dayRepo = DayRepository.getInstance(this.getContext());

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
                        Day currentDay = dayRepo.getCurrentDay();
                        if(currentDay.getProgress() == 0 || newDailyIntake > currentDay.getGoal()) {
                            currentDay.setGoal(newDailyIntake);
                            dayRepo.addDay(currentDay);
                        }
                        else{
                            Toast.makeText(getActivity(), "Goal cannot be lowered once progress for the day has been entered.", Toast.LENGTH_SHORT).show();
                            return false;
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