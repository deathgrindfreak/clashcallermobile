package io.deathgrindfreak.clashcallermobile;

import android.content.Intent;
import android.graphics.Typeface;
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

import io.deathgrindfreak.controllers.StartWarController;
import io.deathgrindfreak.model.Clan;
import io.deathgrindfreak.util.UrlParameterContainer;

public class JoinWarActivity extends ActionBarActivity {

    private Typeface clashFont;
    private StartWarController startWarController;

    private static final String JOINTAG = "Join War Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_war);

        startWarController = new StartWarController(this);

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


    private String getWarIdFromField() {
        EditText warField = (EditText) findViewById(R.id.warIdCodeField);
        return warField.getText().toString();
    }


    public void joinSubmitButtonClicked(View view) {

        String warId = getWarIdFromField();

        // TODO check for length requirements for warID
        if (warId != null && !warId.isEmpty()) {

            Intent showWarIntent = new Intent(this, ShowWarActivity.class);

            UrlParameterContainer<String, String> clanInfoUrl =
                    new UrlParameterContainer<>(new String[]{"REQUEST", "warcode"});

            clanInfoUrl.put("REQUEST", "GET_FULL_UPDATE");
            clanInfoUrl.put("warcode", warId);

            Log.i(JOINTAG, "warId passed in join: " + warId);

            try {
                Clan clanInfo = startWarController.getClanInfo(getResources().getString(R.string.api_url),
                        clanInfoUrl.getEncodeURIString());

                Log.d(JOINTAG, "<-- CLANINFO -->");
                Log.d(JOINTAG, clanInfo.toString());

                if (clanInfo != null) {
                    showWarIntent.putExtra("clan", clanInfo);
                    startActivity(showWarIntent);
                } else {
                    // TODO handle empty Clan
                }
            } catch (Exception ex) {
                Toast errorToast = Toast.makeText(this,
                        "Unable to join war, please check the War ID and try again.", Toast.LENGTH_SHORT);

                errorToast.setGravity(Gravity.CENTER, 0, 0);
                errorToast.show();
            }

        } else {
            Toast.makeText(this, "Please enter a valid War ID!", Toast.LENGTH_SHORT).show();
        }
    }
}
