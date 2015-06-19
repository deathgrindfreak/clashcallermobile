package io.deathgrindfreak.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by deathgrindfreak on 6/18/15.
 */
public class ClashUtil {

    public static int dptopx(Context ctxt, int dp) {
        DisplayMetrics m = ctxt.getResources().getDisplayMetrics();
        return dp * (m.densityDpi / 160);
    }

    public static int dptopx(Context ctxt, String dp) {
        return dptopx(ctxt, new Integer(dp));
    }
}
