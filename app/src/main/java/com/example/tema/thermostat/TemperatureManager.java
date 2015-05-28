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
    private static double day_temper;
    private static double night_temper;
    public static ArrayList<PeriodDay> days;
    int dayOfWeek;

    Date currentTime;
    public TemperatureManager(TextView target_temp){
        currentTime=new Date();
        System.out.printf(String.valueOf(Calendar.DAY_OF_WEEK));

        day_temper=19.4;
        night_temper=15.3;

        Calendar c = Calendar.getInstance();
        c.setTime(currentTime);
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        int hour=c.get(Calendar.HOUR);
        int minutes=c.get(Calendar.MINUTE);

        days=new ArrayList<PeriodDay>();
        days.add(new PeriodDay(1, new TimePeriod(0, 0, 12, 0)));
        days.add(new PeriodDay(2, new TimePeriod(0, 0, 13, 0)));
        days.add(new PeriodDay(3, new TimePeriod(1, 0, 12, 0)));
        days.add(new PeriodDay(4, new TimePeriod(12, 0, 15, 59)));
        days.add(new PeriodDay(5, new TimePeriod(0, 30, 12, 0)));
        days.add(new PeriodDay(6, new TimePeriod(0, 30, 12, 0)));
        days.add(new PeriodDay(7, new TimePeriod(0, 0, 12, 0)));

        if (days.get(dayOfWeek-1).comparePeriod(hour, minutes)){
            target_temp.setText(String.valueOf(day_temper));
        }else {
            target_temp.setText(String.valueOf(night_temper));
        }
    }

    public ArrayList<Item> getNextDays(){
        ArrayList<Item> models = new ArrayList<Item>();
        models.add(new Item("Today - " + days.get(dayOfWeek-1).getDay()));
        models.add(new Item(String.valueOf(days.get(dayOfWeek-1).dayPeriod.startMinute), String.valueOf(day_temper)));
        models.add(new Item(days.get(dayOfWeek-1).nightPeriod.toString(), String.valueOf(night_temper)));


        int day=dayOfWeek==7?1:dayOfWeek;
        for(int i=0; i<4; i++){
            day=day+1>=8?1:day+1;
            models.add(new Item(days.get(day-1).getDay()));
            models.add(new Item(days.get(day-1).dayPeriod.toString(), String.valueOf(day_temper)));
            models.add(new Item(days.get(day-1).nightPeriod.toString(), String.valueOf(night_temper)));
        }

        return models;
    }
}
