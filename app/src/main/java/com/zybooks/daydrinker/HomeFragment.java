package com.zybooks.daydrinker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.zybooks.daydrinker.model.Day;
import com.zybooks.daydrinker.repo.DayRepository;

import java.text.SimpleDateFormat;
import java.util.Date;


public class HomeFragment extends Fragment {
    private EditText editTextNumberCups;
    private Button buttonSubmit;
    private int totalDrank;
    private int drank;
    private int amtDrank = 0;
    private WaterIntakeView waterIntakeView;
    private SettingsFragment dailyIntake;
    DayRepository dayRepo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dayRepo = DayRepository.getInstance(this.getContext());

        //inflates the layout
        View contentView = inflater.inflate(R.layout.activity_home, container, false);
        TextView goalTextView = contentView.findViewById(R.id.goal);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        String goalString = preferences.getString("daily_intake", "12");

        int goalValue;
        try {
            goalValue = Integer.parseInt(goalString);
        } catch (NumberFormatException e) {
            goalValue = 0;
        }

        String goalText = String.valueOf(goalValue);
        goalTextView.setText(goalText);

        return contentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());


        dayRepo = DayRepository.getInstance(this.getContext());


        WaterIntakeView waterIntakeView = view.findViewById(R.id.waterIntakeView);

        editTextNumberCups = view.findViewById(R.id.editTextNumberCups);
        buttonSubmit = view.findViewById(R.id.buttonSubmit);

        //default daily intake
        int defaultValue = 0;

        String dailyIntakeString = preferences.getString("daily_intake", "12");
        int drankGoal;
        if (dailyIntakeString != null) {
            drankGoal = Integer.parseInt(dailyIntakeString);
        } else {
            drankGoal = defaultValue;
        }

        //submit button listener
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int amtDrank = Integer.parseInt(editTextNumberCups.getText().toString());

                    //amount for this day in the repository
                    int oldDrank = dayRepo.getCurrentDay().getProgress();
                    totalDrank = amtDrank + oldDrank;

                    Day currentDay = dayRepo.getCurrentDay();
                    currentDay.setProgress(totalDrank);
                    //updates the current day in the repo

                    if (totalDrank >= drankGoal && oldDrank < drankGoal) {
                        int currentStreak = currentDay.getStreak();
                        currentStreak++;
                        currentDay.setStreak(currentStreak);
                    }
                    dayRepo.addDay(currentDay);
                    //updates the UI w/current intake
                    waterIntakeView.setCurrentIntake();
                } catch (NumberFormatException e) {
                    editTextNumberCups.setError("Please enter a valid number");
                }
            }
        });

        //formats the date
        SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy");
        String date = ft.format(new Date());

        //retrieves the date stored in the repo for the current day
        String latestDate = dayRepo.getCurrentDay().getDate();

        if (!latestDate.equals(date)) {
            updateDatabase(drankGoal);
        }
    }

    //called to update the database w a new entry for the current day
    public void updateDatabase(int dailyIntake) {

        Day newDay = new Day();

        //gets the current date
        SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy");
        String date = ft.format(new Date());

        //gets information about the current day from the repository
        Day currentDay = dayRepo.getCurrentDay();
        int dayId = currentDay.getDayId();
        int streak;

        //sets the attributes of the newDay object
        newDay.setDate(date);
        newDay.setDayId(dayId+1);
        if (currentDay.getProgress() >= currentDay.getGoal())
        {
            newDay.setStreak(currentDay.getStreak());
        }
        else {
            newDay.setStreak(0);
        }
        newDay.setProgress(0);
        newDay.setGoal(currentDay.getGoal());
        dayRepo.addDay(newDay);
    }

    public void setTodayGoal(int dailyIntake) {
            TextView goalTextView = getView().findViewById(R.id.goal);
            goalTextView.setText(String.valueOf(dailyIntake));
    }

}
