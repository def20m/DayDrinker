package com.zybooks.daydrinker.repo;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.zybooks.daydrinker.model.Day;
import java.util.List;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DayRepository {

    private static DayRepository dayRepo;
    private final DayDao dayDao;
    private static final int NUMBER_OF_THREADS = 4;
    private static final ExecutorService databaseExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static DayRepository getInstance(Context context) {
        if (dayRepo == null) {
            dayRepo = new DayRepository(context);
        }
        return dayRepo;
    }

    private DayRepository(Context context) {

        RoomDatabase.Callback databaseCallback = new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
            }
        };

        DayDatabase database = Room.databaseBuilder(context, DayDatabase.class, "day.db")
                .addCallback(databaseCallback)
                .build();

        dayDao = database.dayDao();

        if(dayDao.getCurrentDay() == null){
            firstDay();
        }
    }

    private void firstDay(){
        Day newDay = new Day();
        newDay.setDate(new Date().toString());
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

    public List<Day> getCurrentWeek(){
        return dayDao.getCurrentWeek();
    }

    public int getCurrentStreak(){
        return dayDao.getCurrentStreak();
    }

    public int getMaxStreak(){
        return dayDao.getMaxStreak();
    }

}
