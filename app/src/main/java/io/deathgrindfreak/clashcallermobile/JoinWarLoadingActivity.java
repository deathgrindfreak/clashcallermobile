package io.deathgrindfreak.clashcallermobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import io.deathgrindfreak.controllers.ShowWarController;
import io.deathgrindfreak.model.Clan;
import io.deathgrindfreak.util.UrlParameterContainer;


public class JoinWarLoadingActivity extends ActionBarActivity {

    private ShowWarController showWarController;

    private static final String LOADTAG = "Load War Activity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_war_loading);

        showWarController = new ShowWarController();

        Bundle data = getIntent().getExtras();
        String warId = data.getString("warId");

        loadWarView(warId);
    }

    private void loadWarView(String warId) {

        // Initialize the war view
        Intent showWarIntent = new Intent(JoinWarLoadingActivity.this,
                ShowWarActivity.class);

        UrlParameterContainer<String, String> clanInfoUrl =
                new UrlParameterContainer<>(new String[]{"REQUEST", "warcode"});

        clanInfoUrl.put("REQUEST", "GET_FULL_UPDATE");
        clanInfoUrl.put("warcode", warId);

        Log.i(LOADTAG, "warId passed in join: " + warId);

        Clan clanInfo = showWarController.getClanInfo(getApplicationContext(), getResources().getString(R.string.api_url),
                clanInfoUrl.getEncodeURIString());

        Log.d(LOADTAG, "<-- CLANINFO -->");
        Log.d(LOADTAG, clanInfo == null ? "null" : clanInfo.toString());

        if (clanInfo != null) {
            showWarIntent.putExtra("clan", clanInfo);
            startActivity(showWarIntent);
            finish();
        } else {
            // TODO handle empty Clan
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_war_loading, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
