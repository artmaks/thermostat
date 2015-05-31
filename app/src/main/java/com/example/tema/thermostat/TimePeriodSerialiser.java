package com.example.tema.thermostat;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by Olga on 31.05.2015.
 */
public class TimePeriodSerialiser implements JsonSerializer<TimePeriod> {

    @Override
    public JsonElement serialize(TimePeriod period, Type typeOfSrc, JsonSerializationContext context) {


        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("start-hour", period.startHour);
        jsonObject.addProperty("start-minute", period.startMinute);
        jsonObject.addProperty("end-hour", period.endHour);
        jsonObject.addProperty("end-minute", period.endMinute);

        return jsonObject;
    }
}