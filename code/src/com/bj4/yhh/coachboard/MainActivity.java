
package com.bj4.yhh.coachboard;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.bj4.yhh.coachboard.PlayGround.MainActivityCallback;
import com.bj4.yhh.coachboard.basketball.R;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements ActionBar.TabListener, MainActivityCallback {

    private PlayGroundFragment mFullGround, mHalfGround;

    private SaveDataListFragment mSaveDataListFragment;

    private boolean mDisableBackPress = true;

    private static final int TAB_FULLGROUND = 0;

    private static final int TAB_HALFGROUND = 1;

    private static final int TAB_SAVE_DATALIST = 2;

    private int mCurrentFragment = TAB_FULLGROUND;

    public static final int SPORT_TYPE_BASKETBALL = 0;

    private Fragment getCurrentFragment() {
        switch (mCurrentFragment) {
            case TAB_FULLGROUND:
                return getFullGround();
            case TAB_HALFGROUND:
                return getHalfGround();
            case TAB_SAVE_DATALIST:
                return getSaveDataListFragment();
        }
        return null;
    }

    private synchronized SaveDataListFragment getSaveDataListFragment() {
        if (mSaveDataListFragment == null) {
            mSaveDataListFragment = new SaveDataListFragment(this);
        }
        return mSaveDataListFragment;
    }

    private synchronized PlayGroundFragment getFullGround() {
        if (mFullGround == null) {
            mFullGround = new PlayGroundFragment(this, R.drawable.basketball_full_play_ground);
            mFullGround.setCallback(this);
        }
        return mFullGround;
    }

    private synchronized PlayGroundFragment getHalfGround() {
        if (mHalfGround == null) {
            mHalfGround = new PlayGroundFragment(this, R.drawable.basketball_half_play_ground);
            mHalfGround.setCallback(this);
        }
        return mHalfGround;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActionBar();
    }

    public void onPause() {
        super.onPause();
        if (getCurrentFragment() instanceof PlayGroundFragment) {
            final PlayGroundFragment playGroundFragment = (PlayGroundFragment)getCurrentFragment();
            playGroundFragment.cancelReplay();
        }
    }

    private void initActionBar() {

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(10);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.addTab(actionBar.newTab().setText(R.string.tab_full_ground).setTabListener(this),
                TAB_FULLGROUND);
        actionBar.addTab(actionBar.newTab().setText(R.string.tab_half_ground).setTabListener(this),
                TAB_HALFGROUND);
        actionBar.addTab(actionBar.newTab().setText(R.string.tab_save_data).setTabListener(this),
                TAB_SAVE_DATALIST);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        if (mCurrentFragment == TAB_SAVE_DATALIST) {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(false);
            menu.getItem(2).setVisible(false);
            menu.getItem(3).setVisible(false);
            menu.getItem(4).setVisible(false);
            menu.getItem(5).setVisible(false);
            menu.getItem(6).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        final PlayGroundFragment playGroundFragment;
        if (getCurrentFragment() instanceof PlayGroundFragment) {
            playGroundFragment = (PlayGroundFragment)getCurrentFragment();
            switch (id) {
                case R.id.action_erase:
                    playGroundFragment.erasePen();
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
                    if (title.length > 0) {
                        new AlertDialog.Builder(new ContextThemeWrapper(this,
                                android.R.style.Theme_DeviceDefault_Dialog))
                                .setTitle(R.string.action_bar_load_dialog_title)
                                .setItems(title, new OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        playGroundFragment.restoreData(itemJList.get(which));
                                        getActionBar().setTitle(title[which]);
                                    }
                                }).setNegativeButton(R.string.cancel, null).setCancelable(true)
                                .show();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.none_saved_data_hint,
                                Toast.LENGTH_SHORT).show();
                    }
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
                                    playGroundFragment.saveData(titleEditText.getText().toString());
                                    Toast.makeText(getApplicationContext(),
                                            R.string.data_saved_success_hint, Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }).setNegativeButton(R.string.cancel, null).setCancelable(true).show();
                    break;
                case R.id.action_reset:
                    playGroundFragment.resetAll();
                    getActionBar().setTitle(R.string.app_name);
                    break;
                case R.id.action_share:
                    playGroundFragment.sharePlayGround();
                    break;
                case R.id.action_replay:
                    if (playGroundFragment.isReplaying() == false) {
                        playGroundFragment.replay();
                    }
                    break;
                case R.id.action_undo:
                    playGroundFragment.undo();
                    break;
            }
        } else {
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
            case TAB_SAVE_DATALIST:
                fragmentTransaction.replace(R.id.fragment_main, getSaveDataListFragment());
                break;
        }
        mCurrentFragment = position;
        invalidateOptionsMenu();
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    public void onBackPressed() {
        if (mDisableBackPress)
            return;
        super.onBackPressed();
    }

    @Override
    public void replayDone() {
    }
}
