package io.deathgrindfreak.clashcallermobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class ClashSettingsActivity extends ActionBarActivity {

    private Typeface clashFont;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clash_settings);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        getSupportActionBar().setIcon(R.mipmap.ic_cclogo);


        // Set the typeface
        clashFont = Typeface.createFromAsset(getAssets(), "Supercell-magic-webfont.ttf");


        // Set the clan name
        TextView clanHeader = (TextView) findViewById(R.id.clanInformation);
        clanHeader.setTypeface(clashFont);

        Button clanButton = (Button) findViewById(R.id.clanNameButton);
        clanButton.setTypeface(clashFont);

        SharedPreferences sharedPref = ClashSettingsActivity.this.getSharedPreferences(
                getString(R.string.clan_name), Context.MODE_PRIVATE);

        String clanName = sharedPref.getString(getResources().getString(R.string.clan_name), "");
        String cText = getResources().getString(R.string.clan_name_info)
                + "\n" + (clanName.isEmpty() ? "Set Clan Name" : clanName);

        Spannable cSpan = new SpannableString(cText);
        cSpan.setSpan(new RelativeSizeSpan(1.5f), 0,
                getResources().getString(R.string.clan_name_info).length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        cSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_grey)),
                getResources().getString(R.string.clan_name_info).length(), cText.length() - 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        clanButton.setText(cSpan);


        // Set the village button
        Button villageButton = (Button) findViewById(R.id.memberNameButton);
        villageButton.setTypeface(clashFont);

        SharedPreferences sharedPrefv = ClashSettingsActivity.this.getSharedPreferences(
                getString(R.string.village_name), Context.MODE_PRIVATE);

        String villageName = sharedPrefv.getString(getResources().getString(R.string.village_name), "");
        String vText = getResources().getString(R.string.village_name_info)
                +  "\n" + (villageName.isEmpty() ? "Set Village Name" : villageName);

        Spannable vSpan = new SpannableString(vText);
        vSpan.setSpan(new RelativeSizeSpan(1.5f), 0,
                getResources().getString(R.string.village_name_info).length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        vSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_grey)),
                getResources().getString(R.string.village_name_info).length(), vText.length() - 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        villageButton.setText(vSpan);


        // Set the clan id
        Button idButton = (Button) findViewById(R.id.idButton);
        idButton.setTypeface(clashFont);

        SharedPreferences sharedPrefid = ClashSettingsActivity.this.getSharedPreferences(
                getString(R.string.id_name), Context.MODE_PRIVATE);

        String idName = sharedPrefid.getString(getResources().getString(R.string.id_name), "");
        String idText = getResources().getString(R.string.id_info)
                +  "\n" + (idName.isEmpty() ? "Set Clan ID" : idName);

        Spannable idSpan = new SpannableString(idText);
        vSpan.setSpan(new RelativeSizeSpan(1.5f), 0,
                getResources().getString(R.string.id_info).length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        vSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_grey)),
                getResources().getString(R.string.id_info).length(), idText.length() - 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        idButton.setText(idSpan);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_clash_settings, menu);
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

        if (id == R.id.home) {
            startActivity(new Intent(this, MainActivity.class));
        }

        if (id == R.id.action_help) {
            startActivity(new Intent(this, HelpActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }


    public void clanNameClicked(View view) {

        final SharedPreferences sharedPref = ClashSettingsActivity.this.getSharedPreferences(
                getString(R.string.clan_name), Context.MODE_PRIVATE);

        String clanName = sharedPref.getString(getResources().getString(R.string.clan_name), "");


        final EditText input = new EditText(ClashSettingsActivity.this);

        if (clanName.isEmpty()) {
            input.setHint("Set the Clan Name.");
        } else {
            input.setText(clanName);
        }

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);

        AlertDialog.Builder alert = new AlertDialog.Builder(ClashSettingsActivity.this)
                .setTitle("Clan Name")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String in = input.getText().toString();

                        if (in == null) {
                            Toast tst = Toast.makeText(ClashSettingsActivity.this,
                                    "Please enter a proper clan name.", Toast.LENGTH_SHORT);
                            tst.setGravity(Gravity.CENTER,0,0);
                            tst.show();
                        } else {

                            String cText = getResources().getString(R.string.clan_name_info) + "\n" + in;

                            Button clanButton = (Button) findViewById(R.id.clanNameButton);
                            clanButton.setText(cText);

                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(getString(R.string.clan_name), in);
                            editor.commit();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });

        alert.setView(input);
        alert.show();
    }

    public void villageNameClicked(View view) {

        final SharedPreferences sharedPref = ClashSettingsActivity.this.getSharedPreferences(
                getString(R.string.village_name), Context.MODE_PRIVATE);

        String villageName = sharedPref.getString(getResources().getString(R.string.village_name), "");


        final EditText input = new EditText(ClashSettingsActivity.this);

        if (villageName.isEmpty()) {
            input.setHint("Set the Village Name.");
        } else {
            input.setText(villageName);
        }

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);

        AlertDialog.Builder alert = new AlertDialog.Builder(ClashSettingsActivity.this)
                .setTitle("Village Name")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String in = input.getText().toString();

                        if (in == null) {
                            Toast tst = Toast.makeText(ClashSettingsActivity.this,
                                    "Please enter a proper village name.", Toast.LENGTH_SHORT);
                            tst.setGravity(Gravity.CENTER,0,0);
                            tst.show();
                        } else {

                            String vText = getResources().getString(R.string.village_name_info) + "\n" + in;

                            Button clanButton = (Button) findViewById(R.id.memberNameButton);
                            clanButton.setText(vText);

                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(getString(R.string.village_name), in);
                            editor.commit();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });

        alert.setView(input);
        alert.show();
    }

    // #VYV9C8Y0

    public void idClicked(View view) {

        final SharedPreferences sharedPref = ClashSettingsActivity.this.getSharedPreferences(
                getString(R.string.id_name), Context.MODE_PRIVATE);

        String idName = sharedPref.getString(getResources().getString(R.string.id_name), "");


        final EditText input = new EditText(ClashSettingsActivity.this);

        if (idName.isEmpty()) {
            input.setHint("Set the Clan ID.");
        } else {
            input.setText(idName);
        }

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);

        AlertDialog.Builder alert = new AlertDialog.Builder(ClashSettingsActivity.this)
                .setTitle("Clan ID")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String in = input.getText().toString();

                        if (in == null) {
                            Toast tst = Toast.makeText(ClashSettingsActivity.this,
                                    "Please enter a proper Clan ID.", Toast.LENGTH_SHORT);
                            tst.setGravity(Gravity.CENTER,0,0);
                            tst.show();
                        } else {

                            String idText = getResources().getString(R.string.id_info) + "\n" + in;

                            Button clanButton = (Button) findViewById(R.id.idButton);
                            clanButton.setText(idText);

                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(getString(R.string.id_name), in);
                            editor.commit();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });

        alert.setView(input);
        alert.show();
    }
}
