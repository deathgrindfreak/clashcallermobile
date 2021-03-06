package io.deathgrindfreak.clashcaller;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import io.deathgrindfreak.controllers.ShowWarController;
import io.deathgrindfreak.model.Clan;
import io.deathgrindfreak.model.ClanMember;
import io.deathgrindfreak.model.General;
import io.deathgrindfreak.model.Target;
import io.deathgrindfreak.util.ClashUtil;
import io.deathgrindfreak.util.JsonParse;
import io.deathgrindfreak.util.TaskCallback;


public class ShowWarActivity extends ActionBarActivity {

    private TableLayout callLayout;
    private Button clanMessage;

    private Typeface clashFont;

    private Clan clanInfo;

    private ShowWarController showWarController;

    // Main view
    private ScrollView mainView;

    private static final String SHOWTAG = "Show War Activity";

    private enum NUMBER_COLOR { GOLD, GREY }

    // Weight for the member button
    private static final float MEMBER_WEIGHT = 20f;

    // DP widths for buttons
    private static final int STAR_WIDTH = 70;
    private static final int COMMENT_WIDTH = 60;
    private static final int X_WIDTH = 43;
    private static final int NUMBER_WIDTH = 67;
    private static final int PLUS_WIDTH = 37;

    // The minimum width required to display the star buttons
    private static final int MIN_WIDTH_FOR_STAR = 3;

    // Indexes for certain buttons
    private static final int MAX_STAR_BUTTON_INDEX = 0;
    private static final int STAR_BUTTON_INDEX = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_war);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        getSupportActionBar().setIcon(R.mipmap.ic_cclogo);

        // Create the controller
        showWarController = new ShowWarController(this);

        clashFont = Typeface.createFromAsset(getAssets(), getString(R.string.font));

        final int previousPosition;
        if (savedInstanceState != null) {
            clanInfo = savedInstanceState.getParcelable("clan");
            previousPosition = savedInstanceState.getInt("previousScrollPosition");
        } else {
            Bundle data = getIntent().getExtras();
            clanInfo = data.getParcelable("clan");
            previousPosition = data.getInt("previousScrollPosition");
        }

        Log.d(SHOWTAG, "Claninfo on load: " + clanInfo);

        displayClanInfo(clanInfo);

        Log.d(SHOWTAG, "Scroll position: " + previousPosition);

        // Scroll to the previous position
        mainView.post(new Runnable() {

            @Override
            public void run() {
                mainView.scrollTo(0, previousPosition);
            }
        });
    }


    @Override
    protected void onRestart() {
        super.onRestart();

        // Refresh the page on restart
        refreshPage();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d(SHOWTAG, "Claninfo on save: " + clanInfo);

        // Save the clanInfo object
        outState.putParcelable("clan", clanInfo);
    }


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Retrieve the clanInfo object
        clanInfo = savedInstanceState.getParcelable("clan");

        Log.d(SHOWTAG, "Claninfo on load (onRestoreInstanceState): " + clanInfo);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_war, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Share button for war id
        if (id == R.id.share_button) {

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String warLink = "http://clashofclans.com/war/" + clanInfo.getGeneral().getWarcode();
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Clash Caller link for war against clan: "
                    + clanInfo.getGeneral().getEnemyname());
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, warLink);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));

            return true;
        }

        if (id == R.id.refresh_button) {
            refreshPage();
            return true;
        }

        if (id == R.id.action_home) {
            startActivity(new Intent(this, MainActivity.class));
        }

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, ClashSettingsActivity.class));
        }

        if (id == R.id.action_help) {
            startActivity(new Intent(this, HelpActivity.class));
        }

        if (id == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }


    private void refreshPage() {


        TaskCallback callback = new TaskCallback() {

            @Override
            public String getProgressMessage() {
                return "Refreshing page ...";
            }

            @Override
            public void onTaskCompleted(ProgressDialog progress, String jsonStr) {

                Intent showWarIntent = getIntent();

                if (!jsonStr.isEmpty() && !jsonStr.contains("Invalid War ID")) {

                    // Get the clanInfo object from the JSON string
                    Clan nfo = JsonParse.parseWarJson(jsonStr);

                    Log.d(SHOWTAG, "<-- CLANINFO -->");
                    Log.d(SHOWTAG, nfo == null ? "null" : nfo.toString());

                    if (nfo != null) {
                        clanInfo = nfo;
                        showWarIntent.putExtra("clan", clanInfo);
                        showWarIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                        // Dismiss the progress
                        if (progress.isShowing())
                            progress.dismiss();

                        // Set the previous scroll position
                        int index = mainView.getScrollY();
                        Log.d(SHOWTAG, "Scroll position: " + index);
                        showWarIntent.putExtra("previousScrollPosition", index);

                        startActivity(showWarIntent);
                        finish();
                    }
                }
            }
        };

        showWarController.getClanInfo(callback, this, clanInfo.getGeneral().getWarcode());
    }


    private void displayClanInfo(Clan clan) {

        mainView = (ScrollView) findViewById(R.id.showWarLayout);

        LinearLayout mainViewChild = new LinearLayout(this);
        mainViewChild.setGravity(Gravity.TOP);
        mainViewChild.setOrientation(LinearLayout.VERTICAL);

        mainViewChild.addView(getGeneralLayout(clan.getGeneral()));
        mainViewChild.addView(getCalls(clan.getCalls()));
        mainViewChild.addView(getClanLog(clan.getLog()));

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
        genLayout.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
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
        clanNameView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ClashUtil.dptopx(this, getString(R.string.text_size_large)));
        enemyNameView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ClashUtil.dptopx(this, getString(R.string.text_size_large)));
        vs.setTextSize(TypedValue.COMPLEX_UNIT_PX, ClashUtil.dptopx(this, getString(R.string.text_size_small)));
        clanNameView.setTypeface(clashFont);
        enemyNameView.setTypeface(clashFont);
        vs.setTypeface(clashFont);


        // Set the orientation of the title based on the text width
        DisplayMetrics m = getResources().getDisplayMetrics();
        int width = m.widthPixels / m.densityDpi;

        clanNameView.measure(0, 0);
        vs.measure(0, 0);
        enemyNameView.measure(0, 0);
        int cWidth = clanNameView.getMeasuredWidth() / m.densityDpi;
        int vWidth = vs.getMeasuredWidth() / m.densityDpi;
        int eWidth = enemyNameView.getMeasuredWidth() / m.densityDpi;

        if (width > cWidth + vWidth + eWidth)
            genLayout.setOrientation(LinearLayout.HORIZONTAL);
        else
            genLayout.setOrientation(LinearLayout.VERTICAL);


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
        final TextView clanID = new TextView(this);

        // Set text
        idText.setText("Your clan's unique ID: ");
        clanID.setText(gen.getWarcode());

        // Set text gravity
        idText.setGravity(Gravity.CENTER_VERTICAL);
        clanID.setGravity(Gravity.CENTER_VERTICAL);

        // Set text color
        clanID.setTextColor(getResources().getColor(R.color.button_blue));

        // Set text size
        idText.setTextSize(TypedValue.COMPLEX_UNIT_PX, ClashUtil.dptopx(this, getString(R.string.text_size_medium)));
        clanID.setTextSize(TypedValue.COMPLEX_UNIT_PX, ClashUtil.dptopx(this, getString(R.string.text_size_medium)));

        clanID.setTypeface(clashFont);

        idLayout.addView(idText);
        idLayout.addView(clanID);

        mainGenLayout.addView(idLayout);


        // Clan message
        clanMessage = new Button(this);
        clanMessage.setPadding(0, dpAsPixels, 0, 2 * dpAsPixels);
        clanMessage.setText(gen.getClanmessage());
        clanMessage.setGravity(Gravity.CENTER_HORIZONTAL);
        clanMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, ClashUtil.dptopx(this, getString(R.string.text_size_medium)));
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

                                if (note != null && !note.isEmpty()) {

                                    TaskCallback callback = new TaskCallback() {

                                        @Override
                                        public String getProgressMessage() {
                                            return "Setting Clan Message ...";
                                        }

                                        @Override
                                        public void onTaskCompleted(ProgressDialog progress, String msg) {

                                            // If msg is empty, then an error occurred
                                            if (!msg.isEmpty()) {
                                                clanMessage.setText(note);

                                                clanInfo.getGeneral().setClanmessage(note);
                                            }

                                            if (progress.isShowing())
                                                progress.dismiss();
                                        }
                                    };

                                    // Set the value of the button in the app and call API to update
                                    showWarController.setClanMessage(callback, ShowWarActivity.this,
                                            clanInfo.getGeneral().getWarcode(), note);


                                } else {
                                    Toast tst = Toast.makeText(ShowWarActivity.this,
                                            "The clan message cannot be blank.", Toast.LENGTH_SHORT);
                                    tst.setGravity(Gravity.CENTER, 0, 0);
                                    tst.show();
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

        mainGenLayout.addView(clanMessage);

        return mainGenLayout;
    }


    private TableLayout getCalls(ArrayList<ClanMember> members) {

        callLayout = new TableLayout(this);
        callLayout.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT));
        callLayout.setGravity(Gravity.TOP);
        callLayout.setOrientation(TableLayout.VERTICAL);

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

            memberList = new ArrayList<>();
        }

        return callLayout;
    }


    private TableRow getMembersLayout(int row, ClanMember member) {


        final TableRow rowLayout = new TableRow(this);
        rowLayout.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        // Set the tag with the clan member
        rowLayout.setTag(member == null ? row : member);

        // Alternate colors based on row
        int color = row % 2 == 0 ? R.color.light_grey : R.color.white;


        // Set the row color
        rowLayout.setBackgroundColor(getResources().getColor(color));


        // Add the star button if the screen width is big enough
        DisplayMetrics m = getResources().getDisplayMetrics();
        int width = m.widthPixels / m.densityDpi;

        if (width >= MIN_WIDTH_FOR_STAR) {
            if (member != null && member.getPosx() == 0) {

                ImageButton star = makeStarButton(row);
                star.setBackgroundColor(getResources().getColor(color));
                rowLayout.addView(star);

            } else {
                ImageButton s = makeStarSpacer();
                s.setBackgroundColor(getResources().getColor(color));
                rowLayout.addView(s);
            }
        }


        // Add Comment button
        if (member == null || member.getPosx() == 0) {

            ImageButton comment = makeCommentButton(row);
            comment.setBackgroundColor(getResources().getColor(color));
            rowLayout.addView(comment);

        } else {
            ImageButton c = makeCommentSpacer();
            c.setBackgroundColor(getResources().getColor(color));
            rowLayout.addView(c);
        }


        // If member is null, row hasn't been claimed yet (set to grey)
        if (member == null) {
            Button num = makeNumberButton(row, NUMBER_COLOR.GOLD);
            num.setId(R.id.numButton);
            rowLayout.addView(num);
        } else if (member.getPosx() == 0) {
            Button num = makeNumberButton(row, NUMBER_COLOR.GREY);
            num.setId(R.id.numButton);
            rowLayout.addView(num);
        } else {
            Button n = makeNumberSpacer();
            n.setBackgroundColor(getResources().getColor(color));
            rowLayout.addView(n);
        }


        // Add the star button for the user
        if (width >= MIN_WIDTH_FOR_STAR) {
            if (member == null) {
                ImageButton s = makeStarSpacer();
                s.setBackgroundColor(getResources().getColor(color));
                rowLayout.addView(s);
            } else {
                ImageButton uStar = makeUserStarButton(row, member,
                        getResources().getColor(color));
                uStar.setBackgroundColor(getResources().getColor(color));
                rowLayout.addView(uStar);

                // Set the review button if note is present
                if (member.getNote() != null && !member.getNote().isEmpty()) {

                    ImageButton attack = makeAttackCommentButton(member);
                    attack.setBackgroundColor(getResources().getColor(color));
                    rowLayout.addView(attack);
                }
            }
        }


        // Add Clan members
        if (member == null) {
            Button mem = makeMemberSpacer();
            ImageButton x = makeXSpacer();

            // Set the background color
            mem.setBackgroundColor(getResources().getColor(color));
            x.setBackgroundColor(getResources().getColor(color));

            rowLayout.addView(mem);
            rowLayout.addView(x);

        } else {
            Button memB = makeMemberButton(member);
            memB.setId(R.id.memButton);

            ImageButton x = makeXButton(member, getResources().getColor(color));

            // Set the background color
            x.setBackgroundColor(getResources().getColor(color));

            rowLayout.addView(memB);
            rowLayout.addView(x);
        }

        // Add + button
        if (member == null || member.getPosx() == 0) {

            ImageButton plus = makePlusButton(row, member);

            // Set the background color
            plus.setBackgroundColor(getResources().getColor(color));

            rowLayout.addView(plus);

        } else {
            ImageButton s = makePlusSpacer();

            s.setBackgroundColor(getResources().getColor(color));

            rowLayout.addView(s);
        }

        return rowLayout;
    }


    private ImageButton makeStarSpacer() {
        ImageButton star = new ImageButton(this);
        star.setLayoutParams(new TableRow.LayoutParams(ClashUtil.dptopx(this, STAR_WIDTH), TableRow.LayoutParams.WRAP_CONTENT));

        return star;
    }

    private ImageButton makeCommentSpacer() {
        ImageButton commentButton = new ImageButton(this);
        commentButton.setLayoutParams(new TableRow.LayoutParams(ClashUtil.dptopx(this, COMMENT_WIDTH), TableRow.LayoutParams.WRAP_CONTENT));

        return commentButton;
    }

    private Button makeNumberSpacer() {
        Button numberButton = new Button(this);
        numberButton.setLayoutParams(new TableRow.LayoutParams(ClashUtil.dptopx(this, NUMBER_WIDTH), TableRow.LayoutParams.WRAP_CONTENT, 0));

        return numberButton;
    }

    private Button makeMemberSpacer() {
        Button memButton = new Button(this);
        memButton.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, MEMBER_WEIGHT));

        return memButton;
    }

    private ImageButton makeXSpacer() {
        ImageButton xButton = new ImageButton(this);
        xButton.setLayoutParams(new TableRow.LayoutParams(ClashUtil.dptopx(this, X_WIDTH), TableRow.LayoutParams.WRAP_CONTENT));

        return xButton;
    }

    private ImageButton makePlusSpacer() {
        ImageButton plusButton = new ImageButton(this);
        plusButton.setLayoutParams(new TableRow.LayoutParams(ClashUtil.dptopx(this, PLUS_WIDTH), TableRow.LayoutParams.WRAP_CONTENT));


        return plusButton;
    }


    private ImageButton makeStarButton(final int row) {

        final ImageButton starButton = new ImageButton(this);

        // Get the first clan member at the row
        ArrayList<ClanMember> mems = getMembersAtRow(row);

        // Find the maximum number of stars for the target
        int stars = 1;
        for (ClanMember mem : mems)
            if (mem.getStars() > stars)
                stars = mem.getStars();

        setStarImage(starButton, stars);

        starButton.setScaleType(ImageView.ScaleType.CENTER);

        starButton.setLayoutParams(new TableRow.LayoutParams(ClashUtil.dptopx(this, STAR_WIDTH),
                TableRow.LayoutParams.WRAP_CONTENT));


        return starButton;
    }


    private ImageButton makeCommentButton(final int row) {

        final ImageButton commentButton = new ImageButton(this);

        // Get the first clan member at the row
        final Target target = getTargetAtRow(row);

        // Set the image based on whether the note exists
        if (target != null && target.getNote() != null)
            commentButton.setImageResource(R.drawable.chaticon);
        else
            commentButton.setImageResource(R.drawable.litechaticon);

        commentButton.setScaleType(ImageView.ScaleType.CENTER);

        commentButton.setLayoutParams(new TableRow.LayoutParams(ClashUtil.dptopx(this, COMMENT_WIDTH),
                TableRow.LayoutParams.WRAP_CONTENT));

        // Add the listener and alertdialog popup
        commentButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final EditText input = new EditText(ShowWarActivity.this);
                String note = target == null ? null : target.getNote();

                // If the comment is set, set the text to the existing comment
                if (note == null) {
                    input.setHint("Comment");
                } else {
                    input.setText(note);
                }

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);

                AlertDialog.Builder alert = new AlertDialog.Builder(ShowWarActivity.this)
                        .setTitle("Target #" + (row + 1) + " Note:")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                // Get the value from the input field
                                final String note = input.getText().toString();

                                if (note != null && !note.isEmpty()) {

                                    TaskCallback callback = new TaskCallback() {

                                        @Override
                                        public String getProgressMessage() {
                                            return "Setting Comment ...";
                                        }

                                        @Override
                                        public void onTaskCompleted(ProgressDialog progress, String msg) {
                                            // If msg is empty, then an error occurred
                                            if (!msg.isEmpty()) {

                                                // Set the icon of the comment button
                                                commentButton.setImageResource(R.drawable.chaticon);

                                                // Set the note in the clanInfo instance
                                                if (target != null) {
                                                    target.setNote(note);
                                                } else {
                                                    Target tgt = new Target();
                                                    tgt.setPosition(row);
                                                    tgt.setNote(note);

                                                    clanInfo.getTargets().add(tgt);
                                                }
                                            }

                                            if (progress.isShowing())
                                                progress.dismiss();
                                        }
                                    };

                                    // Set the value of the button in the app and call API to update
                                    showWarController.setMemberNote(callback, ShowWarActivity.this,
                                            clanInfo.getGeneral().getWarcode(),
                                            String.valueOf(row), note);

                                } else {
                                    Toast tst = Toast.makeText(ShowWarActivity.this,
                                            "The comment for a target cannot be blank.", Toast.LENGTH_SHORT);
                                    tst.setGravity(Gravity.CENTER, 0, 0);
                                    tst.show();
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

        return commentButton;
    }


    private Button makeNumberButton(int callNumber, NUMBER_COLOR color) {

        Button numberButton = new Button(this);
        numberButton.setLayoutParams(new TableRow.LayoutParams(ClashUtil.dptopx(this, NUMBER_WIDTH),
                TableRow.LayoutParams.WRAP_CONTENT, 0));
        numberButton.setGravity(Gravity.LEFT);
        numberButton.setText((callNumber + 1) + ".");
        numberButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, ClashUtil.dptopx(this, getString(R.string.text_size_medium)));
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


    private ImageButton makeUserStarButton(final int row, final ClanMember member, final int color) {

        final ImageButton starButton = new ImageButton(this);

        // Find the maximum number of stars for the target
        int stars = member.getStars();

        setUserStarImage(starButton, stars);

        starButton.setScaleType(ImageView.ScaleType.CENTER);

        starButton.setLayoutParams(new TableRow.LayoutParams(ClashUtil.dptopx(this, STAR_WIDTH),
                TableRow.LayoutParams.WRAP_CONTENT));

        // Set the listener and alertdialog popup
        starButton.setOnClickListener(new View.OnClickListener() {

            // Sets the stars on button click
            private void setStars(final int stars) {

                TaskCallback callback = new TaskCallback() {

                    @Override
                    public String getProgressMessage() {
                        return "Setting Stars ...";
                    }

                    @Override
                    public void onTaskCompleted(ProgressDialog progress, String msg) {

                        // If msg is empty, an error occurred elsewhere
                        if (!msg.isEmpty()) {

                            // Set the image for the starButton
                            setUserStarImage(starButton, stars);

                            // Set the stars for the member (Member is never null btw)
                            int ind = clanInfo.getCalls().indexOf(member);
                            clanInfo.getCalls().get(ind).setStars(stars);

                            // Set the max stars for the row
                            ArrayList<ClanMember> mems = getMembersAtRow(row);
                            ClanMember fst = mems.get(0);

                            TableRow row = (TableRow) callLayout.findViewWithTag(fst);
                            ImageButton maxButton = (ImageButton) row.getChildAt(MAX_STAR_BUTTON_INDEX);

                            int maxStars = 2;
                            for (ClanMember mem : mems)
                                if (mem.getStars() > maxStars)
                                    maxStars = mem.getStars();

                            setStarImage(maxButton, maxStars);
                        }

                        if (progress.isShowing())
                            progress.dismiss();
                    }
                };

                // Call the api
                showWarController.updateMemberStars(callback, ShowWarActivity.this,
                        clanInfo.getGeneral().getWarcode(),
                        String.valueOf(member.getPosy()),
                        String.valueOf(member.getPosx()),
                        String.valueOf(stars));
            }

            @Override
            public void onClick(View v) {

                final EditText input = new EditText(ShowWarActivity.this);
                input.setHint("New Call");
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);

                AlertDialog.Builder a = new AlertDialog.Builder(ShowWarActivity.this)
                        .setTitle("Set Result for Attack by " + member.getPlayername().trim());

                final AlertDialog alert = a.create();

                ImageButton called = new ImageButton(ShowWarActivity.this);
                called.setImageResource(R.drawable.calledbig);
                called.setBackgroundColor(getResources().getColor(R.color.white));
                called.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        setStars(1);
                        alert.cancel();
                    }
                });

                ImageButton zeroStar = new ImageButton(ShowWarActivity.this);
                zeroStar.setImageResource(R.drawable.zerostarbig);
                zeroStar.setBackgroundColor(getResources().getColor(R.color.white));
                zeroStar.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        setStars(2);
                        alert.cancel();
                    }
                });

                ImageButton oneStar = new ImageButton(ShowWarActivity.this);
                oneStar.setImageResource(R.drawable.onestarbig);
                oneStar.setBackgroundColor(getResources().getColor(R.color.white));
                oneStar.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        setStars(3);
                        alert.cancel();
                    }
                });

                ImageButton twoStar = new ImageButton(ShowWarActivity.this);
                twoStar.setImageResource(R.drawable.twostarbig);
                twoStar.setBackgroundColor(getResources().getColor(R.color.white));
                twoStar.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        setStars(4);
                        alert.cancel();
                    }
                });

                ImageButton threeStar = new ImageButton(ShowWarActivity.this);
                threeStar.setImageResource(R.drawable.threestarbig);
                threeStar.setBackgroundColor(getResources().getColor(R.color.white));
                threeStar.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        setStars(5);
                        alert.cancel();
                    }
                });

                TextView or = new TextView(ShowWarActivity.this);
                or.setGravity(Gravity.CENTER_HORIZONTAL);
                or.setBackgroundColor(getResources().getColor(R.color.white));
                or.setTypeface(clashFont);
                or.setTextSize(TypedValue.COMPLEX_UNIT_PX, ClashUtil.dptopx(ShowWarActivity.this,
                        getString(R.string.text_size_large)));
                or.setTextColor(getResources().getColor(R.color.button_blue));
                or.setText("- or -");

                ImageButton reviewAttack = new ImageButton(ShowWarActivity.this);
                reviewAttack.setImageResource(R.drawable.reviewbutton);
                reviewAttack.setBackgroundColor(getResources().getColor(R.color.white));
                reviewAttack.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        // Close the current alert
                        alert.cancel();

                        // Open a new alert dialog
                        final EditText input = new EditText(ShowWarActivity.this);

                        if (member.getNote() == null) {
                            input.setHint("Review ...");
                        } else {
                            input.setText(member.getNote());
                        }

                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        input.setLayoutParams(lp);

                        AlertDialog.Builder alert = new AlertDialog.Builder(ShowWarActivity.this)
                                .setTitle("Review for " + member.getPlayername().trim() + "'s Attack.")
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        // Get the value from the input field
                                        final String note = input.getText().toString();

                                        if (note != null && !note.isEmpty()) {

                                            TaskCallback callback = new TaskCallback() {

                                                @Override
                                                public String getProgressMessage() {
                                                    return "Setting Attack Review ...";
                                                }
                                                @Override
                                                public void onTaskCompleted(ProgressDialog progress, String msg) {

                                                    // If msg is empty, then an error occurred
                                                    if (!msg.isEmpty()) {

                                                        int i = clanInfo.getCalls().indexOf(member);
                                                        clanInfo.getCalls().get(i).setNote(note);
                                                        member.setNote(note);

                                                        ImageButton reviewButton = makeAttackCommentButton(member);
                                                        reviewButton.setBackgroundColor(color);

                                                        // Add the review button for the member
                                                        TableRow starRow = (TableRow) callLayout.findViewWithTag(member);
                                                        starRow.addView(reviewButton, STAR_BUTTON_INDEX);
                                                    }

                                                    if (progress.isShowing())
                                                        progress.dismiss();
                                                }
                                            };

                                            // Set the value of the button in the app and call API to update
                                            showWarController.setAttackNote(callback, ShowWarActivity.this,
                                                    clanInfo.getGeneral().getWarcode(),
                                                    String.valueOf(row),
                                                    String.valueOf(member.getPosx()),
                                                    note);

                                        } else {
                                            Toast tst = Toast.makeText(ShowWarActivity.this,
                                                    "The comment for an attack cannot be blank.", Toast.LENGTH_SHORT);
                                            tst.setGravity(Gravity.CENTER, 0, 0);
                                            tst.show();
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


                ScrollView buttonScroll = new ScrollView(ShowWarActivity.this);

                LinearLayout buttonLayout = new LinearLayout(ShowWarActivity.this);
                buttonLayout.setOrientation(LinearLayout.VERTICAL);
                buttonLayout.setGravity(Gravity.CENTER_HORIZONTAL);
                buttonLayout.addView(called);
                buttonLayout.addView(zeroStar);
                buttonLayout.addView(oneStar);
                buttonLayout.addView(twoStar);
                buttonLayout.addView(threeStar);
                buttonLayout.addView(or);
                buttonLayout.addView(reviewAttack);

                buttonScroll.addView(buttonLayout);

                alert.setView(buttonScroll);
                alert.show();
            }
        });


        return starButton;
    }


    private ImageButton makeAttackCommentButton(final ClanMember member) {

        ImageButton reviewButton = new ImageButton(this);
        reviewButton.setImageResource(R.drawable.review);
        reviewButton.setLayoutParams(new TableRow.LayoutParams(ClashUtil.dptopx(this, PLUS_WIDTH),
                TableRow.LayoutParams.WRAP_CONTENT));

        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert = new AlertDialog.Builder(ShowWarActivity.this)
                    .setTitle("Review for " + member.getPlayername().trim() + "'s Attack.")
                    .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing
                        }
                    });

                TextView text = new TextView(ShowWarActivity.this);
                text.setText(member.getNote());

                alert.setView(text);
                alert.show();
            }
        });

        return reviewButton;
    }


    private Button makeMemberButton(final ClanMember mem) {

        // Create the member button
        Button memButton = new Button(this);
        memButton.setGravity(Gravity.LEFT);
        memButton.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, MEMBER_WEIGHT));
        memButton.setPadding(0, 0, 40, 0);
        memButton.setText(mem.getPlayername());
        memButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, ClashUtil.dptopx(this, getString(R.string.text_size_small)));
        memButton.setTypeface(clashFont);
        memButton.setTextColor(getResources().getColor(R.color.button_blue));
        memButton.setBackgroundDrawable(null);
        memButton.setEllipsize(TextUtils.TruncateAt.END);
        memButton.setSingleLine(true);

        // Set the listener and alertdialog popup
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

                                final String newName = input.getText().toString();

                                if (newName != null && !newName.isEmpty()) {

                                    TaskCallback callback = new TaskCallback() {

                                        @Override
                                        public String getProgressMessage() {
                                            return "Changing Call ...";
                                        }

                                        @Override
                                        public void onTaskCompleted(ProgressDialog progress, String msg) {

                                            // If msg is empty, an error occurred
                                            if (!msg.isEmpty()) {
                                                // Set the name of the clan member
                                                int ind = clanInfo.getCalls().indexOf(mem);
                                                clanInfo.getCalls().get(ind).setPlayername(newName);

                                                // Find the table row
                                                TableRow row = (TableRow) callLayout.findViewWithTag(mem);

                                                Button memB = (Button) row.findViewById(R.id.memButton);
                                                memB.setText(newName);
                                            }

                                            if (progress.isShowing())
                                                progress.dismiss();
                                        }
                                    };

                                    // Call the api
                                    showWarController.submitClanName(callback, ShowWarActivity.this,
                                            clanInfo.getGeneral().getWarcode(),
                                            String.valueOf(mem.getPosy()),
                                            String.valueOf(mem.getPosx()),
                                            newName);

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


    private ImageButton makePlusButton(final int row, final ClanMember member) {

        ImageButton plusButton = new ImageButton(this);
        plusButton.setImageResource(R.drawable.add);
        plusButton.setLayoutParams(new TableRow.LayoutParams(ClashUtil.dptopx(this, PLUS_WIDTH),
                TableRow.LayoutParams.WRAP_CONTENT));

        // Add the listener and alertdialog popup
        plusButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SharedPreferences sharedPref = ShowWarActivity.this.getSharedPreferences(
                        getString(R.string.village_name), Context.MODE_PRIVATE);

                String villageName = sharedPref.getString(getResources().getString(R.string.village_name), "");


                final EditText input = new EditText(ShowWarActivity.this);

                // Set the village name if it exists
                if (villageName.isEmpty()) {
                    input.setHint("Your Name");
                } else {
                    input.setText(villageName);
                }

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);

                AlertDialog.Builder alert = new AlertDialog.Builder(ShowWarActivity.this)
                        .setTitle("Call Target #" + (row + 1))
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

                                final String name = input.getText().toString();

                                // Name cannot be empty
                                if (name != null && !name.isEmpty()) {

                                    TaskCallback callback = new TaskCallback() {

                                        @Override
                                        public String getProgressMessage() {
                                            return "Calling Target ...";
                                        }

                                        @Override
                                        public void onTaskCompleted(ProgressDialog progress, String msg) {

                                            // If msg is empty, an error occurred
                                            if (!msg.isEmpty()) {
                                                // Set the new player name
                                                ClanMember mem = new ClanMember();
                                                mem.setPlayername(name);
                                                mem.setPosy(row);


                                                if (member != null) {

                                                    // Find the table row for the call number
                                                    TableRow rowL = (TableRow) callLayout.findViewWithTag(member);
                                                    int index = callLayout.indexOfChild(rowL);

                                                    // Set the member index to the last for that call number
                                                    mem.setPosx(getLastMemberIndex(index));

                                                    // Add to the clan view
                                                    Log.d(SHOWTAG, "row: " + index + " New Row: " + getLastMemberIndex(index));
                                                    callLayout.addView(getMembersLayout(index, mem), index + getLastMemberIndex(index));

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
                                            }

                                            if (progress.isShowing())
                                                progress.dismiss();
                                        }
                                    };

                                    // Call the api
                                    showWarController.appendCall(callback, ShowWarActivity.this,
                                            clanInfo.getGeneral().getWarcode(),
                                            String.valueOf(row),
                                            name);

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


    private ImageButton makeXButton(final ClanMember member, final int color) {

        ImageButton xButton = new ImageButton(this);
        xButton.setImageResource(R.drawable.x_grey);
        xButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        xButton.setLayoutParams(new TableRow.LayoutParams(ClashUtil.dptopx(this, X_WIDTH),
                TableRow.LayoutParams.WRAP_CONTENT));

        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(ShowWarActivity.this)
                        .setTitle("Delete " + member.getPlayername().trim() + "'s Call?")
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                TaskCallback callback = new TaskCallback() {

                                    @Override
                                    public String getProgressMessage() {
                                        return "Deleting Call ...";
                                    }

                                    @Override
                                    public void onTaskCompleted(ProgressDialog progress, String msg) {

                                        // If msg is empty, an error occurred
                                        if (!msg.isEmpty()) {

                                            // Find the table row
                                            TableRow row = (TableRow) callLayout.findViewWithTag(member);
                                            int index = callLayout.indexOfChild(row);

                                            // Delete from layout
                                            callLayout.removeView(row);

                                            // Get all the clan members belonging to PosY of member
                                            ArrayList<ClanMember> rowMembers = getMembersAtRow(member.getPosy());

                                            // Remove the deleted member
                                            rowMembers.remove(member);

                                            // Decrement the x position of all lower members
                                            for (ClanMember mem : rowMembers)
                                                if (mem.getPosx() > member.getPosx())
                                                    mem.setPosx(mem.getPosx() - 1);


                                            // If member was the top call, add a new row back with appropriate member (empty if it was the last)
                                            if (member.getPosx() == 0) {
                                                if (!rowMembers.isEmpty()) {
                                                    TableRow nextMemRow = (TableRow) callLayout.findViewWithTag(rowMembers.get(0));
                                                    callLayout.removeView(nextMemRow);
                                                    callLayout.addView(getMembersLayout(member.getPosy(), rowMembers.get(0)), index);
                                                } else {
                                                    callLayout.addView(getMembersLayout(member.getPosy(), null), index);
                                                }
                                            }

                                            // Remove from clan calls
                                            clanInfo.removeClanMember(member);


                                            // Set the max star for the call
                                            DisplayMetrics m = getResources().getDisplayMetrics();
                                            int width = m.widthPixels / m.densityDpi;
                                            if (width >= MIN_WIDTH_FOR_STAR) {

                                                TableRow starRow;
                                                ArrayList<ClanMember> mems = getMembersAtRow(member.getPosy());
                                                if (member.getPosx() == 0) {
                                                    starRow = (TableRow) callLayout.getChildAt(index);
                                                } else {
                                                    starRow = (TableRow) callLayout.findViewWithTag(mems.get(0));
                                                }

                                                ImageButton maxButton = (ImageButton) starRow.getChildAt(MAX_STAR_BUTTON_INDEX);

                                                int maxStars = -1;
                                                for (ClanMember mem : mems)
                                                    if (mem.getStars() > maxStars)
                                                        maxStars = mem.getStars();

                                                if (maxStars == -1) {
                                                    starRow.removeViewAt(MAX_STAR_BUTTON_INDEX);

                                                    ImageButton blank = makeStarSpacer();
                                                    blank.setBackgroundColor(color);
                                                    starRow.addView(blank, MAX_STAR_BUTTON_INDEX);
                                                } else {
                                                    setStarImage(maxButton, maxStars);
                                                }
                                            }
                                        }


                                        // Dismiss the progress dialog
                                        if (progress.isShowing())
                                            progress.dismiss();
                                    }
                                };

                                // Call the api
                                showWarController.deleteCall(callback, ShowWarActivity.this,
                                        clanInfo.getGeneral().getWarcode(),
                                        String.valueOf(member.getPosy()),
                                        String.valueOf(member.getPosx()));

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


    private LinearLayout getClanLog(String[] log) {

        LinearLayout logLayout = new LinearLayout(this);
        logLayout.setGravity(Gravity.TOP);
        logLayout.setOrientation(LinearLayout.VERTICAL);


        if (log.length != 0) {

            // Set the title
            TextView logTitle = new TextView(this);
            logTitle.setTextColor(getResources().getColor(R.color.button_blue));
            logTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, ClashUtil.dptopx(this, getString(R.string.text_size_large)));
            logTitle.setTypeface(clashFont);
            logTitle.setGravity(Gravity.CENTER_HORIZONTAL);
            logTitle.setPadding(0, 0, 0, 20);

            logTitle.setText("Log");
            logLayout.addView(logTitle);


            // Add the log entries
            for (String line : log) {

                TextView logLine = new TextView(this);
                logLine.setTextColor(getResources().getColor(R.color.number_grey));
                logLine.setTextSize(TypedValue.COMPLEX_UNIT_PX, ClashUtil.dptopx(this, getString(R.string.text_size_small)));
                logLine.setTypeface(clashFont);

                Spannable goldLine = new SpannableString(line);
                goldLine.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.number_gold))
                        , 0, line.indexOf(":", line.indexOf(":") + 1) + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                logLine.setText(goldLine);

                logLayout.addView(logLine);
            }
        }

        return logLayout;
    }


    private void setUserStarImage(ImageButton starButton, int stars) {

        switch(stars) {
            case 0:
            case 1:
                starButton.setImageResource(R.drawable.called);
                break;
            case 2:
                starButton.setImageResource(R.drawable.zero_star);
                break;
            case 3:
                starButton.setImageResource(R.drawable.one_star);
                break;
            case 4:
                starButton.setImageResource(R.drawable.two_star);
                break;
            case 5:
                starButton.setImageResource(R.drawable.three_star);
                break;
        }
    }

    private void setStarImage(ImageButton starButton, int stars) {

        starButton.setId(stars);

        switch(stars) {
            case 1:
                starButton.setImageResource(0);
            case 2:
                starButton.setImageResource(R.drawable.zero_star);
                break;
            case 3:
                starButton.setImageResource(R.drawable.one_star);
                break;
            case 4:
                starButton.setImageResource(R.drawable.two_star);
                break;
            case 5:
                starButton.setImageResource(R.drawable.three_star);
                break;
        }
    }


    private ArrayList<ClanMember> getMembersAtRow(int row) {

        ArrayList<ClanMember> rowMems = new ArrayList<ClanMember>();

        for (ClanMember mem : clanInfo.getCalls())
            if (mem.getPosy() == row)
                rowMems.add(mem);

        // Sort by posx before returning
        Collections.sort(rowMems);

        return rowMems;
    }


    private int getLastMemberIndex(int row) {

        ArrayList<ClanMember> mems = clanInfo.getCalls();
        Collections.sort(mems);

        // Find the first occurrence of member at y position row
        int rowMembers = 0, memInd = 0;
        while (memInd < mems.size()) {
            if (mems.get(memInd).getPosy() == row)
                ++rowMembers;
            ++memInd;
        }

        return rowMembers;
    }


    private Target getTargetAtRow(int row) {

        ArrayList<Target> tgts = clanInfo.getTargets();

        Target target = null;
        for (Target t : tgts)
            if (t.getPosition() == row) {
                target = t;
                break;
            }

        return target;
    }


    public void displaySubmitClanNameToast() {
        Toast.makeText(this, "Call name cannot be empty!", Toast.LENGTH_SHORT).show();
    }

    public void displayAppendCallToast() {
        Toast.makeText(this, "Call name cannot be empty!", Toast.LENGTH_SHORT).show();
    }
}
