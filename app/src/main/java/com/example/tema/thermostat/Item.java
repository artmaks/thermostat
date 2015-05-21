package com.example.tema.thermostat;

/**
 * Created by tema on 21.05.15.
 */
public class Item {
    public String Title;
    public String Period;
    public String Temperature;

    public boolean isGroupHeader = false;

    public Item(String title) {
        this("","");
        isGroupHeader = true;
        this.Title = title;
    }

    public Item(String period, String temperature) {
        this.Period = period;
        this.Temperature = temperature;
    }
}
