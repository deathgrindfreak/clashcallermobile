package io.deathgrindfreak.clashcallermobile;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import io.deathgrindfreak.controllers.HistoryController;
import io.deathgrindfreak.controllers.ShowWarController;
import io.deathgrindfreak.model.Clan;
import io.deathgrindfreak.util.UrlParameterContainer;


public class HistoryActivity extends ActionBarActivity {

    private TableLayout historyLayout;

    private Typeface clashFont;

    private HistoryController historyController;
    private ShowWarController showWarController;

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
            //histMap = historyController.loadHistory();


            // TODO remove test data
            histMap = getExampleData();

            Log.d(HISTTAG, "histMap: " + histMap);

            // If the history map is empty, show the error display
            if (histMap.isEmpty()) {
                displayErrorView();
            } else {
                displayHistoryLayout(histMap);
            }

            //} catch (IOException e) {
        } catch (Exception e) {
            Log.e(HISTTAG, "IOException: " + e.getMessage());

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
            return true;
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

        LinearLayout histHolder = new LinearLayout(this);
        histHolder.setOrientation(LinearLayout.VERTICAL);
        histHolder.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        scrollLayout.addView(histHolder);

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
        histHolder.addView(makeHistoryTable(histMap));
    }


    private TableLayout makeHistoryTable(LinkedHashMap<String, HashMap<String, String>> histMap) {

        TableLayout historyTable = new TableLayout(this);
        historyTable.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT));
        historyTable.setGravity(Gravity.TOP);
        historyTable.setOrientation(TableLayout.VERTICAL);

        // Add the header row
        historyTable.addView(makeTableHeader());

        int color, row = 0;
        for (Map.Entry<String, HashMap<String, String>> entry : histMap.entrySet()) {

            TableRow tblRow = makeTableRow(entry.getKey(), entry.getValue());

            // Set the row color
            color = row % 2 == 0 ? R.color.light_grey : R.color.white;
            tblRow.setBackgroundColor(getResources().getColor(color));
            row++;

            historyTable.addView(tblRow);
        }

        return historyTable;
    }


    private TableRow makeTableHeader() {

        TableRow headers = new TableRow(this);
        headers.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        for (String header : tableHeaders) {
            TextView hdr = new TextView(this);

            if (header.equals("warId")) {
                hdr.setLayoutParams(new TableRow.LayoutParams(historyController.dptopx(LINK_WIDTH),
                        TableRow.LayoutParams.WRAP_CONTENT));
            } else {
                hdr.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            }

            hdr.setTypeface(clashFont);
            hdr.setTextColor(getResources().getColor(R.color.button_blue));
            hdr.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.abc_text_size_small_material));
            hdr.setText(header);

            headers.addView(hdr);
        }

        return headers;
    }


    private TableRow makeTableRow(final String warId, HashMap<String, String> attrs) {

        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));


        Button warIdLink = new Button(this);
        warIdLink.setLayoutParams(new TableRow.LayoutParams(historyController.dptopx(LINK_WIDTH),
                TableRow.LayoutParams.WRAP_CONTENT));
        warIdLink.setTypeface(clashFont);
        warIdLink.setBackgroundColor(getResources().getColor(R.color.button_blue));
        warIdLink.setTextColor(getResources().getColor(R.color.white));
        warIdLink.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.abc_text_size_small_material));
        warIdLink.setText(warId);

        // Set the click listener
        warIdLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // Initialize the war view
                Intent showWarIntent = new Intent(HistoryActivity.this, ShowWarActivity.class);

                UrlParameterContainer<String, String> clanInfoUrl =
                        new UrlParameterContainer<>(new String[]{"REQUEST", "warcode"});

                clanInfoUrl.put("REQUEST", "GET_FULL_UPDATE");
                clanInfoUrl.put("warcode", warId);

                Log.i(HISTTAG, "warId passed in join: " + warId);

                Clan clanInfo = showWarController.getClanInfo(HistoryActivity.this,
                        getResources().getString(R.string.api_url),
                        clanInfoUrl.getEncodeURIString());

                Log.d(HISTTAG, "<-- CLANINFO -->");
                Log.d(HISTTAG, clanInfo == null ? "null" : clanInfo.toString());

                if (clanInfo != null) {
                    showWarIntent.putExtra("clan", clanInfo);
                    startActivity(showWarIntent);
                    finish();
                }
            }
        });


        TextView enemy = new TextView(this);
        enemy.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        enemy.setTypeface(clashFont);
        enemy.setTextColor(getResources().getColor(R.color.number_grey));
        enemy.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.abc_text_size_small_material));
        enemy.setText(attrs.get("enemy"));
        enemy.setEllipsize(TextUtils.TruncateAt.END);
        enemy.setSingleLine(true);


        TextView date = new TextView(this);
        date.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        date.setTypeface(clashFont);
        date.setTextColor(getResources().getColor(R.color.number_grey));
        date.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.abc_text_size_small_material));
        date.setText(attrs.get("date"));


        row.addView(warIdLink);
        row.addView(enemy);
        row.addView(date);

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
