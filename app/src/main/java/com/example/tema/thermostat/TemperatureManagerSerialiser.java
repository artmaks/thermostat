package com.example.tema.thermostat;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by Olga on 31.05.2015.
 */
public class TemperatureManagerSerialiser implements JsonSerializer<TemperatureManager> {

    @Override
    public JsonElement serialize(TemperatureManager src, Type typeOfSrc, JsonSerializationContext context) {

        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("day-temp", TemperatureManager.day_temper);
        jsonObject.addProperty("night", TemperatureManager.night_temper);
        jsonObject.addProperty("cur-temp", MainActivity.currentTemperature);
        jsonObject.addProperty("vac-mode", TemperatureManager.isVacationMode);
        jsonObject.addProperty("vac-temp", TemperatureManager.vacation_temp);

        final JsonElement days=context.serialize(TemperatureManager.getArrayPeriodDays());
        jsonObject.add("days", days);

        return jsonObject;
    }
}
