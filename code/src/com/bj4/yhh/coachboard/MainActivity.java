
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
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.os.Handler;
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

import com.google.android.gms.ads.*;

public class MainActivity extends Activity implements ActionBar.TabListener, MainActivityCallback,
        SettingsFragment.SettingsChangedCallback {

    private static final String TAG = "MainActivity";

    private static final boolean DEBUG = true;

    private SettingManager mSettingManager;

    private PlayGroundFragment mFullGround, mHalfGround;

    private SaveDataListFragment mSaveDataListFragment;

    private SettingsFragment mSettingsFragment;

    private Handler mHandler = new Handler();

    private static final int TAB_FULLGROUND = 0;

    private static final int TAB_HALFGROUND = 1;

    private static final int TAB_SAVE_DATALIST = 2;

    private static final int TAB_SETTINGS = 3;

    private int mCurrentFragment = TAB_FULLGROUND;

    private static final String INTERSTITIAL_ID = "ca-app-pub-6361389364792908/5697130627";

    private InterstitialAd mInterstitial;

    private Fragment getCurrentFragment() {
        switch (mCurrentFragment) {
            case TAB_FULLGROUND:
                return getFullGround();
            case TAB_HALFGROUND:
                return getHalfGround();
            case TAB_SAVE_DATALIST:
                return getSaveDataListFragment();
            case TAB_SETTINGS:
                return getSettingsFragment();
        }
        return null;
    }

    private synchronized SettingsFragment getSettingsFragment() {
        if (mSettingsFragment == null) {
            mSettingsFragment = new SettingsFragment(this);
            mSettingsFragment.setCallback(this);
        }
        return mSettingsFragment;
    }

    private synchronized SaveDataListFragment getSaveDataListFragment() {
        if (mSaveDataListFragment == null) {
            mSaveDataListFragment = new SaveDataListFragment(this);
        }
        return mSaveDataListFragment;
    }

    private synchronized PlayGroundFragment getFullGround() {
        if (mFullGround == null) {
            mFullGround = new PlayGroundFragment(this, true);
            mFullGround.setCallback(this);
        }
        return mFullGround;
    }

    private synchronized PlayGroundFragment getHalfGround() {
        if (mHalfGround == null) {
            mHalfGround = new PlayGroundFragment(this, false);
            mHalfGround.setCallback(this);
        }
        return mHalfGround;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSettingManager = CoachBoardApplication.getSettingManager(this);
        initActionBar();
        if (mSettingManager.isShowAds()) {
            mInterstitial = new InterstitialAd(this);
            mInterstitial.setAdUnitId(INTERSTITIAL_ID);
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitial.loadAd(adRequest);
        }
    }

    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent == null)
            return; // wtf?
        final String data = intent
                .getStringExtra(SimpleDataWidgetService.SIMPLE_DATA_WIDGET_SERVICE_CLICKED_ITEM_DATA);
        final String dataFileName = intent
                .getStringExtra(SimpleDataWidgetService.SIMPLE_DATA_WIDGET_SERVICE_CLICKED_ITEM_FILENAME);
        if (data != null && "".equals(data) == false) {
            if (getCurrentFragment() instanceof PlayGroundFragment) {
                final PlayGroundFragment playGroundFragment = (PlayGroundFragment)getCurrentFragment();

                if (dataFileName != null && "".equals(dataFileName) == false) {
                    if (dataFileName.startsWith(SettingManager.FILE_NAME_PREFIX_BASEBALL)) {
                        mSettingManager.setSportType(SettingManager.SPORT_TYPE_BASEBALL);
                    } else if (dataFileName.startsWith(SettingManager.FILE_NAME_PREFIX_BASKETBALL)) {
                        mSettingManager.setSportType(SettingManager.SPORT_TYPE_BASKETBALL);
                    } else if (dataFileName.startsWith(SettingManager.FILE_NAME_PREFIX_FOOTBALL)) {
                        mSettingManager.setSportType(SettingManager.SPORT_TYPE_FOOTBALL);
                    } else if (dataFileName.startsWith(SettingManager.FILE_NAME_PREFIX_HANDBALL)) {
                        mSettingManager.setSportType(SettingManager.SPORT_TYPE_HANDBALL);
                    } else if (dataFileName.startsWith(SettingManager.FILE_NAME_PREFIX_HOCKEY)) {
                        mSettingManager.setSportType(SettingManager.SPORT_TYPE_HOCKEY);
                    } else if (dataFileName.startsWith(SettingManager.FILE_NAME_PREFIX_SOCCER)) {
                        mSettingManager.setSportType(SettingManager.SPORT_TYPE_SOCCER);
                    } else if (dataFileName.startsWith(SettingManager.FILE_NAME_PREFIX_TCHOUK)) {
                        mSettingManager.setSportType(SettingManager.SPORT_TYPE_TCHOUK);
                    } else if (dataFileName.startsWith(SettingManager.FILE_NAME_PREFIX_TENNIS)) {
                        mSettingManager.setSportType(SettingManager.SPORT_TYPE_TENNIS);
                    } else if (dataFileName.startsWith(SettingManager.FILE_NAME_PREFIX_VOLLEYBALL)) {
                        mSettingManager.setSportType(SettingManager.SPORT_TYPE_VOLLEYBALL);
                    }
                    onSportTypeChanged();
                }
                mHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            playGroundFragment.restoreData(new JSONObject(data));
                        } catch (JSONException e) {
                            Log.w(TAG, "failed in onNewIntent", e);
                        }
                    }
                }, 1000);

            }
        }
    }

    public void onResume() {
        super.onResume();
        if (mSettingManager.isShowAds()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitial.loadAd(adRequest);
            if (mSettingManager.getBackpressFunction() == SettingManager.BACK_KEY_FUNCTION_DISABLE
                    || mSettingManager.getBackpressFunction() == SettingManager.BACK_KEY_FUNCTION_UNDO) {
                if (mInterstitial.isLoaded()) {
                    mInterstitial.show();
                }
            }
        }
    }

    public void onPause() {
        super.onPause();
        if (getCurrentFragment() instanceof PlayGroundFragment) {
            final PlayGroundFragment playGroundFragment = (PlayGroundFragment)getCurrentFragment();
            playGroundFragment.cancelReplay();
        }
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(
                getApplicationContext(), SimpleDataWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,
                R.id.simple_data_widget_listview);
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
        actionBar.addTab(actionBar.newTab().setText(R.string.tab_settings).setTabListener(this),
                TAB_SETTINGS);
        setTabChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        if (mCurrentFragment == TAB_SAVE_DATALIST || mCurrentFragment == TAB_SETTINGS) {
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
                    final String filePrefix = CoachBoardApplication.getSettingManager(this)
                            .getPrefix();
                    final File[] fileList = getFilesDir().listFiles();
                    for (File f : fileList) {
                        String fileName = f.getName();
                        if (fileName.startsWith(filePrefix)) {
                            String data = PlayGround.readFromFile(f.getAbsolutePath());
                            try {
                                JSONObject j = new JSONObject(data);
                                itemTitle.add(j.getString(PlayGround.JSON_KEY_TITLE));
                                itemJList.add(j);
                            } catch (JSONException e) {
                                if (DEBUG)
                                    Log.w(TAG, "failed", e);
                            }
                        }
                    }
                    final String[] title = itemTitle.toArray(new String[0]);
                    if (title.length > 0) {
                        new AlertDialog.Builder(new ContextThemeWrapper(this,
                                android.R.style.Theme_Holo_Light_Dialog))
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
                    titleEditText.setHint(R.string.save_board_edittext_hint);
                    new AlertDialog.Builder(new ContextThemeWrapper(this,
                            android.R.style.Theme_Holo_Light_Dialog))
                            .setTitle(R.string.action_bar_save_dialog_title)
                            .setIcon(android.R.drawable.ic_dialog_info).setView(titleEditText)
                            .setPositiveButton(R.string.ok, new OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String textContent = titleEditText.getText().toString().trim();
                                    if (textContent.length() == 0)
                                        textContent = titleEditText.getHint().toString();
                                    playGroundFragment.saveData(textContent);
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
        int sportType = mSettingManager.getSportType();
        if (sportType == SettingManager.SPORT_TYPE_BASEBALL && position > TAB_FULLGROUND)
            ++position;
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
            case TAB_SETTINGS:
                fragmentTransaction.replace(R.id.fragment_main, getSettingsFragment());
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
        int backKeyFunction = mSettingManager.getBackpressFunction();
        switch (backKeyFunction) {
            case SettingManager.BACK_KEY_FUNCTION_SYSTEM_DEFAULT:
                if (mSettingManager.isShowAds()) {
                    if (mInterstitial.isLoaded()) {
                        mInterstitial.show();
                    }
                }
                break;
            case SettingManager.BACK_KEY_FUNCTION_DISABLE:
                return;
            case SettingManager.BACK_KEY_FUNCTION_UNDO:
                if (getCurrentFragment() instanceof PlayGroundFragment) {
                    PlayGroundFragment playGroundFragment = (PlayGroundFragment)getCurrentFragment();
                    playGroundFragment.undo();
                }
                return;
            default:
                return;
        }
        super.onBackPressed();
    }

    @Override
    public void replayDone() {
    }

    @Override
    public void onSportTypeChanged() {
        getFullGround().onSportTypeChanged();
        getHalfGround().onSportTypeChanged();
        setTabChanged();
    }

    private void setTabChanged() {
        int sportType = mSettingManager.getSportType();
        ActionBar actionBar = getActionBar();
        int tabCount = actionBar.getTabCount();
        switch (sportType) {
            case SettingManager.SPORT_TYPE_BASEBALL:
                if (tabCount == 4) {
                    actionBar.removeTabAt(TAB_HALFGROUND);
                }
                break;
            default:
                if (tabCount == 3) {
                    actionBar.addTab(actionBar.newTab().setText(R.string.tab_half_ground)
                            .setTabListener(this), TAB_HALFGROUND);
                }
        }
    }
}
