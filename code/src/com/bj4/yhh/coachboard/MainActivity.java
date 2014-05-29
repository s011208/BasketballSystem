
package com.bj4.yhh.coachboard;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements ActionBar.TabListener {

    private PlayGroundFragment mFullGround, mHalfGround;

    private static final int TAB_FULLGROUND = 0;

    private static final int TAB_HALFGROUND = 1;

    private int mCurrentFragment = TAB_FULLGROUND;

    private PlayGroundFragment getCurrentPlayGroundFragment() {
        switch (mCurrentFragment) {
            case TAB_FULLGROUND:
                return getFullGround();
            case TAB_HALFGROUND:
                return getHalfGround();
        }
        return null;
    }

    private synchronized PlayGroundFragment getFullGround() {
        if (mFullGround == null) {
            mFullGround = new PlayGroundFragment(this, R.drawable.full_play_ground);
        }
        return mFullGround;
    }

    private synchronized PlayGroundFragment getHalfGround() {
        if (mHalfGround == null) {
            mHalfGround = new PlayGroundFragment(this, R.drawable.half_play_ground);
        }
        return mHalfGround;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActionBar();
    }

    private void initActionBar() {

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.addTab(actionBar.newTab().setText(R.string.tab_full_ground).setTabListener(this),
                TAB_FULLGROUND);
        actionBar.addTab(actionBar.newTab().setText(R.string.tab_half_ground).setTabListener(this),
                TAB_HALFGROUND);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_erase:
                getCurrentPlayGroundFragment().erasePen();
                break;
            case R.id.action_load:
                ArrayList<String> itemTitle = new ArrayList<String>();
                final ArrayList<JSONObject> itemJList = new ArrayList<JSONObject>();
                final File[] fileList = getFilesDir().listFiles();
                for (File f : fileList) {
                    String data = PlayGround.readFromFile(f.getAbsolutePath());
                    try {
                        JSONObject j = new JSONObject(data);
                        itemTitle.add(j.getString(PlayGround.JSON_KEY_TITLE));
                        itemJList.add(j);
                    } catch (JSONException e) {
                        Log.w("QQQQ", "failed", e);
                    }
                }
                final String[] title = itemTitle.toArray(new String[0]);
                new AlertDialog.Builder(new ContextThemeWrapper(this,
                        android.R.style.Theme_DeviceDefault_Dialog))
                        .setTitle(R.string.action_bar_load_dialog_title)
                        .setItems(title, new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getCurrentPlayGroundFragment().restoreData(itemJList.get(which));
                                getActionBar().setTitle(title[which]);
                            }
                        }).setNegativeButton(R.string.cancel, null).setCancelable(true).show();

                break;
            case R.id.action_save:
                final EditText titleEditText = new EditText(this);
                new AlertDialog.Builder(new ContextThemeWrapper(this,
                        android.R.style.Theme_DeviceDefault_Dialog))
                        .setTitle(R.string.action_bar_save_dialog_title)
                        .setIcon(android.R.drawable.ic_dialog_info).setView(titleEditText)
                        .setPositiveButton(R.string.ok, new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getCurrentPlayGroundFragment().saveData(
                                        titleEditText.getText().toString());
                            }
                        }).setNegativeButton(R.string.cancel, null).setCancelable(true).show();
                break;
            case R.id.action_reset:
                getCurrentPlayGroundFragment().resetAll();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        int position = tab.getPosition();
        switch (position) {
            case TAB_FULLGROUND:
                fragmentTransaction.replace(R.id.fragment_main, getFullGround());
                break;
            case TAB_HALFGROUND:
                fragmentTransaction.replace(R.id.fragment_main, getHalfGround());
                break;
        }
        mCurrentFragment = position;
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

}
