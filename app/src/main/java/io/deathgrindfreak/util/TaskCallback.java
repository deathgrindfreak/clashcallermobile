package io.deathgrindfreak.util;

import android.app.ProgressDialog;


/**
 * Created by deathgrindfreak on 6/4/15.
 */
public interface TaskCallback {
    String getProgressMessage();
    void onTaskCompleted(ProgressDialog progress, String returnStr);
}
