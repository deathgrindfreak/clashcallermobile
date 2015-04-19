package io.deathgrindfreak.util;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jcbell on 4/19/2015.
 */
public class ApiClassConnector extends AsyncTask<String, Integer, String>{

    private static final String APITAG = "ApiClassConnector";

    @Override
    protected String doInBackground(String... params) {

        String url = params[0];
        String parms = params[1];
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
            Log.e(APITAG, ex.getMessage());
        } catch (IOException ex) {
            Log.e(APITAG, ex.getMessage());
        } catch (Exception ex) {
            Log.e(APITAG, ex.getMessage());
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException ex) {
                Log.e(APITAG, ex.getMessage());
            }
        }

        return sb.toString();
    }
}
