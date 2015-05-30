package com.example.tema.thermostat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by Olga on 28.05.2015.
 */
public class PeriodDay {
    int Day;
    ArrayList<TimePeriod> daysperiods;
    List<TimePeriod> nightperiods;
    TimePeriod dayPeriod;
    TimePeriod nightPeriod;

    public PeriodDay(int Day, TimePeriod day, TimePeriod nightPeriod){
        this.Day=Day;
        this.dayPeriod=day;
        this.nightPeriod=nightPeriod;
    }
    public PeriodDay(int Day, TimePeriod day){
        this.Day=Day;
        daysperiods=new ArrayList<TimePeriod>();
        nightperiods=new ArrayList<TimePeriod>();
        addDayPeriod(day.startHour, day.startMinute, day.endHour, day.endMinute);
    }
    public PeriodDay(int Day, ArrayList<TimePeriod> daysPeriods){
        this.Day=Day;
        this.daysperiods=daysPeriods;
        nightperiods=new ArrayList<TimePeriod>();
    }

    private void setNightPeriods(){
        if (daysperiods.size()==1){
            nightperiods.add(new TimePeriod(daysperiods.get(0).endHour, daysperiods.get(0).endMinute, daysperiods.get(0).startHour, daysperiods.get(0).startMinute));
            return;
        }
        for (int i=0; i<daysperiods.size()-1; i++){
            TimePeriod curPeriod=daysperiods.get(i);
            TimePeriod nextPeriod=daysperiods.get(i+1);
            nightperiods.add(new TimePeriod(curPeriod.endHour, curPeriod.endMinute, nextPeriod.startHour, nextPeriod.startMinute));
        }
    }

    public void addDayPeriod(int startHour, int startMinute,int endHour, int endMinute){
        daysperiods.add(new TimePeriod(startHour, startMinute, endHour, endMinute));
        Collections.sort(daysperiods);
        nightperiods.clear();
        setNightPeriods();
    }

    public boolean isFull(){
        return daysperiods.size()>=5;
    }

    public void setDayPeriod(int startHour, int startMinute,int endHour, int endMinute){
        dayPeriod=new TimePeriod(startHour, startMinute, endHour, endMinute);
        nightPeriod=new TimePeriod(endHour, endMinute, startHour, startMinute);

    }

    public ArrayList<Item>  getItem(boolean isFirst, String dayT, String nightT){
        ArrayList<Item> models = new ArrayList<Item>();


        if (isFirst){
            models.add(new Item("Today - " + getDay()));
        } else {
            models.add(new Item(getDay()));
        }

        for (int i=0; i<daysperiods.size(); i++) {
            models.add(new Item(daysperiods.get(i).toString(), dayT));
            if (i<nightperiods.size())
                models.add(new Item(nightperiods.get(i).toString(), nightT));
        }
        return models;
    }

    public ArrayList<Item> getDayPeriods(){
        ArrayList<Item> models = new ArrayList<Item>();
        String dayT=TemperatureManager.getStringDayTemp();

        for (int i=0; i<daysperiods.size(); i++) {
            models.add(new Item(daysperiods.get(i).toString(), dayT));
        }
        return models;
    }

    public boolean comparePeriod(int hour, int minute){
        for (int i=0; i<daysperiods.size(); i++){
            // time can be only in one period
            if (daysperiods.get(i).compareWithTime(hour,minute)){
                return true;
            }
        }

        return false;
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
