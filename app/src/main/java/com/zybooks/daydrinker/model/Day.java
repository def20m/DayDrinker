package com.zybooks.daydrinker.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Day {

    @PrimaryKey
    @ColumnInfo(name = "dayId")
    private int dayId;

    @ColumnInfo(name = "date")
    private Date date;

    @ColumnInfo(name = "goal")
    private int goal;

    @ColumnInfo(name = "progress")
    private int progress;

    @ColumnInfo(name = "streak")
    private int streak;

    public int getDayId(){
        return dayId;
    }
    public Date getDate(){
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
    public void setDate(Date date){
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
}