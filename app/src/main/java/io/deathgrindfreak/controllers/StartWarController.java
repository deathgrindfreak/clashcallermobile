package io.deathgrindfreak.controllers;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import io.deathgrindfreak.clashcallermobile.StartWarActivity;

/**
 * Created by jcbell on 4/15/2015.
 */
public class StartWarController {

    private StartWarActivity startWarActivity;

    private static final String SWCTAG = "Start War Controller";

    public StartWarController(StartWarActivity startWarActivity) {
        this.startWarActivity = startWarActivity;
    }


    public String getWarId(String encodeURIString) {

        BufferedReader in = null;
        String warIdUrl = "";

        try {
            URL warUrl = new URL(encodeURIString);
            URLConnection warCon = warUrl.openConnection();
            warCon.setDoOutput(true);
            warCon.connect();

            in = new BufferedReader(new InputStreamReader(warCon.getInputStream()));

            warIdUrl = in.readLine();

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

        return warIdUrl;
    }
}
