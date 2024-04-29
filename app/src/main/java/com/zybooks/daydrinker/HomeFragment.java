package com.zybooks.daydrinker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class HomeFragment extends Fragment {
    private EditText editTextNumberCups;
    private Button buttonSubmit;
    private int totalDrank;
    private int drank;
    private int amtDrank = 0;
    Calendar calendar = Calendar.getInstance();
    private WaterIntakeView waterIntakeView;
    private SettingsFragment dailyIntake;
    DayRepository dayRepo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dayRepo = DayRepository.getInstance(this.getContext());

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

        Log.d("HomeFragment", "Fragment is being created");
        dayRepo = DayRepository.getInstance(this.getContext());

        calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTimeInMillis(System.currentTimeMillis());

        WaterIntakeView waterIntakeView = view.findViewById(R.id.waterIntakeView);
        editTextNumberCups = view.findViewById(R.id.editTextNumberCups);
        buttonSubmit = view.findViewById(R.id.buttonSubmit);

        int defaultValue = 0;
        String dailyIntakeString = preferences.getString("daily_intake", null);
        int dailyIntake;
        if (dailyIntakeString != null) {
            dailyIntake = Integer.parseInt(dailyIntakeString);
        } else {
            dailyIntake = defaultValue;
        }

        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        Log.d("HomeFragment", "onCreateView: dayOfMonth: " + dayOfMonth);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.d("total water drank:", String.valueOf(totalDrank));
                    int amtDrank = Integer.parseInt(editTextNumberCups.getText().toString());
                    totalDrank = amtDrank + totalDrank;
                    waterIntakeView.setCurrentIntake(totalDrank);
                } catch (NumberFormatException e) {
                    editTextNumberCups.setError("Please enter a valid number");
                }
            }
        });

        SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy");
        String date = ft.format(new Date());

        String latestDate = dayRepo.getCurrentDay().getDate();

        if (!latestDate.equals(date)) {
            Log.d("HomeFragment", "onCreateView: Database update triggered");
            updateDatabase(dailyIntake, totalDrank);
        } else {
            Log.d("HomeFragment", "onCreateView: Database update not triggered");
        }
    }

    public void updateDatabase(int dailyIntake, int totalDrank) {

        Day newDay = new Day();
        SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy");
        String date = ft.format(new Date());

        Day currentDay = dayRepo.getCurrentDay();
        int dayId = currentDay.getDayId();
        Log.d("HomeFragment", "Day ID: " + dayId);
        int streak = dayRepo.getCurrentStreak();

        newDay.setDate(date);
        newDay.setDayId(dayId);
        newDay.setGoal(dailyIntake);
        Log.d("HomeFragment", "Progress: " + totalDrank);
        newDay.setProgress(totalDrank);

        if (totalDrank >= dailyIntake) {
            streak++;
        } else {
            streak = 0;
        }
        newDay.setStreak(streak);
        dayRepo.addDay(newDay);
    }
    public void setTodayGoal(int dailyIntake) {
            TextView goalTextView = getView().findViewById(R.id.goal);
            goalTextView.setText(String.valueOf(dailyIntake));
    }

}
