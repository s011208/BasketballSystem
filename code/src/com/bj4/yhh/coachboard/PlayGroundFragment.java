
package com.bj4.yhh.coachboard;

import org.json.JSONObject;

import com.bj4.yhh.coachboard.PlayGround.MainActivityCallback;
import com.bj4.yhh.coachboard.basketball.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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

    private View mChangePenColorView;

    private AdView mBottomBanner;

    private Context mContext;

    public PlayGroundFragment() {
    }

    public PlayGroundFragment(Context context, boolean isFullGround) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater)mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSettingManager = CoachBoardApplication.getSettingManager(context);
        setPlayGround(isFullGround);
        mContentView = (RelativeLayout)inflater.inflate(R.layout.play_ground_fragment, null);
        initComponents();
        mBottomBanner = (AdView)mContentView.findViewById(R.id.adView);
        if (mSettingManager.isShowAds() && isFullGround == false) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mBottomBanner.loadAd(adRequest);
        } else {
            mBottomBanner.setVisibility(View.GONE);
        }
    }

    public void onResume() {
        super.onResume();
        if (mBottomBanner != null)
            mBottomBanner.resume();
    }

    public void onDestroy() {
        if (mBottomBanner != null)
            mBottomBanner.destroy();
        super.onDestroy();
    }

    public void onPause() {
        if (mBottomBanner != null)
            mBottomBanner.pause();
        super.onPause();
    }

    public void onSportTypeChanged() {
        mPlayGround.setBackgroundResource(getBackgroundResourceId());
        mPlayGround.onSportTypeChanged(mSettingManager.getSportType());
        mRedTeamCb.setChecked(true);
        mBlueTeamCb.setChecked(true);
        mBallCb.setChecked(true);
        mPenCb.setChecked(true);
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
                rtn = mIsFullGround ? R.drawable.baseball_ground : R.drawable.baseball_ground;
                break;
            case SettingManager.SPORT_TYPE_BASKETBALL:
                rtn = mIsFullGround ? R.drawable.basketball_full_play_ground
                        : R.drawable.basketball_half_play_ground;
                break;
            case SettingManager.SPORT_TYPE_FOOTBALL:
                rtn = mIsFullGround ? R.drawable.football_full_ground
                        : R.drawable.football_half_ground;
                break;
            case SettingManager.SPORT_TYPE_SOCCER:
                rtn = mIsFullGround ? R.drawable.soccer_full_ground : R.drawable.soccer_half_ground;
                break;
            case SettingManager.SPORT_TYPE_TENNIS:
                rtn = mIsFullGround ? R.drawable.tennis_full_ground : R.drawable.tennis_half_ground;
                break;
            case SettingManager.SPORT_TYPE_VOLLEYBALL:
                rtn = mIsFullGround ? R.drawable.volleyball_full_ground
                        : R.drawable.volleyball_half_ground;
                break;
            case SettingManager.SPORT_TYPE_HANDBALL:
                rtn = mIsFullGround ? R.drawable.handball_full_ground
                        : R.drawable.handball_half_ground;
                break;
            case SettingManager.SPORT_TYPE_HOCKEY:
                rtn = mIsFullGround ? R.drawable.hockey_full_ground : R.drawable.hockey_half_ground;
                break;
            case SettingManager.SPORT_TYPE_TCHOUK:
                rtn = mIsFullGround ? R.drawable.tchoukball_full_ground
                        : R.drawable.tchoukball_half_ground;
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
            mChangePenColorView = mContentView.findViewById(R.id.pen_color_view);
            mChangePenColorView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    final int paintColor = mPlayGround.getPaintColor();
                    final int selectedItem;
                    if (Color.WHITE == paintColor) {
                        selectedItem = 0;
                    } else if (Color.RED == paintColor) {
                        selectedItem = 1;
                    } else if (Color.BLUE == paintColor) {
                        selectedItem = 2;
                    } else if (Color.YELLOW == paintColor) {
                        selectedItem = 3;
                    } else {
                        selectedItem = 4;
                    }
                    new AlertDialog.Builder(new ContextThemeWrapper(mContext,
                            android.R.style.Theme_Holo_Light_Dialog))
                            .setTitle(R.string.choose_pen_color_dialog_title)
                            .setSingleChoiceItems(
                                    new String[] {
                                            mContext.getString(R.string.color_white),
                                            mContext.getString(R.string.color_red),
                                            mContext.getString(R.string.color_blue),
                                            mContext.getString(R.string.color_yellow),
                                            mContext.getString(R.string.color_black)
                                    }, selectedItem, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case 0:
                                                    mChangePenColorView
                                                            .setBackgroundColor(Color.WHITE);
                                                    mPlayGround.setPaintColor(Color.WHITE);
                                                    break;
                                                case 1:
                                                    mChangePenColorView
                                                            .setBackgroundColor(Color.RED);
                                                    mPlayGround.setPaintColor(Color.RED);
                                                    break;
                                                case 2:
                                                    mChangePenColorView
                                                            .setBackgroundColor(Color.BLUE);
                                                    mPlayGround.setPaintColor(Color.BLUE);
                                                    break;
                                                case 3:
                                                    mChangePenColorView
                                                            .setBackgroundColor(Color.YELLOW);
                                                    mPlayGround.setPaintColor(Color.YELLOW);
                                                    break;
                                                case 4:
                                                    mChangePenColorView
                                                            .setBackgroundColor(Color.BLACK);
                                                    mPlayGround.setPaintColor(Color.BLACK);
                                                    break;
                                            }
                                            dialog.dismiss();
                                        }
                                    }).setNegativeButton(R.string.cancel, null).show();
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mContentView;
    }
}
