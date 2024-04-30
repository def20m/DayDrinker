package com.zybooks.daydrinker.repo;

import android.content.Context;

import androidx.room.Room;

import com.zybooks.daydrinker.model.Day;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DayRepository {

    private static DayRepository dayRepo;
    private final DayDao dayDao;
    private static final int NUMBER_OF_THREADS = 4;
    private static final ExecutorService databaseExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static final String COLUMN_ID = "Day";
    public static final String COLUMN_DATE = "Date";
    public static final String COLUMN_GOAL = "Goal";
    public static final String COLUMN_PROGRESS = "Intake";
    public static final String COLUMN_STREAK = "Streak";

    public static DayRepository getInstance(Context context) {
        if (dayRepo == null) {
            dayRepo = new DayRepository(context);
        }
        return dayRepo;
    }

    public DayRepository(Context context) {

        DayDatabase database = Room.databaseBuilder(context, DayDatabase.class, "day.db")
                .allowMainThreadQueries()
                .build();

        dayDao = database.dayDao();

        if(dayDao.getCurrentDay() == null){
            firstDay();
        }
    }

    private void firstDay(){
        Day newDay = new Day();
        SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy");
        String date = ft.format(new Date());
        newDay.setDate(date);
        newDay.setDayId(0);
        newDay.setGoal(12);
        newDay.setProgress(0);
        newDay.setStreak(0);
        addDay(newDay);
    }

    public void addDay(Day day){
        databaseExecutor.execute(() -> {
            dayDao.addDay(day);
        });
    }


    public Day getCurrentDay(){
            return dayDao.getCurrentDay();
    }

    public List<String> getCurrentWeek(){
        List<Day> week = dayDao.getCurrentWeek();
        List<String> weekString = new ArrayList<>();
        for ( Day d : week ) {
            String formattedDay = String.format("%-20s%-10s%-10s", d.getDate(), d.getGoal(), d.getProgress());
            weekString.add(formattedDay);
            //weekString.add(d.toString());
        }
        return weekString;
    }

    public int getCurrentStreak(){
        return dayDao.getCurrentStreak();
    }

    public int getMaxStreak(){
        return dayDao.getMaxStreak();
    }

}
