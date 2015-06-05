package io.deathgrindfreak.clashcallermobile;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;

import io.deathgrindfreak.controllers.HistoryController;
import io.deathgrindfreak.controllers.ShowWarController;
import io.deathgrindfreak.model.Clan;
import io.deathgrindfreak.util.JsonParse;
import io.deathgrindfreak.util.TaskCallback;
import io.deathgrindfreak.util.UrlParameterContainer;

public class JoinWarActivity extends ActionBarActivity {

    private Typeface clashFont;

    private ShowWarController showWarController;
    private HistoryController historyController;

    private static final String JOINTAG = "Join War Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_war);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        getSupportActionBar().setIcon(R.mipmap.ic_cclogo);

        showWarController = new ShowWarController(this);
        historyController = new HistoryController(this);


        // Set the Clash of Clans font
        clashFont = Typeface.createFromAsset(getAssets(), "Supercell-magic-webfont.ttf");

        // Set the button typeface
        Button submit = (Button) findViewById(R.id.joinSubmitButton);
        TextView joinWar = (TextView) findViewById(R.id.joinWarText);

        submit.setTypeface(clashFont);
        submit.setTextColor(getResources().getColor(R.color.white));
        joinWar.setTypeface(clashFont);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_join_war, menu);
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
            Intent homeIntent = new Intent(this, ClashSettingsActivity.class);
            startActivity(homeIntent);
        }

        if (id == R.id.home) {
            Intent homeIntent = new Intent(this, MainActivity.class);
            startActivity(homeIntent);
        }

        if (id == R.id.action_help) {
            startActivity(new Intent(this, HelpActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }


    private String getWarIdFromField() {
        EditText warField = (EditText) findViewById(R.id.warIdCodeField);
        return warField.getText().toString();
    }


    public void joinSubmitButtonClicked(View view) {

        String warId = getWarIdFromField();

        if (warId != null && !warId.isEmpty() && warId.length() == 5) {

            // Initialize the war view
            final Intent showWarIntent = new Intent(this, ShowWarActivity.class);

            TaskCallback callback = new TaskCallback() {

                @Override
                public String getProgressMessage() {
                    return "Joining War ...";
                }

                @Override
                public void onTaskCompleted(ProgressDialog progress, String jsonStr) {

                    if (!jsonStr.isEmpty() && !jsonStr.contains("Invalid War ID")) {

                        // Get the clanInfo object from the JSON string
                        Clan clanInfo = JsonParse.parseWarJson(jsonStr);

                        Log.d(JOINTAG, "<-- CLANINFO -->");
                        Log.d(JOINTAG, clanInfo == null ? "null" : clanInfo.toString());


                        // If clanInfo was loaded successfully, save to the history file
                        LinkedHashMap<String, HashMap<String, String>> histMap;
                        try {

                            // Load the history map
                            histMap = historyController.loadHistory(JoinWarActivity.this);

                            // If the warId isn't already present, add to history
                            if (histMap.get(clanInfo.getGeneral().getWarcode()) == null) {

                                HashMap<String, String> attr = new HashMap<>();

                                SimpleDateFormat format = new SimpleDateFormat("MMM d, yyyy");
                                attr.put("date", format.format(clanInfo.getGeneral().getStarttime()));
                                attr.put("enemy", clanInfo.getGeneral().getEnemyname());
                                attr.put("clan", clanInfo.getGeneral().getClanname());
                                attr.put("size", String.valueOf(clanInfo.getGeneral().getSize()));

                                histMap.put(clanInfo.getGeneral().getWarcode(), attr);

                                historyController.saveHistory(JoinWarActivity.this, histMap);
                            }
                        } catch (IOException e) {
                            Toast tst = Toast.makeText(JoinWarActivity.this, "Could not load history.", Toast.LENGTH_SHORT);
                            tst.setGravity(Gravity.CENTER, 0, 0);
                            tst.show();
                        }

                        // Show the war
                        showWarIntent.putExtra("clan", clanInfo);
                        startActivity(showWarIntent);

                    } else {
                        Toast tst = Toast.makeText(JoinWarActivity.this, "Invalid WarID.  Please try again.", Toast.LENGTH_SHORT);
                        tst.setGravity(Gravity.CENTER, 0, 0);
                        tst.show();
                    }

                    // Dismiss the progress dialog
                    if (progress.isShowing())
                        progress.dismiss();
                }
            };

            // Call the api
            showWarController.getClanInfo(callback, this, warId);

        } else {
            Toast.makeText(this, "Please enter a valid War ID!", Toast.LENGTH_SHORT).show();
        }
    }
}
