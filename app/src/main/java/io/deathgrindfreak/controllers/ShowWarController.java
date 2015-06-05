package io.deathgrindfreak.controllers;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;

import java.util.Map;

import io.deathgrindfreak.clashcallermobile.JoinWarActivity;
import io.deathgrindfreak.clashcallermobile.R;
import io.deathgrindfreak.clashcallermobile.ShowWarActivity;
import io.deathgrindfreak.clashcallermobile.StartWarActivity;
import io.deathgrindfreak.model.Clan;
import io.deathgrindfreak.util.ApiClassConnector;
import io.deathgrindfreak.util.JsonParse;
import io.deathgrindfreak.util.TaskCallback;
import io.deathgrindfreak.util.UrlParameterContainer;

/**
 * Created by jcbell on 4/15/2015.
 */
public class ShowWarController {

    private ShowWarActivity showWarActivity;
    private JoinWarActivity joinWarActivity;
    private StartWarActivity startWarActivity;

    private static final String SWCTAG = "Show War Controller";

    public ShowWarController() {}

    public ShowWarController(ShowWarActivity showWarActivity) {
        this.showWarActivity = showWarActivity;
    }

    public ShowWarController(JoinWarActivity joinWarActivity) {
        this.joinWarActivity = joinWarActivity;
    }

    public ShowWarController(StartWarActivity startWarActivity) {
        this.startWarActivity = startWarActivity;
    }

    public void getWarId(TaskCallback callback, Context context, Map<String, String> params) {

        // Create url for http param string
        UrlParameterContainer<String, String> urlMap =
            new UrlParameterContainer<>(new String[] {
                "REQUEST", "cname", "ename", "size", "timer",
                "clanid", "enemyid", "searchable"
        });

        urlMap.put("cname", params.get("cname"));
        urlMap.put("ename", params.get("ename"));
        urlMap.put("size", params.get("size"));
        urlMap.put("timer", params.get("timer"));
        urlMap.put("clanid", params.get("clanid"));
        urlMap.put("enemyid", params.get("enemyid"));
        urlMap.put("searchable", params.get("searchable"));

        callApi(callback, context, context.getResources().getString(R.string.api_url),
                urlMap.getEncodeURIString());
    }


    public void getClanInfo(TaskCallback callback, Context context, String warId) {

        Log.d(SWCTAG, "warId passed in getClanInfo: " + warId);

        UrlParameterContainer<String, String> clanInfoUrl =
                new UrlParameterContainer<>(new String[]{"REQUEST", "warcode"});

        clanInfoUrl.put("REQUEST", "GET_FULL_UPDATE");
        clanInfoUrl.put("warcode", warId);

        callApi(callback, context, context.getResources().getString(R.string.api_url),
                clanInfoUrl.getEncodeURIString());
    }


    public void submitClanName(TaskCallback callback, Context context, String warUrl, String posy, String posx, String name) {

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

            callApi(callback, context, showWarActivity
                            .getResources().getString(R.string.api_url),
                    clanNameUrl.getEncodeURIString());
        } else {
            showWarActivity.displaySubmitClanNameToast();
        }
    }

    public void appendCall(TaskCallback callback, Context context, String warUrl, String posy, String name) {

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

            callApi(callback, context, showWarActivity
                            .getResources().getString(R.string.api_url),
                    appendUrl.getEncodeURIString());

        } else {
            showWarActivity.displayAppendCallToast();
        }
    }

    public void setClanMessage(TaskCallback callback, Context context, String warUrl, String note) {

        UrlParameterContainer<String, String> clanMessage =
                new UrlParameterContainer<>(new String[]{"REQUEST", "warcode", "posy", "value"});

        clanMessage.put("REQUEST", "UPDATE_CLAN_MESSAGE");
        clanMessage.put("warcode", warUrl);
        clanMessage.put("value", note);

        Log.d(SWCTAG, "warId passed in setClanMessage: " + warUrl);
        Log.d(SWCTAG, "name passed in setClanMessage: " + note);

        callApi(callback, context, showWarActivity
                        .getResources().getString(R.string.api_url),
                clanMessage.getEncodeURIString());
    }

    public void deleteCall(TaskCallback callback, Context context, String warUrl, String posy, String posx) {


        UrlParameterContainer<String, String> clanMessage =
                new UrlParameterContainer<>(new String[]{"REQUEST", "warcode", "posy", "posx"});

        clanMessage.put("REQUEST", "DELETE_CALL");
        clanMessage.put("warcode", warUrl);
        clanMessage.put("posy", posy);
        clanMessage.put("posx", posx);

        Log.d(SWCTAG, "warId passed in deleteCall: " + warUrl);
        Log.d(SWCTAG, "posy passed in deleteCall: " + posy);
        Log.d(SWCTAG, "posx passed in deleteCall: " + posx);

        callApi(callback, context, showWarActivity
                        .getResources().getString(R.string.api_url),
                clanMessage.getEncodeURIString());
    }

    public void setMemberNote(TaskCallback callback, Context context, String warUrl, String posy, String value) {

        UrlParameterContainer<String, String> clanMessage =
                new UrlParameterContainer<>(new String[]{"REQUEST", "warcode", "posy", "value"});

        clanMessage.put("REQUEST", "UPDATE_TARGET_NOTE");
        clanMessage.put("warcode", warUrl);
        clanMessage.put("posy", posy);
        clanMessage.put("value", value);

        Log.d(SWCTAG, "warId passed in setMemberNote: " + warUrl);
        Log.d(SWCTAG, "posy passed in setMemberNote: " + posy);
        Log.d(SWCTAG, "value passed in setMemberNote: " + value);

        callApi(callback, context, showWarActivity
                        .getResources().getString(R.string.api_url),
                clanMessage.getEncodeURIString());
    }

    public void setAttackNote(TaskCallback callback, Context context, String warUrl, String posy, String posx, String value) {

        UrlParameterContainer<String, String> clanMessage =
                new UrlParameterContainer<>(new String[]{"REQUEST", "warcode", "posy", "posx", "value"});

        clanMessage.put("REQUEST", "UPDATE_CALL_NOTE");
        clanMessage.put("warcode", warUrl);
        clanMessage.put("posy", posy);
        clanMessage.put("posx", posx);
        clanMessage.put("value", value);

        Log.d(SWCTAG, "warId passed in setAttackNote: " + warUrl);
        Log.d(SWCTAG, "posy passed in setAttackNote: " + posy);
        Log.d(SWCTAG, "posx passed in setAttackNote: " + posx);
        Log.d(SWCTAG, "value passed in setAttackNote: " + value);

        callApi(callback, context, showWarActivity .getResources().getString(R.string.api_url),
                clanMessage.getEncodeURIString());
    }

    public void updateMemberStars(TaskCallback callback, Context context, String warUrl, String posy, String posx, String value) {

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

        callApi(callback, context, showWarActivity.getResources().getString(R.string.api_url),
                clanMessage.getEncodeURIString());
    }


    private void callApi(TaskCallback callback, Context context, String url, String parms) {
        new ApiClassConnector(callback, context).execute(url, parms);
    }
}
