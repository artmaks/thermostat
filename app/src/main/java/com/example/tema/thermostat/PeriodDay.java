package com.example.tema.thermostat;

import java.util.Calendar;

/**
 * Created by Olga on 28.05.2015.
 */
public class PeriodDay {
    int Day;
    TimePeriod dayPeriod;
    TimePeriod nightPeriod;

    public PeriodDay(int Day, TimePeriod day, TimePeriod nightPeriod){
        this.Day=Day;
        this.dayPeriod=day;
        this.nightPeriod=nightPeriod;
    }
    public PeriodDay(int Day, TimePeriod day){
        this.Day=Day;
        setDayPeriod(day.startHour, day.startMinute, day.endHour, day.endMinute);
    }

    public void setDayPeriod(int startHour, int startMinute,int endHour, int endMinute){
        dayPeriod=new TimePeriod(startHour, startMinute, endHour, endMinute);
        nightPeriod=new TimePeriod(endHour, endMinute, startHour, startMinute);

    }


    public boolean comparePeriod(int hour, int minute){
        if (dayPeriod.compareWithTime(hour,minute)){
            return true;
        } else {
            return false;
        }
    }

    public String getDay(){
        String result;
        switch (Day){
            case 1: result="Sunday";
                    break;
            case 2: result="Monday";
                break;
            case 3: result="Wednesday";
                break;
            case 4: result="Wednesday";
                break;
            case 5: result="Thursday";
                break;
            case 6: result="Friday";
                break;
            case 7: result="Saturday";
                break;
            default: result="null";
                break;
        }
        return result;
    }

}
