package io.deathgrindfreak.clashcallermobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class HelpActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        getSupportActionBar().setIcon(R.mipmap.ic_cclogo);

        // Open the wiki
        final WebView myWebView = (WebView) findViewById(R.id.helpWebView);
        myWebView.setWebViewClient(new WebViewClient());

        // Start an AsyncTask so that the progress dialog shows up
        new AsyncTask<String, Void, Void> {

            @Override
            protected onPreExecute() {
                ProgressDialog progress = new ProgressDialog(HelpActivity.this);
                progress.setMessage("Loading Help ...");
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.show();
            }

            @Override
            protected void doInBackground(String... url) {
                myWebView.loadUrl(url[0]);
            }

            @Override
            protected void onPostExecute() {
                if (progress.isShowing())
                    progress.dismiss();
            }
        }.execute("http://github.com/deathgrindfreak/clashcallermobile/blob/master/HELP.md")
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_help, menu);
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

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, HelpActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
