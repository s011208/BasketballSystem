
package com.bj4.yhh.coachboard;

import org.json.JSONObject;

import com.bj4.yhh.coachboard.PlayGround.MainActivityCallback;
import com.bj4.yhh.coachboard.basketball.R;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;

public class PlayGroundFragment extends Fragment {
    private PlayGround mPlayGround;

    private RelativeLayout mContentView;

    private boolean mIsFullGround;

    private CheckBox mRedTeamCb, mBlueTeamCb, mBallCb, mPenCb;

    private SettingManager mSettingManager;

    public PlayGroundFragment() {
    }

    public PlayGroundFragment(Context context, boolean isFullGround) {
        LayoutInflater inflater = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSettingManager = CoachBoardApplication.getSettingManager(context);
        setPlayGround(isFullGround);
        mContentView = (RelativeLayout)inflater.inflate(R.layout.play_ground_fragment, null);
        initComponents();
    }

    public void onSportTypeChanged() {
        mPlayGround.setBackgroundResource(getBackgroundResourceId());
        mPlayGround.onSportTypeChanged(mSettingManager.getSportType());
    }

    public void sharePlayGround() {
        if (mPlayGround != null) {
            mPlayGround.sharePlayGround();
        }
    }

    public void erasePen() {
        if (mPlayGround != null) {
            mPlayGround.erasePen();
        }
    }

    public void restoreData(JSONObject jObject) {
        if (mPlayGround != null) {
            mPlayGround.restoreData(jObject);
        }
    }

    public String saveData(String title) {
        if (mPlayGround != null) {
            mPlayGround.saveData(title);
        }
        return null;
    }

    public void replay() {
        if (mPlayGround != null) {
            mPlayGround.replay();
        }
    }

    public void cancelReplay() {
        if (mPlayGround != null) {
            mPlayGround.cancelReplay();
        }
    }

    public void undo() {
        if (mPlayGround != null) {
            mPlayGround.undo();
        }
    }

    public void resetAll() {
        if (mPlayGround != null) {
            mPlayGround.resetAll();
        }
    }

    public void setPlayGround(boolean isFullGround) {
        mIsFullGround = isFullGround;
    }

    public boolean isReplaying() {
        if (mPlayGround != null) {
            return mPlayGround.isReplaying();
        }
        return false;
    }

    public void setCallback(MainActivityCallback cb) {
        if (mPlayGround != null) {
            mPlayGround.setCallback(cb);
        }
    }

    private int getBackgroundResourceId() {
        int sportType = mSettingManager.getSportType();
        int rtn = 0;
        switch (sportType) {
            case SettingManager.SPORT_TYPE_BASEBALL:
                rtn = mIsFullGround ? R.drawable.basketball_full_play_ground
                        : R.drawable.basketball_half_play_ground;
                break;
            case SettingManager.SPORT_TYPE_BASKETBALL:
                rtn = mIsFullGround ? R.drawable.basketball_full_play_ground
                        : R.drawable.basketball_half_play_ground;
                break;
            case SettingManager.SPORT_TYPE_FOOTBALL:
                rtn = mIsFullGround ? R.drawable.basketball_full_play_ground
                        : R.drawable.basketball_half_play_ground;
                break;
            case SettingManager.SPORT_TYPE_SOCCER:
                rtn = mIsFullGround ? R.drawable.soccer_full_ground : R.drawable.soccer_half_ground;
                break;
            case SettingManager.SPORT_TYPE_TENNIS:
                rtn = mIsFullGround ? R.drawable.basketball_full_play_ground
                        : R.drawable.basketball_half_play_ground;
                break;
            case SettingManager.SPORT_TYPE_VOLLEYBALL:
                rtn = mIsFullGround ? R.drawable.basketball_full_play_ground
                        : R.drawable.basketball_half_play_ground;
                break;
        }
        return rtn;
    }

    private void initComponents() {
        if (mContentView != null) {
            mPlayGround = (PlayGround)mContentView.findViewById(R.id.playground);
            mPlayGround.setBackgroundResource(getBackgroundResourceId());
            mRedTeamCb = (CheckBox)mContentView.findViewById(R.id.playground_red_cb);
            mBlueTeamCb = (CheckBox)mContentView.findViewById(R.id.playground_blue_cb);
            mBallCb = (CheckBox)mContentView.findViewById(R.id.playground_ball_cb);
            mPenCb = (CheckBox)mContentView.findViewById(R.id.playground_pen_cb);
            mRedTeamCb.setChecked(true);
            mBlueTeamCb.setChecked(true);
            mBallCb.setChecked(true);
            mPenCb.setChecked(true);
            mRedTeamCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mPlayGround.setRedTeamVisiblity(isChecked);
                }
            });
            mBlueTeamCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mPlayGround.setBlueTeamVisiblity(isChecked);
                }
            });
            mBallCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mPlayGround.setBallVisiblity(isChecked);
                }
            });
            mPenCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mPlayGround.setPenVisiblity(isChecked);
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mContentView;
    }
}
