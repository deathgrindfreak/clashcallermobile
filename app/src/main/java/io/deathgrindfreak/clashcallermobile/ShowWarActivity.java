package io.deathgrindfreak.clashcallermobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import io.deathgrindfreak.model.Clan;
import io.deathgrindfreak.model.ClanMember;
import io.deathgrindfreak.model.General;


public class ShowWarActivity extends ActionBarActivity {

    private Typeface clashFont;

    private Clan clanInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_war);

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
        Button clanMessage = new Button(this);
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

                AlertDialog.Builder alert = new AlertDialog.Builder(ShowWarActivity.this)
                        .setTitle("Update Clan Message")
                        //.setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        });
                        //.setIcon(android.R.drawable.ic_dialog_alert)

                final EditText input = new EditText(ShowWarActivity.this);
                input.setHint("Message");
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alert.setView(input);

                alert.show();
            }
        });

        mainGenLayout.addView(clanMessage);

        return mainGenLayout;
    }

    private LinearLayout getCalls(ClanMember[] members) {

        LinearLayout callLayout = new LinearLayout(this);
        callLayout.setGravity(Gravity.TOP);
        callLayout.setOrientation(LinearLayout.VERTICAL);

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

    private LinearLayout getMembersLayout(final int i, ArrayList<ClanMember> rowList) {

        LinearLayout rowLayout = new LinearLayout(this);
        rowLayout.setGravity(Gravity.TOP);
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);

        // TODO Add stars in front of comment (keep alignment the same)

        // TODO Then add Comment button

        // Add number button
        Button numberButton = new Button(this);
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
        if (!rowList.isEmpty()) {
            LinearLayout membLayout = new LinearLayout(this);
            membLayout.setGravity(Gravity.TOP);
            membLayout.setOrientation(LinearLayout.VERTICAL);

            for (ClanMember mem : rowList)
                membLayout.addView(dummyLayout(mem));

            rowLayout.addView(membLayout);
        }

        // Add + button
        Button plusButton = new Button(this);
        plusButton.setGravity(Gravity.LEFT);
        plusButton.setBackgroundResource(R.drawable.add);
        plusButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.abc_text_size_medium_material));
        plusButton.setTypeface(clashFont);

        plusButton.setOnClickListener(new View.OnClickListener() {

            final int num = i + 1;

            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(ShowWarActivity.this)
                        .setTitle("Call Target #" + num)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        });

                final EditText input = new EditText(ShowWarActivity.this);
                input.setHint("Your Name");
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alert.setView(input);

                alert.show();
            }
        });

        rowLayout.addView(plusButton);

        return rowLayout;
    }

    private LinearLayout dummyLayout(ClanMember mem) {

        LinearLayout dumb = new LinearLayout(this);
        dumb.setGravity(Gravity.TOP);
        dumb.setOrientation(LinearLayout.VERTICAL);

        Button memButton = new Button(this);
        memButton.setGravity(Gravity.LEFT);
        memButton.setText(mem.getPlayername());
        memButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.abc_text_size_small_material));
        memButton.setTypeface(clashFont);
        memButton.setTextColor(getResources().getColor(R.color.button_blue));
        memButton.setBackgroundDrawable(null);
        dumb.addView(memButton);

        memButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(ShowWarActivity.this)
                        .setTitle("Change Call To:")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        });

                final EditText input = new EditText(ShowWarActivity.this);
                input.setHint("New Call");
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alert.setView(input);

                alert.show();
            }
        });

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
}
