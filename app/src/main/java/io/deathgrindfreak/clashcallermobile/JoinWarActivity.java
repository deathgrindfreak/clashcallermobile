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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import io.deathgrindfreak.clashcallermobile.R;
import io.deathgrindfreak.controllers.StartWarController;
import io.deathgrindfreak.model.Clan;
import io.deathgrindfreak.util.UrlParameterContainer;

public class JoinWarActivity extends ActionBarActivity {

    private Typeface clashFont;
    private StartWarController startWarController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_war);

        startWarController = new StartWarController(this);

        initListeners();

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void initListeners() {

        EditText warCodeField = (EditText) findViewById(R.id.warIdCodeField);
        warCodeField.addTextChangedListener(new TextWatcher() {
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
    }


    public void submitButtonClicked(View view) {

        Intent showWarIntent = new Intent(this, ShowWarActivity.class);

        UrlParameterContainer<String, String> clanInfoUrl =
                new UrlParameterContainer<>(new String[] {"REQUEST", "warcode"});

        clanInfoUrl.put("REQUEST", "GET_FULL_UPDATE");
        clanInfoUrl.put("warcode", warId);

        Clan clanInfo = startWarController.getClanInfo(getResources().getString(R.string.api_url),
                clanInfoUrl.getEncodeURIString());

        if (clanInfo != null) {
            showWarIntent.putExtra("clan", clanInfo);
            startActivity(showWarIntent);
        } else {
            // TODO handle empty Clan
        }
    }
}
