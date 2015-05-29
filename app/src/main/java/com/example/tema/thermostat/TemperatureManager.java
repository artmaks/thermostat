package com.example.tema.thermostat;

import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Olga on 28.05.2015.
 */

public class TemperatureManager {
    private static float day_temper;
    private static float night_temper;
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

        day_temper=29.4f;
        night_temper=5.3f;

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

        models.add(new Item("Today - " + days.get(dayOfWeek-1).getDay()));
        models.add(new Item(days.get(dayOfWeek-1).dayPeriod.toString(), dayT));
        models.add(new Item(days.get(dayOfWeek-1).nightPeriod.toString(), nightT));

        int day=dayOfWeek==7?1:dayOfWeek;
        for(int i=0; i<2; i++){
            day=day+1>=8?1:day+1;

            models.add(new Item(days.get(day-1).getDay()));
            models.add(new Item(days.get(day-1).dayPeriod.toString(), dayT));
            models.add(new Item(days.get(day-1).nightPeriod.toString(), nightT));
        }

        return models;
    }
}
