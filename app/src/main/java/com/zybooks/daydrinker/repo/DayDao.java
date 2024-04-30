package com.zybooks.daydrinker.repo;

import androidx.room.*;
import com.zybooks.daydrinker.model.Day;
import java.util.List;

@Dao
public interface DayDao {
    @Query("SELECT * FROM Day ORDER BY dayId DESC LIMIT 7")
    List<Day> getCurrentWeek();

    @Query("SELECT * FROM Day WHERE dayId = (SELECT MAX(dayId) FROM Day)")
    Day getCurrentDay();

    @Query("SELECT streak FROM Day WHERE dayId = (SELECT MAX(dayId) FROM Day)")
    int getCurrentStreak();

    @Query("SELECT MAX(streak) FROM Day")
    int getMaxStreak();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addDay(Day day);
}
