package io.deathgrindfreak.clashcaller;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.deathgrindfreak.util.ClashUtil;


public class AboutActivity extends ActionBarActivity {

    private Typeface clashFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        getSupportActionBar().setIcon(R.mipmap.ic_cclogo);

        clashFont = Typeface.createFromAsset(getAssets(), getString(R.string.font));

        TextView aboutTitle = (TextView) findViewById(R.id.aboutTitle);
        aboutTitle.setTypeface(clashFont);

        TextView contactTitle = (TextView) findViewById(R.id.contactTitle);
        contactTitle.setTypeface(clashFont);


        Button redEmail = (Button) findViewById(R.id.redemail);
        redEmail.setTypeface(clashFont);
        redEmail.setTextColor(getResources().getColor(R.color.clanred));
        redEmail.setTextSize(TypedValue.COMPLEX_UNIT_PX, ClashUtil.dptopx(this, getString(R.string.text_size_small)));


        Button deEmail = (Button) findViewById(R.id.deemail);
        deEmail.setTypeface(clashFont);
        deEmail.setTextColor(getResources().getColor(R.color.black));
        deEmail.setTextSize(TypedValue.COMPLEX_UNIT_PX, ClashUtil.dptopx(this, getString(R.string.text_size_small)));


        TextView small1 = (TextView) findViewById(R.id.small1);
        small1.setTextSize(TypedValue.COMPLEX_UNIT_PX, ClashUtil.dptopx(this, getString(R.string.text_size_small)));
        small1.setTypeface(clashFont);
        String txt1 = small1.getText().toString();

        SpannableString cTxt1 = new SpannableString(txt1);
        cTxt1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)),
                "Mobile application by".length(), "Mobile application by deathgrindfreak".length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        cTxt1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)),
                "Mobile application by deathgrindfreak from clan".length(),
                "Mobile application by deathgrindfreak from clan Ganja Jedi!".length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        small1.setText(cTxt1);


        TextView small2 = (TextView) findViewById(R.id.small2);
        small2.setTextSize(TypedValue.COMPLEX_UNIT_PX, ClashUtil.dptopx(this, getString(R.string.text_size_small)));
        small2.setTypeface(clashFont);
        String txt2 = small2.getText().toString();

        SpannableString cTxt2 = new SpannableString(txt2);
        cTxt2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.clanred)),
                "Website, API, art and original concept by".length(),
                "Website, API, art and original concept by RED".length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        cTxt2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.clanred)),
                "Website, API, art and original concept by RED from".length(),
                "Website, API, art and original concept by RED from Clan Hot Sauce!".length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        small2.setText(cTxt2);

        TextView small3 = (TextView) findViewById(R.id.small3);
        small3.setTextSize(TypedValue.COMPLEX_UNIT_PX, ClashUtil.dptopx(this, getString(R.string.text_size_small)));
        small3.setTypeface(clashFont);

        TextView small4 = (TextView) findViewById(R.id.small4);
        small4.setTextSize(TypedValue.COMPLEX_UNIT_PX, ClashUtil.dptopx(this, getString(R.string.text_size_small)));
        small4.setTypeface(clashFont);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_home) {
            startActivity(new Intent(this, MainActivity.class));
        }

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, ClashSettingsActivity.class));
        }

        if (id == R.id.action_help) {
            startActivity(new Intent(this, HelpActivity.class));
        }

        if (id == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    public void redEmailClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "red@clashcaller.com", null));
        startActivity(Intent.createChooser(intent, "Choose an Email client :"));
    }

    public void deEmailClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","john.cooper.bell@gmail.com", null));
        startActivity(Intent.createChooser(intent, "Choose an Email client :"));
    }
}
