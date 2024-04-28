package com.zybooks.daydrinker.repo;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.zybooks.daydrinker.model.Day;

@Database(entities = {Day.class}, version = 2)
public abstract class DayDatabase extends RoomDatabase {
    public abstract DayDao dayDao();

}
