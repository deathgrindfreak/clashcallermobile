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

import io.deathgrindfreak.controllers.StartWarController;
import io.deathgrindfreak.model.Clan;
import io.deathgrindfreak.model.ClanMember;
import io.deathgrindfreak.model.General;
import io.deathgrindfreak.util.UrlParameterContainer;


public class ShowWarActivity extends ActionBarActivity {

    private Button clanMessage;

    private Typeface clashFont;

    private Clan clanInfo;

    private StartWarController startWarController;

    private static final String SHOWTAG = "Show War Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_war);

        startWarController = new StartWarController(this);

        clashFont = Typeface.createFromAsset(getAssets(), "Supercell-magic-webfont.ttf");

        Bundle data = getIntent().getExtras();
        clanInfo = data.getParcelable("clan");

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

    private TableLayout getCalls(ClanMember[] members) {

        TableLayout callLayout = new TableLayout(this);
        callLayout.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
        callLayout.setGravity(Gravity.TOP);
        callLayout.setOrientation(TableLayout.VERTICAL);

        // Sort members by position
        Arrays.sort(members);

        int memInd = 0;
        ArrayList<ClanMember> memberList = new ArrayList<ClanMember>();
        for (int i = 0; i < clanInfo.getGeneral().getSize(); i++) {
            while (memInd < members.length && members[memInd].getPosy() == i) {
                memberList.add(members[memInd]);
                memInd++;
            }
            callLayout.addView(getMembersLayout(i, memberList));
            memberList = new ArrayList<ClanMember>();
        }

        return callLayout;
    }

    private TableRow getMembersLayout(final int i, final ArrayList<ClanMember> rowList) {

        final TableRow rowLayout = new TableRow(this);
        rowLayout.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        // TODO Add stars in front of comment (keep alignment the same)

        // TODO Then add Comment button

        // Add number button
        final Button numberButton = new Button(this);
        numberButton.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT, .7f));
        numberButton.setGravity(Gravity.LEFT);
        numberButton.setText((i + 1) + ".");
        numberButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.abc_text_size_medium_material));
        numberButton.setTypeface(clashFont);
        numberButton.setBackgroundDrawable(null);

        // Set the color based on occupancy
        if (rowList.isEmpty()) {
            numberButton.setTextColor(getResources().getColor(R.color.number_gold));
        } else {
            numberButton.setTextColor(getResources().getColor(R.color.number_grey));
        }

        // TODO set a listener here
        rowLayout.addView(numberButton);


        // Add Clan members
        final LinearLayout membLayout = new LinearLayout(this);
        membLayout.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT, 3f));
        membLayout.setGravity(Gravity.TOP);
        membLayout.setOrientation(LinearLayout.VERTICAL);

        if (!rowList.isEmpty()) {
            for (ClanMember mem : rowList)
                membLayout.addView(dummyLayout(mem, membLayout, numberButton));

        }

        rowLayout.addView(membLayout);


        // Add + button
        Drawable plus = getResources().getDrawable(R.drawable.add);
        plus.setBounds(0, 0, (int)(plus.getIntrinsicWidth()*0.8),
                (int)(plus.getIntrinsicHeight()*0.8));
        ScaleDrawable sd = new ScaleDrawable(plus, 0, .8f, .8f);

        Button plusButton = new Button(this);
        plusButton.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT, 1.3f));
        plusButton.setGravity(Gravity.CENTER);
        //plusButton.setBackgroundDrawable(plus);
        plusButton.setCompoundDrawables(sd.getDrawable(), null, null, null);

        plusButton.setOnClickListener(new View.OnClickListener() {

            final int num = i + 1;

            @Override
            public void onClick(View v) {

                final EditText input = new EditText(ShowWarActivity.this);
                input.setHint("Your Name");
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);

                AlertDialog.Builder alert = new AlertDialog.Builder(ShowWarActivity.this)
                        .setTitle("Call Target #" + num)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

                                String name = input.getText().toString();

                                // Call the api
                                appendCall(clanInfo.getGeneral().getWarcode(), String.valueOf(num - 1), name);

                                // Set up a new clan member
                                ClanMember mem = new ClanMember();
                                mem.setPlayername(name);
                                mem.setPosy(num - 1);
                                mem.setPosx(rowList.size());

                                // Add to the clan view
                                membLayout.addView(dummyLayout(mem, membLayout, numberButton));

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

        rowLayout.addView(plusButton);

        return rowLayout;
    }

    private LinearLayout dummyLayout(final ClanMember mem, final LinearLayout row, final Button numberButton) {

        final LinearLayout dumb = new LinearLayout(this);
        dumb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        dumb.setGravity(Gravity.TOP);
        dumb.setOrientation(LinearLayout.HORIZONTAL);

        final Button memButton = new Button(this);
        memButton.setGravity(Gravity.LEFT);
        memButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 2f));
        memButton.setText(mem.getPlayername());
        memButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.abc_text_size_small_material));
        memButton.setTypeface(clashFont);
        memButton.setTextColor(getResources().getColor(R.color.button_blue));
        memButton.setBackgroundDrawable(null);
        memButton.setEllipsize(TextUtils.TruncateAt.END);

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

                                // Call the api
                                submitClanName(clanInfo.getGeneral().getWarcode(),
                                        String.valueOf(mem.getPosy()), String.valueOf(mem.getPosx()), newName);

                                // Set the text of the button
                                memButton.setText(newName);
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

        dumb.addView(memButton);


        Drawable x = getResources().getDrawable(R.drawable.x_grey);
        x.setBounds(0, 0, (int)(x.getIntrinsicWidth()*0.6),
                (int)(x.getIntrinsicHeight()*0.6));
        ScaleDrawable sd = new ScaleDrawable(x, 0, .6f, .6f);

        Button xButton = new Button(this);
        //xButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
        //        LinearLayout.LayoutParams.WRAP_CONTENT, 3f));
        //xButton.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
        xButton.setGravity(Gravity.LEFT);
        xButton.setCompoundDrawables(sd.getDrawable(), null, null, null);
        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(ShowWarActivity.this)
                        .setTitle("Delete " + mem.getPlayername() + "'s Call?")
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                // Call the api
                                deleteCall(clanInfo.getGeneral().getWarcode(),
                                        String.valueOf(mem.getPosy()), String.valueOf(mem.getPosx()));

                                // Delete from layout
                                row.removeView(dumb);

                                // Set the color based on occupancy
                                if (row.getChildCount() == 0) {
                                    numberButton.setTextColor(getResources().getColor(R.color.number_gold));
                                } else {
                                    numberButton.setTextColor(getResources().getColor(R.color.number_grey));
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
        // TODO set a listener here

        dumb.addView(xButton);

        return dumb;
    }

    private LinearLayout makeClanMemberButtonLayout(ClanMember member) {

        LinearLayout mainMemLayout = new LinearLayout(this);
        mainMemLayout.setGravity(Gravity.TOP);
        mainMemLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout memLayout = new LinearLayout(this);
        memLayout.setGravity(Gravity.TOP);
        memLayout.setOrientation(LinearLayout.HORIZONTAL);

        Button memberButton = new Button(this);
        memberButton.setText(member.getPlayername());
        memberButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.abc_text_size_medium_material));
        memberButton.setTypeface(clashFont);
        // TODO set a listener here
        memLayout.addView(memberButton);

        Button xButton = new Button(this);
        xButton.setBackgroundResource(R.drawable.x_grey);
        // TODO set a listener here
        memLayout.addView(xButton);
        mainMemLayout.addView(memLayout);

        Button callButton = new Button(this);
        callButton.setBackgroundResource(R.drawable.called);
        callButton.setGravity(Gravity.LEFT);
        // TODO Set button to picture
        // TODO set a listener here
        mainMemLayout.addView(callButton);

        return memLayout;
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
