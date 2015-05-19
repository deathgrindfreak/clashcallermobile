package io.deathgrindfreak.clashcallermobile;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    private Typeface clashFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the Clash of Clans font
        clashFont = Typeface.createFromAsset(getAssets(), "Supercell-magic-webfont.ttf");

        // Set the button typeface
        Button swarButton = (Button) findViewById(R.id.startWarButton);
        Button jwarButton = (Button) findViewById(R.id.joinWarButton);

        // TODO make visible when search has been implemented
        Button search = (Button) findViewById(R.id.searchButton);
        search.setVisibility(View.GONE);


        Button about = (Button) findViewById(R.id.aboutButton);

        swarButton.setTypeface(clashFont);
        jwarButton.setTypeface(clashFont);
        search.setTypeface(clashFont);
        about.setTypeface(clashFont);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void startWarButtonClicked(View view) {
        Intent startWarIntent = new Intent(this, StartWarActivity.class);
        startActivity(startWarIntent);
    }

    public void joinWarButtonClicked(View view) {
        Intent joinWarIntent = new Intent(this, JoinWarActivity.class);
        startActivity(joinWarIntent);
    }

    public void searchButtonClicked(View view) {
        Intent searchIntent = new Intent(this, SearchClanActivity.class);
        startActivity(searchIntent);
    }

    public void aboutButtonClicked(View view) {
        Intent aboutIntent = new Intent(this, AboutActivity.class);
        startActivity(aboutIntent);
    }
}
