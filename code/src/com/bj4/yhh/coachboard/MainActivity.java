
package com.bj4.yhh.coachboard;

import java.util.Locale;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
            mFullGround = new PlayGroundFragment();
            mFullGround.setPlayGround(R.drawable.full_play_ground);
        }
        return mFullGround;
    }

    private synchronized PlayGroundFragment getHalfGround() {
        if (mHalfGround == null) {
            mHalfGround = new PlayGroundFragment();
            mHalfGround.setPlayGround(R.drawable.half_playground);
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
        if (id == R.id.action_settings) {
            return true;
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
