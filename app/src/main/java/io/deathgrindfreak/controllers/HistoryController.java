package io.deathgrindfreak.controllers;

import android.content.Context;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;

import io.deathgrindfreak.clashcallermobile.HistoryActivity;
import io.deathgrindfreak.clashcallermobile.JoinWarActivity;
import io.deathgrindfreak.clashcallermobile.R;
import io.deathgrindfreak.clashcallermobile.StartWarActivity;

/**
 * Created by deathgrindfreak on 5/20/15.
 */
public class HistoryController {

    private HistoryActivity historyActivity;
    private JoinWarActivity joinWarActivity;
    private StartWarActivity startWarActivity;

    private static final String HCTAG = "History Controller";


    public HistoryController(HistoryActivity historyActivity) {
        this.historyActivity = historyActivity;
    }

    public HistoryController(JoinWarActivity joinWarActivity) {
        this.joinWarActivity = joinWarActivity;
    }

    public HistoryController(StartWarActivity startWarActivity) {
        this.startWarActivity = startWarActivity;
    }

    /**
     * Saves the history map to a file for later use.
     *
     * @param histMap  The history map to be saved
     * @throws IOException  If write was unsuccessful
     */
    public void saveHistory(Context context, LinkedHashMap<String, HashMap<String, String>> histMap) throws IOException {
        writeJsonFile(context, mapToJson(histMap));
    }


    /**
     * Loads the history map from the file in memory
     *
     * @return  the history map
     * @throws IOException  If the read was unsuccessful
     */
    public LinkedHashMap<String, HashMap<String, String>> loadHistory(Context context) throws IOException {

        String json = readJsonFile(context);

        LinkedHashMap<String, HashMap<String, String>> histMap =
                json.isEmpty() ? new LinkedHashMap<String, HashMap<String, String>>() : jsonToMap(json);

        return  histMap;
    }


    private String mapToJson(LinkedHashMap<String, HashMap<String, String>> histMap) {
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(histMap);

        Log.d(HCTAG, "Json string in mapToJson: " + json);

        return json;
    }


    private LinkedHashMap<String, HashMap<String, String>> jsonToMap(String json) {
        Gson gson = new Gson();
        Type histMap = new TypeToken<LinkedHashMap<String, HashMap<String, String>>>(){}.getType();
        LinkedHashMap<String, HashMap<String, String>> map = gson.fromJson(json, histMap);

        Log.d(HCTAG, "History map in jsonToMap: " + map);

        return map;
    }


    private void writeJsonFile(Context context, String historyStr) throws IOException {

        FileOutputStream stream = context.openFileOutput(context
                .getResources()
                .getString(R.string.history_file), Context.MODE_PRIVATE);

        stream.write(historyStr.getBytes());
        stream.close();
    }


    private String readJsonFile(Context context) throws IOException {

        String historyFile = context.getResources().getString(R.string.history_file);

        if (fileExists(context, historyFile)) {

            FileInputStream stream = context.openFileInput(historyFile);

            InputStreamReader isr = new InputStreamReader (stream) ;
            BufferedReader buffreader = new BufferedReader (isr) ;

            String readString;
            StringBuilder strBuild = new StringBuilder();
            while ((readString = buffreader.readLine()) != null) {
                strBuild.append(readString);
            }

            isr.close() ;
            stream.close();

            return strBuild.toString();

        } else {
            return "";
        }
    }


    private boolean fileExists(Context context, String filename) {

        File file = context.getFileStreamPath(filename);

        if(file == null || !file.exists()) {
            return false;
        }
        return true;
    }


    public int dptopx(int dp) {
        DisplayMetrics m =
                historyActivity.getResources().getDisplayMetrics();
        return dp * (m.densityDpi / 160);
    }
}
