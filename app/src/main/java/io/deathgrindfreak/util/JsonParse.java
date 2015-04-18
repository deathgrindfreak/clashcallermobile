package io.deathgrindfreak.util;

import com.google.gson.Gson;

import io.deathgrindfreak.model.Clan;

/**
 * Created by jcbell on 4/18/2015.
 */
public class JsonParse {

    public static Clan parseWarJson(String jsonString) {
        return (new Gson()).fromJson(jsonString, Clan.class);
    }
}
