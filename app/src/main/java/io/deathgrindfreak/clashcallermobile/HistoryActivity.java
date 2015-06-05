package io.deathgrindfreak.clashcallermobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import io.deathgrindfreak.controllers.HistoryController;
import io.deathgrindfreak.controllers.ShowWarController;
import io.deathgrindfreak.model.Clan;
import io.deathgrindfreak.util.JsonParse;
import io.deathgrindfreak.util.TaskCallback;
import io.deathgrindfreak.util.UrlParameterContainer;


public class HistoryActivity extends ActionBarActivity {

    private TableLayout historyLayout;

    private Typeface clashFont;

    private HistoryController historyController;
    private ShowWarController showWarController;

    private LinearLayout histHolder;

    private LinkedHashMap<String, HashMap<String, String>> histMap;

    private static final String HISTTAG = "History Activity";

    private static final String[] tableHeaders = { "WarID", "Enemy Clan", "Date" };

    private static final int LINK_WIDTH = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Display the logo in the actionbar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        getSupportActionBar().setIcon(R.mipmap.ic_cclogo);

        clashFont = Typeface.createFromAsset(getAssets(), "Supercell-magic-webfont.ttf");

        historyController = new HistoryController(this);
        showWarController = new ShowWarController();


        // Display the main view
        try {

            histMap = historyController.loadHistory(this);

            // TODO remove test data
            //histMap = getExampleData();

            Log.d(HISTTAG, "histMap: " + histMap);

            // If the history map is empty, show the error display
            if (histMap.isEmpty()) {
                displayErrorView();
            } else {
                displayHistoryLayout(histMap);
            }

        } catch (IOException e) {
            Log.e(HISTTAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(HISTTAG, "Exception: " + e.getMessage());

            Toast tst = Toast.makeText(this, "There was an error loading the history file.", Toast.LENGTH_SHORT);
            tst.setGravity(Gravity.CENTER, 0, 0);
            tst.show();

            displayErrorView();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
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


    private void displayErrorView() {

        LinearLayout mainView = (LinearLayout) findViewById(R.id.historyLayout);

        // Set the main title
        TextView errTitle = new TextView(this);
        errTitle.setGravity(Gravity.CENTER);
        errTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.abc_text_size_small_material));
        errTitle.setTextColor(getResources().getColor(R.color.light_grey));
        errTitle.setText("Nothing to display.");

        mainView.addView(errTitle);
    }


    private void displayHistoryLayout(LinkedHashMap<String, HashMap<String, String>> histMap) {

        LinearLayout mainView = (LinearLayout) findViewById(R.id.historyLayout);

        ScrollView scrollLayout = new ScrollView(this);
        scrollLayout.setLayoutParams(new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT,
                ScrollView.LayoutParams.MATCH_PARENT));

        mainView.addView(scrollLayout);

        scrollLayout.addView(makeHistoryLayout(histMap));

    }


    private LinearLayout makeHistoryLayout(LinkedHashMap<String, HashMap<String, String>> histMap) {

        histHolder = new LinearLayout(this);
        histHolder.setOrientation(LinearLayout.VERTICAL);
        histHolder.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        histHolder.setBackgroundColor(getResources().getColor(R.color.white));

        // Set the main title
        TextView histTitle = new TextView(this);
        histTitle.setPadding(0,10,0,60);
        histTitle.setTypeface(clashFont);
        histTitle.setTextColor(getResources().getColor(R.color.button_blue));
        histTitle.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
        histTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.abc_text_size_large_material));
        histTitle.setText("War History");

        histHolder.addView(histTitle);


        // Add rows in reverse order
        List<String> keys = new ArrayList<String>(histMap.keySet());

        for (int i = keys.size() - 1; i >= 0; i--) {
            System.out.println("key: " + keys.get(i));
            histHolder.addView(makeTableRow(keys.get(i), histMap.get(keys.get(i))));
        }

        return histHolder;
    }


    private LinearLayout makeTableRow(final String warId, HashMap<String, String> attrs) {

        Log.d(HISTTAG, "warID: " + warId);
        Log.d(HISTTAG, "enemy: " + attrs.get("enemy"));
        Log.d(HISTTAG, "clan: " + attrs.get("clan"));
        Log.d(HISTTAG, "date: " + attrs.get("date"));


        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.VERTICAL);
        row.setPadding(historyController.dptopx(10), 0,
                historyController.dptopx(10), historyController.dptopx(10));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, historyController.dptopx(10), 0, historyController.dptopx(10));
        row.setLayoutParams(params);

        // Set the tag
        row.setTag(warId);

        // Set the background color
        row.setBackgroundColor(getResources().getColor(R.color.light_grey));

        // Set the click listener
        row.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                TaskCallback callback = new TaskCallback() {
                    @Override
                    public void onTaskCompleted(String jsonStr) {

                        if (!jsonStr.isEmpty() && !jsonStr.contains("Invalid War ID")) {

                            // Initialize the war view
                            Intent showWarIntent = new Intent(HistoryActivity.this, ShowWarActivity.class);

                            // Get the clanInfo object from the JSON string
                            Clan clanInfo = JsonParse.parseWarJson(jsonStr);

                            Log.d(HISTTAG, "<-- CLANINFO -->");
                            Log.d(HISTTAG, clanInfo == null ? "null" : clanInfo.toString());

                            if (clanInfo != null) {
                                showWarIntent.putExtra("clan", clanInfo);
                                startActivity(showWarIntent);
                            } else {

                                String errorStr = "The War with ID \"" + warId + "\" could not be loaded." +
                                        "The war may have simply expired, would you like to delete it from your history?";

                                AlertDialog.Builder alert = new AlertDialog.Builder(HistoryActivity.this)
                                        .setTitle(errorStr)
                                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                LinearLayout row = (LinearLayout) histHolder.findViewWithTag(warId);
                                                histHolder.removeView(row);

                                                histMap.remove(warId);

                                                try {
                                                    historyController.saveHistory(HistoryActivity.this, histMap);
                                                } catch (IOException e) {
                                                    Log.e(HISTTAG, "Couldn't save histMap: " + e.getMessage());

                                                    Toast tst = Toast.makeText(HistoryActivity.this, "Could not save history.", Toast.LENGTH_SHORT);
                                                    tst.setGravity(Gravity.CENTER, 0, 0);
                                                    tst.show();
                                                }

                                            }
                                        })
                                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // do nothing
                                            }
                                        });

                                alert.show();
                            }
                        }
                    }
                };

                showWarController.getClanInfo(callback, HistoryActivity.this, warId);
            }
        });


        LinearLayout buttonLayout = new LinearLayout(this);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));


        TextView warIdLink = new TextView(this);
        warIdLink.setPadding(0, historyController.dptopx(10), 0, historyController.dptopx(10));
        warIdLink.setTypeface(clashFont);
        warIdLink.setGravity(Gravity.LEFT);
        warIdLink.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 10f));
        warIdLink.setTextColor(getResources().getColor(R.color.button_blue));
        warIdLink.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.abc_text_size_small_material));

        Spannable warStr = new SpannableString("WarID: " + warId);
        warStr.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.number_grey)),
                0, "WarID: ".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        warStr.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.button_blue)),
                "WarID: ".length(), warStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        warIdLink.setText(warStr);

        buttonLayout.addView(warIdLink);


        LinearLayout xLayout = new LinearLayout(this);
        xLayout.setOrientation(LinearLayout.HORIZONTAL);
        xLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        xLayout.setGravity(Gravity.RIGHT);

        ImageButton xButton = new ImageButton(this);
        xButton.setImageResource(R.drawable.x_grey);
        xButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        xButton.setLayoutParams(new LinearLayout.LayoutParams(historyController.dptopx(50),
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        xButton.setBackgroundColor(getResources().getColor(R.color.light_grey));

        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert = new AlertDialog.Builder(HistoryActivity.this)
                        .setTitle("Delete War with ID \"" + warId + "\" from history?")
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                LinearLayout row = (LinearLayout) histHolder.findViewWithTag(warId);
                                histHolder.removeView(row);

                                histMap.remove(warId);

                                try {
                                    historyController.saveHistory(HistoryActivity.this, histMap);
                                } catch (IOException e) {
                                    Log.e(HISTTAG, "Couldn't save histMap: " + e.getMessage());

                                    Toast tst = Toast.makeText(HistoryActivity.this, "Could not save history.", Toast.LENGTH_SHORT);
                                    tst.setGravity(Gravity.CENTER, 0, 0);
                                    tst.show();
                                }

                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        });

                alert.show();
            }
        });

        //xLayout.addView(xButton);

        buttonLayout.addView(xButton);


        TextView enemy = new TextView(this);
        enemy.setTypeface(clashFont);
        enemy.setTextColor(getResources().getColor(R.color.number_grey));
        enemy.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.abc_text_size_small_material));
        enemy.setText(attrs.get("clan") + " vs. " + attrs.get("enemy"));
        enemy.setEllipsize(TextUtils.TruncateAt.END);
        enemy.setSingleLine(true);


        TextView date = new TextView(this);
        date.setTypeface(clashFont);
        date.setTextColor(getResources().getColor(R.color.number_grey));
        date.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.abc_text_size_small_material));
        date.setText("Start Date: " + attrs.get("date"));

        TextView size = new TextView(this);
        size.setTypeface(clashFont);
        size.setTextColor(getResources().getColor(R.color.number_grey));
        size.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.abc_text_size_small_material));
        size.setText("Size: " + attrs.get("size") + " x " + attrs.get("size"));


        row.addView(buttonLayout);
        row.addView(enemy);
        row.addView(date);
        row.addView(size);

        return row;
    }


    // TODO remove test data
    private LinkedHashMap<String, HashMap<String, String>> getExampleData() {

        LinkedHashMap<String, HashMap<String, String>> hist =
                new LinkedHashMap<>();

        HashMap<String, String> attr1 = new HashMap<>();
        attr1.put("enemy", "Manaus Team Dumbass");
        attr1.put("date", "Mar 7 2014");
        hist.put("g7uwm", attr1);


        HashMap<String, String> attr2 = new HashMap<>();
        attr2.put("enemy", "Arm Angel");
        attr2.put("date", "Dec 12 2015");
        hist.put("cjrw7", attr2);


        return hist;
    }
}
