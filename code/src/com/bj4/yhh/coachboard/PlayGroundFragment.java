
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

    private int mPlayGroundResource;

    private CheckBox mRedTeamCb, mBlueTeamCb, mBallCb, mPenCb;

    public PlayGroundFragment() {
    }

    public PlayGroundFragment(Context context, int playGroundResource) {
        LayoutInflater inflater = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setPlayGround(playGroundResource);
        mContentView = (RelativeLayout)inflater.inflate(R.layout.play_ground_fragment, null);
        initComponents();
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

    public void resetAll() {
        if (mPlayGround != null) {
            mPlayGround.resetAll();
        }
    }

    public void setPlayGround(int res) {
        mPlayGroundResource = res;
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

    private void initComponents() {
        if (mContentView != null) {
            mPlayGround = (PlayGround)mContentView.findViewById(R.id.playground);
            mPlayGround.setBackgroundResource(mPlayGroundResource);
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
