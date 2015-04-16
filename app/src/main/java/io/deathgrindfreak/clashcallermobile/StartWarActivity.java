package io.deathgrindfreak.clashcallermobile;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import io.deathgrindfreak.controllers.StartWarController;
import io.deathgrindfreak.util.UrlParameterContainer;


public class StartWarActivity extends ActionBarActivity {

    private Typeface clashFont;
    private UrlParameterContainer<String, String> urlMap;
    private StartWarController startWarController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_war);

        startWarController = new StartWarController(this);

        initListeners();

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initListeners() {
        EditText cNameField = (EditText) findViewById(R.id.clanNameField);
        cNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        EditText eNameField = (EditText) findViewById(R.id.enemyNameField);
        eNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        EditText cIdField = (EditText) findViewById(R.id.clanId);
        cIdField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        EditText eIdField = (EditText) findViewById(R.id.enemyId);
        eIdField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Spinner warSizeSpinner = (Spinner) findViewById(R.id.warSizeSpinner);
        warSizeSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        Spinner timerSpinner = (Spinner) findViewById(R.id.timerSpinner);
        timerSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        CheckBox archiveCB = (CheckBox) findViewById(R.id.archiveCB);
        archiveCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }

    public void submitButtonClicked(View view) {

        String clanName = urlMap.get("cname");
        String enemyClanName = urlMap.get("ename");

        if (clanName == null || clanName.isEmpty()) {
            Toast.makeText(this, "Please fill out the Clan Name field", Toast.LENGTH_SHORT).show();
        } else if (enemyClanName == null || enemyClanName.isEmpty()) {
            Toast.makeText(this, "Please fill out the Enemy Clan Name field", Toast.LENGTH_SHORT).show();
        } else {
            Intent showWarIntent = new Intent(this, ShowWarActivity.class);

            String warId = startWarController.getWarId(R.string.api_url + urlMap.getEncodeURIString());

            if (warId != null && !warId.isEmpty()) {
                showWarIntent.putExtra("cname", clanName);
                showWarIntent.putExtra("ename", enemyClanName);
                showWarIntent.putExtra("size", urlMap.get("size"));
                showWarIntent.putExtra("warId", warId);

                startActivity(showWarIntent);
            } else {
                // TODO handle empty warId
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
