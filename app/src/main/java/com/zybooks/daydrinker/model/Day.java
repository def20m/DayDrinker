package com.zybooks.daydrinker.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Day {

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "dayId")
    private int dayId;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "goal")
    private int goal;

    @ColumnInfo(name = "progress")
    private int progress;

    @ColumnInfo(name = "streak")
    private int streak;

    public int getDayId(){
        return dayId;
    }
    public String getDate(){
        return date;
    }
    public int getGoal(){
        return goal;
    }
    public int getProgress(){
        return progress;
    }
    public int getStreak(){
        return streak;
    }

    public void setDayId(int dayId){
        this.dayId = dayId;
    }
    public void setDate(String date){
        this.date = date;
    }
    public void setGoal(int goal){
        this.goal = goal;
    }
    public void setProgress(int progress){
        this.progress = progress;
    }
    public void setStreak(int streak){
        this.streak = streak;
    }

    @Override
    public String toString(){
        return getDate() + '\t' + Integer.toString(getGoal()) + '\t' + Integer.toString(getProgress());
    }
}
