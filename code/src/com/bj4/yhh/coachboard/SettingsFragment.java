
package com.bj4.yhh.coachboard;

import java.util.ArrayList;

import com.bj4.yhh.coachboard.basketball.R;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

public class SettingsFragment extends Fragment {

    private Handler mHandler = new Handler();

    public interface SettingsChangedCallback {
        public void onSportTypeChanged();
    }

    public void setCallback(SettingsChangedCallback cb) {
        mCallback = cb;
    }

    private SettingsChangedCallback mCallback;

    private LinearLayout mContentView;

    private Context mContext;

    private LayoutInflater mInflater;

    private RadioButton mRBasketball, mRBaseBall, mRSoccer, mRFootBall, mRTennis, mRVolleyBall;

    private ArrayList<RadioButton> mSportTypeButtons = new ArrayList<RadioButton>();

    private SettingManager mSettingManager;

    public SettingsFragment() {
    }

    public SettingsFragment(Context context) {
        mContext = context;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = (LinearLayout)mInflater.inflate(R.layout.settings_fragment, null);
        mSettingManager = CoachBoardApplication.getSettingManager(mContext);
        initComponents();
    }

    private void initComponents() {
        initRadioButtons();
    }

    private void initRadioButtons() {
        mRBasketball = (RadioButton)mContentView.findViewById(R.id.sport_type_basketball);
        mRBaseBall = (RadioButton)mContentView.findViewById(R.id.sport_type_baseball);
        mRSoccer = (RadioButton)mContentView.findViewById(R.id.sport_type_soccer);
        mRFootBall = (RadioButton)mContentView.findViewById(R.id.sport_type_football);
        mRTennis = (RadioButton)mContentView.findViewById(R.id.sport_type_tennis);
        mRVolleyBall = (RadioButton)mContentView.findViewById(R.id.sport_type_volleyball);
        int sportType = mSettingManager.getSportType();
        switch (sportType) {
            case SettingManager.SPORT_TYPE_BASEBALL:
                mRBaseBall.setChecked(true);
                break;
            case SettingManager.SPORT_TYPE_BASKETBALL:
                mRBasketball.setChecked(true);
                break;
            case SettingManager.SPORT_TYPE_FOOTBALL:
                mRFootBall.setChecked(true);
                break;
            case SettingManager.SPORT_TYPE_SOCCER:
                mRSoccer.setChecked(true);
                break;
            case SettingManager.SPORT_TYPE_TENNIS:
                mRTennis.setChecked(true);
                break;
            case SettingManager.SPORT_TYPE_VOLLEYBALL:
                mRVolleyBall.setChecked(true);
                break;
        }
        mSportTypeButtons.add(mRBasketball);
        mSportTypeButtons.add(mRBaseBall);
        mSportTypeButtons.add(mRSoccer);
        mSportTypeButtons.add(mRFootBall);
        mSportTypeButtons.add(mRTennis);
        mSportTypeButtons.add(mRVolleyBall);
        OnCheckedChangeListener radioClickListener = new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    boolean hasCheckChanged = false;
                    final int checkedId = buttonView.getId();
                    for (RadioButton btn : mSportTypeButtons) {
                        if (checkedId != btn.getId()) {
                            if (btn.isChecked())
                                hasCheckChanged = true;
                            btn.setChecked(false);
                        }
                    }
                    switch (checkedId) {
                        case R.id.sport_type_basketball:
                            mSettingManager.setSportType(SettingManager.SPORT_TYPE_BASKETBALL);
                            break;
                        case R.id.sport_type_baseball:
                            mSettingManager.setSportType(SettingManager.SPORT_TYPE_BASEBALL);
                            break;
                        case R.id.sport_type_soccer:
                            mSettingManager.setSportType(SettingManager.SPORT_TYPE_SOCCER);
                            break;
                        case R.id.sport_type_football:
                            mSettingManager.setSportType(SettingManager.SPORT_TYPE_FOOTBALL);
                            break;
                        case R.id.sport_type_tennis:
                            mSettingManager.setSportType(SettingManager.SPORT_TYPE_TENNIS);
                            break;
                        case R.id.sport_type_volleyball:
                            mSettingManager.setSportType(SettingManager.SPORT_TYPE_VOLLEYBALL);
                            break;
                        default:
                            mSettingManager.setSportType(SettingManager.SPORT_TYPE_BASKETBALL);
                    }
                    if (hasCheckChanged) {
                        mHandler.post(new Runnable() {

                            @Override
                            public void run() {
                                if (mCallback != null) {
                                    mCallback.onSportTypeChanged();
                                }
                            }
                        });
                    }
                }
            }
        };
        for (RadioButton btn : mSportTypeButtons) {
            btn.setOnCheckedChangeListener(radioClickListener);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mContentView;
    }

}
