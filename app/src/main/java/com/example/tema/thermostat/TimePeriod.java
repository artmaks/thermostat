package com.example.tema.thermostat;

/**
 * Created by Olga on 28.05.2015.
 */
public class TimePeriod {
    int startHour;
    int startMinute;
    int endHour;
    int endMinute;

    public TimePeriod(int startHour, int startMinute, int endHour, int endMinute){
        this.startHour=startHour;
        this.startMinute=startMinute;
        this.endHour=endHour;
        this.endMinute=endMinute;
    }

    public void setStartTime(int startHour, int startMinute){
        this.startHour=startHour;
        this.startMinute=startMinute;
    }

    public  void setEndTime(int endHour, int endMinute){
        this.endHour=endHour;
        this.endMinute=endMinute;
    }


    public boolean compareWithTime(int hour, int minute){
        boolean result;
        if (hour>=startHour){
            if (hour<endHour){
                result=true;
            } else if (hour==endHour&&minute<endMinute) {
                result=true;
            } else {
                result=false;
            }
        } else {
            result=false;
        }
        return result;
    }

    @Override
    public String toString(){
        return  String.format("%02d", startHour) + ":"
                + String.format("%02d", startMinute)+ " - " +
                String.format("%02d", endHour) + ":"
                + String.format("%02d", endMinute);
    }

}
