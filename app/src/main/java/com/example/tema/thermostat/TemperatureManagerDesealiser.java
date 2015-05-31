package com.example.tema.thermostat;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by Olga on 31.05.2015.
 */
public class TemperatureManagerDesealiser implements JsonDeserializer<TemperatureManager> {
    @Override
    public TemperatureManager deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
            throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();

        final JsonElement jsonDTemp = jsonObject.get("day-temp");
        final float DTemp = jsonDTemp.getAsFloat();
        final JsonElement jsonNTemp = jsonObject.get("night");
        final float NTemp = jsonNTemp.getAsFloat();
        final JsonElement isVacMode=jsonObject.get("vac-mode");
        final  boolean mode=isVacMode.getAsBoolean();
        final JsonElement jsonVacTemp = jsonObject.get("vac-temp");
        final float vacTemp = jsonVacTemp.getAsFloat();
        final JsonElement temp = jsonObject.get("cur-temp");
        final float cTemp = temp.getAsFloat();

        PeriodDay[] days=context.deserialize(jsonObject.get("days"), PeriodDay[].class);

        final TemperatureManager manager=new TemperatureManager(DTemp, NTemp, days, mode, vacTemp, cTemp);
        return manager;
    }
}
