package io.deathgrindfreak.clashcallermobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;

import io.deathgrindfreak.controllers.HistoryController;
import io.deathgrindfreak.controllers.ShowWarController;
import io.deathgrindfreak.model.Clan;
import io.deathgrindfreak.util.UrlParameterContainer;


public class StartWarActivity extends ActionBarActivity {

    private Typeface clashFont;
    private UrlParameterContainer<String, String> urlMap;
    private ShowWarController showWarController;
    private HistoryController historyController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_war);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        getSupportActionBar().setIcon(R.mipmap.ic_cclogo);

        showWarController = new ShowWarController();
        historyController = new HistoryController(this);

        setWarSizeCombo();
        setTimerCombo();

        // Set the Clash of Clans font
        clashFont = Typeface.createFromAsset(getAssets(), "Supercell-magic-webfont.ttf");

        // Set the button typeface
        Button submit = (Button) findViewById(R.id.submitButton);
        TextView startWar = (TextView) findViewById(R.id.startWarTextView);
        TextView more = (TextView) findViewById(R.id.optionsTextView);

        submit.setTypeface(clashFont);
        submit.setTextColor(getResources().getColor(R.color.white));
        startWar.setTypeface(clashFont);
        more.setTypeface(clashFont);

        // Create url for http param string
        urlMap = new UrlParameterContainer<>(new String[] {
            "REQUEST", "cname", "ename", "size", "timer",
                "clanid", "enemyid", "searchable"
        });

        // Set defaults for url parameter string
        urlMap.put("REQUEST", "CREATE_WAR");
        urlMap.put("size", "50");
        urlMap.put("timer", "0");
        urlMap.put("searchable", "0");


        // Set the clan text field if clan name is set
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.clan_name), Context.MODE_PRIVATE);
        String clanName = sharedPref.getString(getResources().getString(R.string.clan_name), "");

        if (!clanName.isEmpty()) {
            EditText cNameField = (EditText) findViewById(R.id.clanNameField);
            cNameField.setText(clanName);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_war, menu);
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

    private UrlParameterContainer<String, String> getValuesFromScreen(UrlParameterContainer<String, String> params) {
        EditText cNameField = (EditText) findViewById(R.id.clanNameField);
        EditText eNameField = (EditText) findViewById(R.id.enemyNameField);
        EditText cIdField = (EditText) findViewById(R.id.clanId);
        EditText eIdField = (EditText) findViewById(R.id.enemyId);
        Spinner warSizeSpinner = (Spinner) findViewById(R.id.warSizeSpinner);
        Spinner timerSizeSpinner = (Spinner) findViewById(R.id.timerSpinner);
        CheckBox archiveCB = (CheckBox) findViewById(R.id.archiveCB);

        params.put("cname", cNameField.getText().toString());
        params.put("ename", eNameField.getText().toString());
        params.put("size", getResources().getStringArray(R.array.war_values)[warSizeSpinner.getSelectedItemPosition()]);
        params.put("timer", getResources().getStringArray(R.array.timer_values)[timerSizeSpinner.getSelectedItemPosition()]);

        String cId = cIdField.getText().toString();
        String eId = eIdField.getText().toString();

        if (cId == null || cId.isEmpty()) {
            params.put("clanid", null);
        } else {
            params.put("clanid", cId);
        }

        if (eId == null || eId.isEmpty()) {
            params.put("clanid", null);
        } else {
            params.put("clanid", eId);
        }

        params.put("searchable", (archiveCB.isChecked() ? "1" : "0"));

        return params;
    }

    public void submitButtonClicked(View view) {

        // Get the values from the screen
        urlMap = getValuesFromScreen(urlMap);

        String clanName = urlMap.get("cname");
        String enemyClanName = urlMap.get("ename");

        // Clan name and enemy name must be filled out
        if (clanName == null || clanName.isEmpty()) {
            Toast.makeText(this, "Please fill out the Clan Name field", Toast.LENGTH_SHORT).show();
        } else if (enemyClanName == null || enemyClanName.isEmpty()) {
            Toast.makeText(this, "Please fill out the Enemy Clan Name field", Toast.LENGTH_SHORT).show();
        } else {
            Intent showWarIntent = new Intent(this, ShowWarActivity.class);

            String warId = showWarController.getWarId(this, getResources().getString(R.string.api_url),
                    urlMap.getEncodeURIString());


            // If warId is empty, then an error occurred
            if (!warId.isEmpty()) {

                UrlParameterContainer<String, String> clanInfoUrl =
                        new UrlParameterContainer<>(new String[]{"REQUEST", "warcode"});

                clanInfoUrl.put("REQUEST", "GET_FULL_UPDATE");
                clanInfoUrl.put("warcode", warId.substring(4));

                Clan clanInfo = showWarController.getClanInfo(this, getResources().getString(R.string.api_url),
                        clanInfoUrl.getEncodeURIString());

                if (clanInfo != null) {

                    // If clanInfo was loaded successfully, save to the history file
                    LinkedHashMap<String, HashMap<String, String>> histMap;
                    try {

                        // Load the history map
                        histMap = historyController.loadHistory(this);

                        // If the warId isn't already present, add to history
                        if (histMap.get(clanInfo.getGeneral().getWarcode()) == null) {

                            HashMap<String, String> attr = new HashMap<>();

                            SimpleDateFormat format = new SimpleDateFormat("MMM d, yyyy");
                            attr.put("date", format.format(clanInfo.getGeneral().getStarttime()));
                            attr.put("enemy", clanInfo.getGeneral().getEnemyname());
                            attr.put("clan", clanInfo.getGeneral().getClanname());
                            attr.put("size", String.valueOf(clanInfo.getGeneral().getSize()));

                            histMap.put(clanInfo.getGeneral().getWarcode(), attr);

                            historyController.saveHistory(this, histMap);
                        }
                    } catch (IOException e) {
                        Toast tst = Toast.makeText(this, "Could not load history.", Toast.LENGTH_SHORT);
                        tst.setGravity(Gravity.CENTER, 0, 0);
                        tst.show();
                    }

                    // Show the war
                    showWarIntent.putExtra("clan", clanInfo);
                    startActivity(showWarIntent);

                } else {
                    Toast tst = Toast.makeText(this, "Invalid WarID.  Please try again.", Toast.LENGTH_SHORT);
                    tst.setGravity(Gravity.CENTER, 0, 0);
                    tst.show();
                }
            }
        }
    }

    private void setWarSizeCombo() {
        Spinner spinner = (Spinner) findViewById(R.id.warSizeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.war_sizes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setTimerCombo() {
        Spinner spinner = (Spinner) findViewById(R.id.timerSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.timer_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
