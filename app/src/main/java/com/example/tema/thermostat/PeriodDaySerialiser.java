package com.example.tema.thermostat;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by Olga on 31.05.2015.
 */
public class PeriodDaySerialiser implements JsonSerializer<PeriodDay> {

    @Override
    public JsonElement serialize(final PeriodDay src, Type typeOfSrc, JsonSerializationContext context) {
        final PeriodDay day = (PeriodDay) src;

        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", day.Day);
        jsonObject.addProperty("night", TemperatureManager.night_temper);

        final  JsonElement jsonPeriods=context.serialize(src.getArrayPeriods());
        jsonObject.add("periods", jsonPeriods);

        return jsonObject;
    }
}
