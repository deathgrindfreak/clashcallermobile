package io.deathgrindfreak.clashcallermobile;

import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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

        mainView.addView(getGeneralLayout(clan.getGeneral()));
        mainView.addView(getCalls(clan.getCalls()));
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
        vs.setText("vs.");

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
        TextView clanMessage = new TextView(this);

        clanMessage.setPadding(0, dpAsPixels, 0, dpAsPixels);

        // Set text
        clanMessage.setText(gen.getClanmessage());

        // Set text gravity
        clanMessage.setGravity(Gravity.CENTER_HORIZONTAL);

        // Set text size
        clanMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.abc_text_size_medium_material));

        clanMessage.setTypeface(clashFont);

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
        LinearLayout memberLayout;
        ArrayList<ClanMember> memberList = new ArrayList<ClanMember>();
        for (int i = 0; i < clanInfo.getGeneral().getSize(); i++) {
            while (members[memInd].getPosy() == i) {
                memberList.add(members[memInd]);
                memInd++;
            }
            callLayout.addView(getMembersLayout(i, memberList));
            memberList = new ArrayList<ClanMember>();
        }

        return callLayout;
    }

    private LinearLayout getMembersLayout(int i, ArrayList<ClanMember> rowList) {

        LinearLayout rowLayout = new LinearLayout(this);
        rowLayout.setGravity(Gravity.TOP);
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);

        // TODO Add stars in front of comment (keep alignment the same)

        // TODO Then add Comment button

        // TODO Add number

        // Add Clan members
        LinearLayout memLayout = new LinearLayout(this);
        memLayout.setGravity(Gravity.TOP);
        memLayout.setOrientation(LinearLayout.VERTICAL);

        for (ClanMember mem : rowList)
            memLayout.addView(makeClanMemberButtonLayout(mem));

        // TODO Add + button

        return rowLayout;
    }

    private LinearLayout makeClanMemberButtonLayout(ClanMember member) {

        LinearLayout memLayout = new LinearLayout(this);
        memLayout.setGravity(Gravity.TOP);
        memLayout.setOrientation(LinearLayout.HORIZONTAL);

        Button memberButton = new Button(this);
        memberButton.setText(member.getPlayername());
        memberButton.setTypeFace(clanFont);
        memberButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.abc_text_size_medium_material));
        // TODO set a listener here

        Button xButton = new Button(this);
        // TODO Set button to picture
        // TODO set a listener here

        memLayout.addView(memberButton);
        memLayout.addview(xButton);

        return memLayout;
    }
}
