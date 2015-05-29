package com.example.tema.thermostat;

import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Olga on 28.05.2015.
 */

public class TemperatureManager {
    private static float day_temper;
    private static float night_temper;
    private float target;
    public static ArrayList<PeriodDay> days;
    int dayOfWeek;
    int currentDay;
    int hour;
    int minutes;

    Date currentTime;
    long currenSeconds;

    public TemperatureManager(){
        currentTime=new Date();
        currenSeconds=currentTime.getTime();

        day_temper=19.4f;
        night_temper=15.3f;

        Calendar c = Calendar.getInstance();
        c.setTime(currentTime);
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        hour=c.get(Calendar.HOUR_OF_DAY);
        minutes=c.get(Calendar.MINUTE);

        days=new ArrayList<PeriodDay>();
        days.add(new PeriodDay(1, new TimePeriod(0, 0, 12, 0)));
        days.add(new PeriodDay(2, new TimePeriod(0, 0, 13, 0)));
        days.add(new PeriodDay(3, new TimePeriod(1, 0, 12, 0)));
        days.add(new PeriodDay(4, new TimePeriod(12, 0, 15, 59)));
        days.add(new PeriodDay(5, new TimePeriod(0, 30, 12, 0)));
        days.add(new PeriodDay(6, new TimePeriod(0, 29, 12, 0)));
        days.add(new PeriodDay(7, new TimePeriod(0, 11, 12, 0)));

        if (days.get(dayOfWeek-1).comparePeriod(hour, minutes)){
            target= day_temper;
        }else {
            target= night_temper;
        }
    }


    public void incrementcurrentTime(int milliseconds){
        currenSeconds+=milliseconds;
        currentTime=new Date(currenSeconds);
        Calendar c = Calendar.getInstance();
        c.setTime(currentTime);
        currentDay = c.get(Calendar.DAY_OF_WEEK);
        hour=c.get(Calendar.HOUR_OF_DAY);
        minutes=c.get(Calendar.MINUTE);
    }

    public boolean isNewPeriod(){
        float old_target=this.target;
        if (days.get(dayOfWeek-1).comparePeriod(hour, minutes)){
            this.target = day_temper;
        }else {
            this.target = night_temper;
        }

        return this.target!=old_target;
    }

    public boolean isAnotherDay(){
        if (currentDay!=dayOfWeek){
            dayOfWeek=currentDay;
            return true;
        }
        return false;
    }


    public float getTargetTemprature()
    {
        return target;
    }

    public ArrayList<Item> getNextDays(){
        ArrayList<Item> models = new ArrayList<Item>();
        models.add(new Item("Today - " + days.get(dayOfWeek-1).getDay()));
        models.add(new Item(days.get(dayOfWeek-1).dayPeriod.toString(), String.valueOf(day_temper)));
        models.add(new Item(days.get(dayOfWeek-1).nightPeriod.toString(), String.valueOf(night_temper)));

        int day=dayOfWeek==7?1:dayOfWeek;
        for(int i=0; i<2; i++){
            day=day+1>=8?1:day+1;

            models.add(new Item(days.get(day-1).getDay()));
            models.add(new Item(days.get(day-1).dayPeriod.toString(), String.valueOf(day_temper)));
            models.add(new Item(days.get(day-1).nightPeriod.toString(), String.valueOf(night_temper)));
        }

        return models;
    }
}
