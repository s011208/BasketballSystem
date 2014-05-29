
package com.bj4.yhh.coachboard;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PlayGroundFragment extends Fragment {
    private PlayGround mPlayGround;

    private int mPlayGroundResource;

    public void setPlayGround(int res) {
        mPlayGroundResource = res;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPlayGround = new PlayGround(getActivity());
        mPlayGround.setBackgroundResource(mPlayGroundResource);
        return mPlayGround;
    }
}
