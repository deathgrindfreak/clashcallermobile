package io.deathgrindfreak.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.deathgrindfreak.model.Clan;

/**
 * Created by jcbell on 4/18/2015.
 */
public class JsonParse {

    public static Clan parseWarJson(String jsonString) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.fromJson(jsonString, Clan.class);
    }
}
