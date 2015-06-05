package io.deathgrindfreak.util;

import io.deathgrindfreak.model.Clan;

/**
 * Created by deathgrindfreak on 6/4/15.
 */
public interface TaskCallback {
    String getProgressMessage();
    void onTaskCompleted(ProgressDialog progress, String returnStr);
}
