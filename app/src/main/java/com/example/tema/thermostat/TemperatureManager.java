package com.example.tema.thermostat;


import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Olga on 28.05.2015.
 */

public class TemperatureManager {
    public static float day_temper;
    public static float night_temper;

    public static boolean isVacationMode;
    public static float vacation_temp;
    private float target;
    public static ArrayList<PeriodDay> days;
    private int dayOfWeek;
    private int currentDay;
    private int hour;
    private int minutes;
    private boolean dayPeriod;

    Date currentTime;
    long currenSeconds;

    public TemperatureManager(){
        currentTime=new Date();
        currenSeconds=currentTime.getTime();

        vacation_temp=18.9f;
        day_temper=29.4f;
        night_temper=5.3f;

        isVacationMode=false;
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
            dayPeriod=true;
        }else {
            target= night_temper;
            dayPeriod=false;
        }
    }

    public void updateAfterReturn(){
        // count time from returning
        currentTime=new Date();
        long milliseconds=currentTime.getTime();
        currenSeconds+=(milliseconds-currenSeconds)*3000;

        //initialise others
        currentTime = new Date(currenSeconds);
        Calendar c = Calendar.getInstance();
        c.setTime(currentTime);
        currentDay = c.get(Calendar.DAY_OF_WEEK);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minutes = c.get(Calendar.MINUTE);
        if (days.get(dayOfWeek-1).comparePeriod(hour, minutes)){
            target= day_temper;
            dayPeriod=true;
        }else {
            target= night_temper;
            dayPeriod=false;
        }
    }

    public boolean isDayPeriod(){
        return dayPeriod;
    }

    public static void setDay_temper(float temper){
        day_temper=temper;
    }

    public static void setNight_temper(float temper){
        night_temper=temper;
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

    public static String getStringDayTemp(){
        return String.format(Locale.ENGLISH, "%.1f", day_temper);
    }

    public boolean isNewPeriod(){
        float old_target=this.target;
        if (days.get(dayOfWeek-1).comparePeriod(hour, minutes)){
            this.target = day_temper;
            dayPeriod=true;
        }else {
            this.target = night_temper;
            dayPeriod=false;
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

        String dayT=String.format(Locale.ENGLISH, "%.1f", day_temper);
        String nightT=String.format(Locale.ENGLISH, "%.1f", night_temper);

        int day=dayOfWeek;

        for (int i=0;i<3;i++ ){
            if (i==0){
                models.addAll(days.get(day-1).getItem(true, dayT, nightT));
            } else {
                models.addAll(days.get(day-1).getItem(false, dayT, nightT));
            }
            day=day==7?1:day+1;
        }

        return models;
    }
}
