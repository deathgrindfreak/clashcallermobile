package io.deathgrindfreak.controllers;

import android.content.Context;
import android.util.Log;

import java.util.concurrent.ExecutionException;

import io.deathgrindfreak.clashcallermobile.R;
import io.deathgrindfreak.clashcallermobile.ShowWarActivity;
import io.deathgrindfreak.model.Clan;
import io.deathgrindfreak.util.ApiClassConnector;
import io.deathgrindfreak.util.JsonParse;
import io.deathgrindfreak.util.UrlParameterContainer;

/**
 * Created by jcbell on 4/15/2015.
 */
public class ShowWarController {

    private ShowWarActivity showWarActivity;

    private static final String SWCTAG = "Show War Controller";

    public ShowWarController() {}

    public ShowWarController(ShowWarActivity showWarActivity) {
        this.showWarActivity = showWarActivity;
    }

    public String getWarId(Context context, String url, String parms) {
        return getReturnString(context, url, parms);
    }


    public Clan getClanInfo(Context context, String url, String parms) {
        String jsonStr = getReturnString(context, url, parms);

        if (jsonStr.isEmpty() || jsonStr.contains("Invalid War ID")) {
            return null;
        } else {
            return JsonParse.parseWarJson(jsonStr);
        }
    }


    public String submitClanName(Context context, String warUrl, String posy, String posx, String name) {

        String clanName = "";

        if (name != null && !name.isEmpty()) {

            UrlParameterContainer<String, String> clanNameUrl =
                    new UrlParameterContainer<>(new String[]{"REQUEST", "warcode", "posy", "posx", "value"});

            clanNameUrl.put("REQUEST", "UPDATE_NAME");
            clanNameUrl.put("warcode", warUrl);
            clanNameUrl.put("posy", posy);
            clanNameUrl.put("posx", posx);
            clanNameUrl.put("value", name);

            Log.d(SWCTAG, "warId passed in submitClanName: " + warUrl);
            Log.d(SWCTAG, "posy passed in submitClanName: " + posy);
            Log.d(SWCTAG, "posx passed in submitClanName: " + posx);
            Log.d(SWCTAG, "name passed in submitClanName: " + name);

            clanName = getReturnString(context, showWarActivity
                            .getResources().getString(R.string.api_url),
                    clanNameUrl.getEncodeURIString());

            Log.d(SWCTAG, "<-- SUBMIT CLAN NAME -->");
            Log.d(SWCTAG, clanName);

        } else {
            showWarActivity.displaySubmitClanNameToast();
        }

        return clanName;
    }

    public String appendCall(Context context, String warUrl, String posy, String name) {

        String appendCall = "";

        if (name != null && !name.isEmpty()) {

            UrlParameterContainer<String, String> appendUrl =
                    new UrlParameterContainer<>(new String[]{"REQUEST", "warcode", "posy", "value"});

            appendUrl.put("REQUEST", "APPEND_CALL");
            appendUrl.put("warcode", warUrl);
            appendUrl.put("posy", posy);
            appendUrl.put("value", name);

            Log.d(SWCTAG, "warId passed in appendCall: " + warUrl);
            Log.d(SWCTAG, "posy passed in appendCall: " + posy);
            Log.d(SWCTAG, "name passed in appendCall: " + name);

            appendCall = getReturnString(context, showWarActivity
                            .getResources().getString(R.string.api_url),
                    appendUrl.getEncodeURIString());

            Log.d(SWCTAG, "<-- APPEND CALL -->");
            Log.d(SWCTAG, appendCall);

        } else {
            showWarActivity.displayAppendCallToast();
        }

        return appendCall;
    }

    public String setClanMessage(Context context, String warUrl, String note) {

        UrlParameterContainer<String, String> clanMessage =
                new UrlParameterContainer<>(new String[]{"REQUEST", "warcode", "posy", "value"});

        clanMessage.put("REQUEST", "UPDATE_CLAN_MESSAGE");
        clanMessage.put("warcode", warUrl);
        clanMessage.put("value", note);

        Log.d(SWCTAG, "warId passed in setClanMessage: " + warUrl);
        Log.d(SWCTAG, "name passed in setClanMessage: " + note);

        String msg = getReturnString(context, showWarActivity
                        .getResources().getString(R.string.api_url),
                clanMessage.getEncodeURIString());

        Log.d(SWCTAG, "<-- CLAN MESSAGE -->");
        Log.d(SWCTAG, msg);

        return msg;
    }

    public String deleteCall(Context context, String warUrl, String posy, String posx) {


        UrlParameterContainer<String, String> clanMessage =
                new UrlParameterContainer<>(new String[]{"REQUEST", "warcode", "posy", "posx"});

        clanMessage.put("REQUEST", "DELETE_CALL");
        clanMessage.put("warcode", warUrl);
        clanMessage.put("posy", posy);
        clanMessage.put("posx", posx);

        Log.d(SWCTAG, "warId passed in deleteCall: " + warUrl);
        Log.d(SWCTAG, "posy passed in deleteCall: " + posy);
        Log.d(SWCTAG, "posx passed in deleteCall: " + posx);

        String msg = getReturnString(context, showWarActivity
                        .getResources().getString(R.string.api_url),
                clanMessage.getEncodeURIString());

        Log.d(SWCTAG, "<-- DELETE MEMBER -->");
        Log.d(SWCTAG, msg);

        return msg;
    }

    public String setMemberNote(Context context, String warUrl, String posy, String value) {

        UrlParameterContainer<String, String> clanMessage =
                new UrlParameterContainer<>(new String[]{"REQUEST", "warcode", "posy", "value"});

        clanMessage.put("REQUEST", "UPDATE_TARGET_NOTE");
        clanMessage.put("warcode", warUrl);
        clanMessage.put("posy", posy);
        clanMessage.put("value", value);

        Log.d(SWCTAG, "warId passed in setMemberNote: " + warUrl);
        Log.d(SWCTAG, "posy passed in setMemberNote: " + posy);
        Log.d(SWCTAG, "value passed in setMemberNote: " + value);

        String msg = getReturnString(context, showWarActivity
                        .getResources().getString(R.string.api_url),
                clanMessage.getEncodeURIString());

        Log.d(SWCTAG, "<-- SET MEMBER NOTE -->");
        Log.d(SWCTAG, msg);

        return msg;
    }


    public String updateMemberStars(Context context, String warUrl, String posy, String posx, String value) {

        UrlParameterContainer<String, String> clanMessage =
                new UrlParameterContainer<>(new String[]{"REQUEST", "warcode", "posx", "posy", "value"});

        clanMessage.put("REQUEST", "UPDATE_STARS");
        clanMessage.put("warcode", warUrl);
        clanMessage.put("posx", posx);
        clanMessage.put("posy", posy);
        clanMessage.put("value", value);

        Log.d(SWCTAG, "warId passed in updateMemberStars: " + warUrl);
        Log.d(SWCTAG, "posx passed in updateMemberStars: " + posx);
        Log.d(SWCTAG, "posy passed in updateMemberStars: " + posy);
        Log.d(SWCTAG, "value passed in updateMemberStars: " + value);

        String msg = getReturnString(context, showWarActivity.getResources().getString(R.string.api_url),
                clanMessage.getEncodeURIString());

        Log.d(SWCTAG, "<-- UPDATE MEMBER STARS -->");
        Log.d(SWCTAG, msg);

        return msg;
    }


    private String getReturnString(Context context, String url , String parms) {
        String returnStr = "";
        try {
            returnStr = new ApiClassConnector(context).execute(url, parms).get();
        } catch (InterruptedException e) {
            Log.e(SWCTAG, e.getMessage());
        } catch (ExecutionException e) {
            Log.e(SWCTAG, e.getMessage());
        }
        return returnStr;
    }
}
