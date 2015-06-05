package io.deathgrindfreak.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import io.deathgrindfreak.clashcallermobile.JoinWarActivity;

/**
 * Created by jcbell on 4/19/2015.
 */
public class ApiClassConnector extends AsyncTask<String, Integer, String>{

    private Context context;
    private ProgressDialog progress;
    private TaskCallback callback;

    private static final String APITAG = "ApiClassConnector";


    public ApiClassConnector(TaskCallback callback, Context context) {
        this.callback = callback;
        this.context = context;

        progress = new ProgressDialog(context);
    }


    @Override
    protected void onPreExecute() {

        // Show the progress dialog while api is connecting
        progress.setMessage(callback.getProgressMessage());
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }


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

        if (sb == null) {
            return "";
        } else {
            return sb.toString();
        }
    }

    @Override
    protected void onPostExecute(String returnStr) {

        Log.d(APITAG, "Return string: " + returnStr);

        if (returnStr.isEmpty()) {

            // Dismiss the progress dialog
            if (progress.isShowing())
                progress.dismiss();

            Toast tst = Toast.makeText(context,
                    "An unexpected network error occured.  Please check your connection and try again later.",
                    Toast.LENGTH_SHORT);
            tst.setGravity(Gravity.CENTER, 0, 0);
            tst.show();
        } else {
            if (callback != null)
                callback.onTaskCompleted(progress, returnStr);
        }
    }
}
