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
import java.util.concurrent.ExecutionException;

import io.deathgrindfreak.clashcallermobile.JoinWarActivity;
import io.deathgrindfreak.clashcallermobile.ShowWarActivity;
import io.deathgrindfreak.clashcallermobile.StartWarActivity;
import io.deathgrindfreak.model.Clan;
import io.deathgrindfreak.util.ApiClassConnector;
import io.deathgrindfreak.util.JsonParse;

/**
 * Created by jcbell on 4/15/2015.
 */
public class StartWarController {

    private StartWarActivity startWarActivity;
    private JoinWarActivity joinWarActivity;
    private ShowWarActivity showWarActivity;

    private static final String SWCTAG = "Start War Controller";

    public StartWarController(StartWarActivity startWarActivity) {
        this.startWarActivity = startWarActivity;
    }

    public StartWarController(JoinWarActivity joinWarActivity) {
        this.joinWarActivity = joinWarActivity;
    }

    public StartWarController(ShowWarActivity showWarActivity) {
        this.showWarActivity = showWarActivity;
    }

    public String getWarId(String url, String parms) {
        return getReturnString(url, parms);
    }


    public Clan getClanInfo(String url, String parms) {
        String jsonStr = getReturnString(url, parms);
        return JsonParse.parseWarJson(jsonStr);
    }

    public String submitCallName(String url, String parms) {
        return getReturnString(url, parms);
    }

    public String appendCall(String url, String parms) {
        return getReturnString(url, parms);
    }

    public String setClanMessage(String url, String parms) {
        return getReturnString(url, parms);
    }

    public String deleteCall(String url, String parms) {
        return getReturnString(url, parms);
    }


    private String getReturnString(String url , String parms) {
        String returnStr = "";
        try {
            returnStr = new ApiClassConnector().execute(url, parms).get();
        } catch (InterruptedException e) {
            Log.e(SWCTAG, e.getMessage());
        } catch (ExecutionException e) {
            Log.e(SWCTAG, e.getMessage());
        }
        return returnStr;
    }
}
