package io.deathgrindfreak.clashcallermobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import io.deathgrindfreak.controllers.StartWarController;
import io.deathgrindfreak.model.Clan;
import io.deathgrindfreak.model.ClanMember;
import io.deathgrindfreak.model.General;
import io.deathgrindfreak.util.UrlParameterContainer;


public class ShowWarActivity extends ActionBarActivity {

    private TableLayout callLayout;
    private Button clanMessage;

    private Typeface clashFont;

    private Clan clanInfo;

    private StartWarController startWarController;

    private static final String SHOWTAG = "Show War Activity";

    private enum NUMBER_COLOR { GOLD, GREY }

    private static final float NUMBER_WEIGHT = 1f;
    private static final float MEMBER_WEIGHT = .01f;
    private static final float X_WEIGHT = 1f;
    private static final float PLUS_WEIGHT = 1f;
    private static final float WEIGHT_SUM =
            NUMBER_WEIGHT + MEMBER_WEIGHT + X_WEIGHT + PLUS_WEIGHT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_war);

        // Create the controller
        if (startWarController == null)
            startWarController = new StartWarController(this);

        clashFont = Typeface.createFromAsset(getAssets(), "Supercell-magic-webfont.ttf");

        if (clanInfo == null) {
            Bundle data = getIntent().getExtras();
            clanInfo = data.getParcelable("clan");
        }

        displayClanInfo(clanInfo);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_war, menu);
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


    private void displayClanInfo(Clan clan) {

        ScrollView mainView = (ScrollView) findViewById(R.id.showWarLayout);

        LinearLayout mainViewChild = new LinearLayout(this);
        mainViewChild.setGravity(Gravity.TOP);
        mainViewChild.setOrientation(LinearLayout.VERTICAL);

        mainViewChild.addView(getGeneralLayout(clan.getGeneral()));
        mainViewChild.addView(getCalls(clan.getCalls()));

        mainView.addView(mainViewChild);
    }


    private LinearLayout getGeneralLayout(General gen) {

        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (10*scale + 0.5f);

        LinearLayout mainGenLayout = new LinearLayout(this);
        mainGenLayout.setGravity(Gravity.TOP);
        mainGenLayout.setOrientation(LinearLayout.VERTICAL);


        // Clan and enemy text
        LinearLayout genLayout = new LinearLayout(this);
        genLayout.setGravity(Gravity.TOP);
        genLayout.setOrientation(LinearLayout.VERTICAL);

        genLayout.setPadding(0, dpAsPixels, 0, dpAsPixels);

        TextView clanNameView = new TextView(this);
        TextView enemyNameView = new TextView(this);
        TextView vs = new TextView(this);

        // Set text
        clanNameView.setText(gen.getClanname());
        enemyNameView.setText(gen.getEnemyname());
        vs.setText(" vs. ");

        // Set text gravity
        clanNameView.setGravity(Gravity.CENTER_HORIZONTAL);
        enemyNameView.setGravity(Gravity.CENTER_HORIZONTAL);
        vs.setGravity(Gravity.CENTER_HORIZONTAL);

        // Set text color
        clanNameView.setTextColor(getResources().getColor(R.color.button_blue));
        enemyNameView.setTextColor(getResources().getColor(R.color.button_blue));

        // Set text size
        clanNameView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.abc_text_size_large_material));
        enemyNameView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.abc_text_size_large_material));
        vs.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.abc_text_size_small_material));

        clanNameView.setTypeface(clashFont);
        enemyNameView.setTypeface(clashFont);
        vs.setTypeface(clashFont);

        genLayout.addView(clanNameView);
        genLayout.addView(vs);
        genLayout.addView(enemyNameView);

        mainGenLayout.addView(genLayout);


        // ClanID
        LinearLayout idLayout = new LinearLayout(this);
        idLayout.setGravity(Gravity.TOP);
        idLayout.setOrientation(LinearLayout.HORIZONTAL);

        idLayout.setGravity(Gravity.CENTER_HORIZONTAL);

        idLayout.setPadding(0, dpAsPixels, 0, dpAsPixels);

        TextView idText = new TextView(this);
        TextView clanID = new TextView(this);

        // Set text
        idText.setText("Your clan's unique ID: ");
        clanID.setText(gen.getWarcode());

        // Set text gravity
        idText.setGravity(Gravity.CENTER_VERTICAL);
        clanID.setGravity(Gravity.CENTER_VERTICAL);

        // Set text color
        clanID.setTextColor(getResources().getColor(R.color.button_blue));

        // Set text size
        idText.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.abc_text_size_medium_material));
        clanID.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.abc_text_size_medium_material));

        clanID.setTypeface(clashFont);

        idLayout.addView(idText);
        idLayout.addView(clanID);

        mainGenLayout.addView(idLayout);


        // Clan message
        clanMessage = new Button(this);
        clanMessage.setPadding(0, dpAsPixels, 0, dpAsPixels);
        clanMessage.setText(gen.getClanmessage());
        clanMessage.setGravity(Gravity.CENTER_HORIZONTAL);
        clanMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.abc_text_size_medium_material));
        clanMessage.setTypeface(clashFont);
        clanMessage.setBackgroundDrawable(null);

        clanMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText input = new EditText(ShowWarActivity.this);
                String msg = clanInfo.getGeneral().getClanmessage();
                String title;

                // If the default message isn't set, just set the hint
                if (msg.equals(getResources().getString(R.string.default_message))) {
                    title = "Set Clan Message";
                    input.setHint("Message");
                } else {
                    title = "Update Clan Message";
                    input.setText(clanInfo.getGeneral().getClanmessage());
                }

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);

                AlertDialog.Builder alert = new AlertDialog.Builder(ShowWarActivity.this)
                        .setTitle(title)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                // Get the value from the input field
                                final String note = input.getText().toString();

                                // Set the value of the button in the app and call API to update
                                setClanMessage(clanInfo.getGeneral().getWarcode(), note);
                                clanMessage.setText(note);
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
        });

        mainGenLayout.addView(clanMessage);

        return mainGenLayout;
    }


    private TableLayout getCalls(ArrayList<ClanMember> members) {

        callLayout = new TableLayout(this);
        callLayout.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT));
        callLayout.setGravity(Gravity.TOP);
        callLayout.setOrientation(TableLayout.VERTICAL);
        //callLayout.setWeightSum(WEIGHT_SUM);

        // Sort members by position
        Collections.sort(members);

        int memInd = 0;
        ArrayList<ClanMember> memberList = new ArrayList<ClanMember>();
        for (int i = 0; i < clanInfo.getGeneral().getSize(); i++) {

            // Add all members belonging to a call to the member list
            while (memInd < members.size() && members.get(memInd).getPosy() == i) {
                memberList.add(members.get(memInd));
                memInd++;
            }

            // Add the members to the table
            if (!memberList.isEmpty()) {
                for (ClanMember mem : memberList)
                    callLayout.addView(getMembersLayout(i, mem));

            } else {
                callLayout.addView(getMembersLayout(i, null));
            }

            memberList = new ArrayList<ClanMember>();
        }

        return callLayout;
    }


    private TableRow getMembersLayout(int row, ClanMember member) {

        final TableRow rowLayout = new TableRow(this);
        rowLayout.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        // Set the tag with the clan member
        rowLayout.setTag(member == null ? row : member);

        // Set the background color
        if (row % 2 == 0)
            rowLayout.setBackgroundColor(getResources().getColor(R.color.light_grey));


        // TODO Add stars in front of comment (keep alignment the same)

        // TODO Then add Comment button


        // If member is null, row hasn't been claimed yet (set to grey)
        if (member == null) {
            Button num = makeNumberButton(row, NUMBER_COLOR.GREY);
            num.setId(R.id.numButton);
            rowLayout.addView(num);
        } else if (member.getPosx() == 0) {
            Button num = makeNumberButton(row, NUMBER_COLOR.GOLD);
            num.setId(R.id.numButton);
            rowLayout.addView(num);
        } else {
            Button n = makeSpacer(NUMBER_WEIGHT);

            // Set the background color
            if (row % 2 == 0)
                n.setBackgroundColor(getResources().getColor(R.color.light_grey));

            rowLayout.addView(n);
        }

        // Add Clan members
        if (member == null) {
            Button m = makeSpacer(MEMBER_WEIGHT);
            Button x = makeSpacer(X_WEIGHT);

            // Set the background color
            if (row % 2 == 0) {
                m.setBackgroundColor(getResources().getColor(R.color.light_grey));
                x.setBackgroundColor(getResources().getColor(R.color.light_grey));
            }

            rowLayout.addView(m);
            rowLayout.addView(x);

        } else {
            Button memB = makeMemberButton(member);
            memB.setId(R.id.memButton);

            Button x = makeXButton(member);

            // Set the background color
            if (row % 2 == 0)
                x.setBackgroundColor(getResources().getColor(R.color.light_grey));

            rowLayout.addView(memB);
            rowLayout.addView(x);
        }

        // Add + button
        if (member == null || member.getPosx() == 0) {

            Button plus = makePlusButton(row, member);

            // Set the background color
            if (row % 2 == 0)
                plus.setBackgroundColor(getResources().getColor(R.color.light_grey));

            rowLayout.addView(plus);

        } else {
            Button s = makeSpacer(PLUS_WEIGHT);

            // Set the background color
            if (row % 2 == 0)
                s.setBackgroundColor(getResources().getColor(R.color.light_grey));

            rowLayout.addView(s);
        }

        return rowLayout;
    }


    private Button makeNumberButton(int callNumber, NUMBER_COLOR color) {

        Button numberButton = new Button(this);
        numberButton.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT, NUMBER_WEIGHT));
        numberButton.setGravity(Gravity.LEFT);
        numberButton.setText((callNumber + 1) + ".");
        numberButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.abc_text_size_medium_material));
        numberButton.setTypeface(clashFont);
        numberButton.setBackgroundDrawable(null);

        switch (color) {
            case GOLD:
                numberButton.setTextColor(getResources().getColor(R.color.number_gold));
                break;
            case GREY:
                numberButton.setTextColor(getResources().getColor(R.color.number_grey));
                break;
        }

        // TODO set a listener here

        return numberButton;
    }


    private Button makeSpacer(float weight) {

        Button butt = new Button(this);
        butt.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT, weight));
        butt.setWidth(0);

        return butt;
    }


    private Button makeMemberButton(final ClanMember mem) {

        Button memButton = new Button(this);
        memButton.setGravity(Gravity.LEFT);
        memButton.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT, MEMBER_WEIGHT));
        memButton.setWidth(0);
        memButton.setText(mem.getPlayername());
        memButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.abc_text_size_small_material));
        memButton.setTypeface(clashFont);
        memButton.setTextColor(getResources().getColor(R.color.button_blue));
        memButton.setBackgroundDrawable(null);
        memButton.setEllipsize(TextUtils.TruncateAt.END);
        memButton.setSingleLine(true);

        memButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText input = new EditText(ShowWarActivity.this);
                input.setHint("New Call");
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);

                AlertDialog.Builder alert = new AlertDialog.Builder(ShowWarActivity.this)
                        .setTitle("Change Call To:")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                String newName = input.getText().toString();

                                if (newName != null && !newName.isEmpty()) {

                                    // Call the api
                                    submitClanName(clanInfo.getGeneral().getWarcode(),
                                            String.valueOf(mem.getPosy()), String.valueOf(mem.getPosx()), newName);

                                    // Find the table row
                                    TableRow row = (TableRow) callLayout.findViewWithTag(mem);
                                    int index = callLayout.indexOfChild(row);

                                    Button memB = (Button) row.findViewById(R.id.memButton);
                                    memB.setText(newName);

                                } else {
                                    Toast.makeText(ShowWarActivity.this, "Please enter a valid name!", Toast.LENGTH_SHORT);
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
        });

        return memButton;
    }


    private Button makePlusButton(final int row, final ClanMember member) {

        Drawable plus = getResources().getDrawable(R.drawable.add);
        plus.setBounds(0, 0, (int) (plus.getIntrinsicWidth() * 0.8),
                (int) (plus.getIntrinsicHeight() * 0.8));
        ScaleDrawable sd = new ScaleDrawable(plus, 0, .8f, .8f);

        Button plusButton = new Button(this);
        plusButton.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT, PLUS_WEIGHT));
        plusButton.setWidth(0);
        plusButton.setGravity(Gravity.CENTER);
        //plusButton.setBackgroundDrawable(plus);
        plusButton.setCompoundDrawables(sd.getDrawable(), null, null, null);

        plusButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final EditText input = new EditText(ShowWarActivity.this);
                input.setHint("Your Name");
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);

                AlertDialog.Builder alert = new AlertDialog.Builder(ShowWarActivity.this)
                        .setTitle("Call Target #" + (row + 1))
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

                                String name = input.getText().toString();

                                if (name != null && !name.isEmpty()) {

                                    // Call the api
                                    appendCall(clanInfo.getGeneral().getWarcode(), String.valueOf(row), name);

                                    // Set the new player name
                                    ClanMember mem = new ClanMember();
                                    mem.setPlayername(name);
                                    mem.setPosy(row);


                                    if (member != null) {

                                        // Find the table row
                                        TableRow rowL = (TableRow) callLayout.findViewWithTag(member);
                                        int index = callLayout.indexOfChild(rowL);

                                        mem.setPosx(member.getPosx() + 1);

                                        Log.d(SHOWTAG, "row: " + row + " New Row: " + (getLastMemberIndex(index) + 1));

                                        // Add to the clan view
                                        callLayout.addView(getMembersLayout(row, mem), getLastMemberIndex(index) + 1);

                                    } else {

                                        // Find the table row
                                        TableRow rowL = (TableRow) callLayout.findViewWithTag(row);
                                        int index = callLayout.indexOfChild(rowL);

                                        mem.setPosx(0);

                                        // Add to the clan view
                                        callLayout.removeView(rowL);
                                        callLayout.addView(getMembersLayout(row, mem), index);
                                    }

                                    // Add the clan member to the clan array
                                    clanInfo.addClanMember(mem);

                                } else {
                                    Toast.makeText(ShowWarActivity.this, "Please enter a valid name!", Toast.LENGTH_SHORT);
                                }

                                // TODO might need to refresh the log after this is called
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
        });

        return plusButton;
    }


    private Button makeXButton(final ClanMember member) {

        Drawable x = getResources().getDrawable(R.drawable.x_grey);
        x.setBounds(0, 0, (int) (x.getIntrinsicWidth() * 0.6),
                (int) (x.getIntrinsicHeight() * 0.6));
        ScaleDrawable sd = new ScaleDrawable(x, 0, .6f, .6f);

        Button xButton = new Button(this);
        xButton.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT, X_WEIGHT));
        xButton.setWidth(0);
        //xButton.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
        xButton.setGravity(Gravity.LEFT);
        xButton.setCompoundDrawables(sd.getDrawable(), null, null, null);
        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(ShowWarActivity.this)
                        .setTitle("Delete " + member.getPlayername() + "'s Call?")
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                // Call the api
                                deleteCall(clanInfo.getGeneral().getWarcode(),
                                        String.valueOf(member.getPosy()), String.valueOf(member.getPosx()));

                                // Find the table row
                                TableRow row = (TableRow) callLayout.findViewWithTag(member);
                                int index = callLayout.indexOfChild(row);

                                // Get the number button
                                Button numButton = (Button) row.findViewById(0);

                                // Delete from layout
                                callLayout.removeView(row);

                                // If member was the top call, add an empty call row back
                                if (member.getPosx() == 0) {
                                    callLayout.addView(getMembersLayout(member.getPosy(), null), index);
                                }

                                // Remove from clan calls
                                clanInfo.removeClanMember(member);
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
        // TODO set a listener here

        return xButton;
    }


    private int getLastMemberIndex(int row) {

        ArrayList<ClanMember> mems = clanInfo.getCalls();
        Collections.sort(mems);

        int lastrow = row;
        while (mems.get(lastrow).getPosy() == row)
            ++lastrow;

        return lastrow;
    }


    private void submitClanName(String warUrl, String posy, String posx, String name) {

        if (name != null && !name.isEmpty()) {

            UrlParameterContainer<String, String> clanNameUrl =
                    new UrlParameterContainer<>(new String[]{"REQUEST", "warcode", "posy", "posx", "value"});

            clanNameUrl.put("REQUEST", "UPDATE_NAME");
            clanNameUrl.put("warcode", warUrl);
            clanNameUrl.put("posy", posy);
            clanNameUrl.put("posx", posx);
            clanNameUrl.put("value", name);

            Log.d(SHOWTAG, "warId passed in submitClanName: " + warUrl);
            Log.d(SHOWTAG, "posy passed in submitClanName: " + posy);
            Log.d(SHOWTAG, "posx passed in submitClanName: " + posx);
            Log.d(SHOWTAG, "name passed in submitClanName: " + name);

            String clanName = startWarController.submitCallName(getResources().getString(R.string.api_url),
                    clanNameUrl.getEncodeURIString());

            Log.d(SHOWTAG, "<-- SUBMIT CLAN NAME -->");
            Log.d(SHOWTAG, clanName);

        } else {
            Toast.makeText(this, "Call name cannot be empty!", Toast.LENGTH_SHORT).show();
        }
    }

    private void appendCall(String warUrl, String posy, String name) {

        if (name != null && !name.isEmpty()) {

            UrlParameterContainer<String, String> appendUrl =
                    new UrlParameterContainer<>(new String[]{"REQUEST", "warcode", "posy", "value"});

            appendUrl.put("REQUEST", "APPEND_CALL");
            appendUrl.put("warcode", warUrl);
            appendUrl.put("posy", posy);
            appendUrl.put("value", name);

            Log.d(SHOWTAG, "warId passed in appendCall: " + warUrl);
            Log.d(SHOWTAG, "posy passed in appendCall: " + posy);
            Log.d(SHOWTAG, "name passed in appendCall: " + name);

            String appendCall = startWarController.appendCall(getResources().getString(R.string.api_url),
                    appendUrl.getEncodeURIString());

            Log.d(SHOWTAG, "<-- APPEND CALL -->");
            Log.d(SHOWTAG, appendCall);

        } else {
            Toast.makeText(this, "Call name cannot be empty!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setClanMessage(String warUrl, String note) {


        UrlParameterContainer<String, String> clanMessage =
                new UrlParameterContainer<>(new String[]{"REQUEST", "warcode", "posy", "value"});

        clanMessage.put("REQUEST", "UPDATE_CLAN_MESSAGE");
        clanMessage.put("warcode", warUrl);
        clanMessage.put("value", note);

        Log.d(SHOWTAG, "warId passed in setClanMessage: " + warUrl);
        Log.d(SHOWTAG, "name passed in setClanMessage: " + note);

        String msg = startWarController.setClanMessage(getResources().getString(R.string.api_url),
                clanMessage.getEncodeURIString());

        Log.d(SHOWTAG, "<-- CLAN MESSAGE -->");
        Log.d(SHOWTAG, msg);
    }

    private void deleteCall(String warUrl, String posy, String posx) {


        UrlParameterContainer<String, String> clanMessage =
                new UrlParameterContainer<>(new String[]{"REQUEST", "warcode", "posy", "posx"});

        clanMessage.put("REQUEST", "DELETE_CALL");
        clanMessage.put("warcode", warUrl);
        clanMessage.put("posy", posy);
        clanMessage.put("posx", posx);

        Log.d(SHOWTAG, "warId passed in deleteCall: " + warUrl);
        Log.d(SHOWTAG, "posy passed in deleteCall: " + posy);
        Log.d(SHOWTAG, "posx passed in deleteCall: " + posx);

        String msg = startWarController.deleteCall(getResources().getString(R.string.api_url),
                clanMessage.getEncodeURIString());

        Log.d(SHOWTAG, "<-- DELETE MEMBER -->");
        Log.d(SHOWTAG, msg);
    }
}
