package io.deathgrindfreak.controllers;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import io.deathgrindfreak.clashcallermobile.StartWarActivity;
import io.deathgrindfreak.model.Clan;
import io.deathgrindfreak.util.JsonParse;

/**
 * Created by jcbell on 4/15/2015.
 */
public class StartWarController {

    private StartWarActivity startWarActivity;

    private static final String SWCTAG = "Start War Controller";

    public StartWarController(StartWarActivity startWarActivity) {
        this.startWarActivity = startWarActivity;
    }

    public String getWarId(String url, String parms) {
        return getReturnString(url, parms);
    }


    public Clan getClanInfo(String url, String parms) {
        String jsonStr = getReturnString(url, parms);
        return JsonParse.parseWarJson(jsonStr);
    }


    private String getReturnString(String url , String parms) {

        BufferedReader in = null;
        StringBuilder sb = null;

        try {
            URL warUrl = new URL(url);
            HttpURLConnection warCon = (HttpURLConnection) warUrl.openConnection();
            warCon.setRequestMethod("POST");
            warCon.setDoOutput(true);
            warCon.connect();

            PrintStream ps = new PrintStream(warCon.getOutputStream());
            ps.print(parms);

            in = new BufferedReader(new InputStreamReader(warCon.getInputStream()));

            String str;
            sb = new StringBuilder();
            while((str = in.readLine()) != null)
                sb.append(str);

        } catch (MalformedURLException ex) {
            Log.e(SWCTAG, ex.getMessage());
        } catch (IOException ex) {
            Log.e(SWCTAG, ex.getMessage());
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException ex) {
                Log.e(SWCTAG, ex.getMessage());
            }
        }

        return sb.toString();
    }
}
