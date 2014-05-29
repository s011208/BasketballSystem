
package com.bj4.yhh.coachboard;

import android.app.Fragment;
import android.os.Bundle;
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

    public void setPlayGround(int res) {
        mPlayGroundResource = res;
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
        mContentView = (RelativeLayout)inflater.inflate(R.layout.play_ground_fragment, null);
        initComponents();
        return mContentView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putInt("curChoice", mCurCheckPosition);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
//            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }
    }
}
