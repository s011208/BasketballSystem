
package com.bj4.yhh.coachboard;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingManager {

    public static final int SPORT_TYPE_BASKETBALL = 0;

    public static final int SPORT_TYPE_SOCCER = 1;

    public static final int SPORT_TYPE_FOOTBALL = 2;

    public static final int SPORT_TYPE_TENNIS = 3;

    public static final int SPORT_TYPE_BASEBALL = 4;

    public static final int SPORT_TYPE_VOLLEYBALL = 5;

    private static final int SPORT_TYPE_TOTAL_ITEMS = SPORT_TYPE_VOLLEYBALL;

    private int mCurrentSportType = -1;

    private Context mContext;

    private SharedPreferences mPref;

    private static final String SHAREDPREFERENCES_KEY = "settings";

    private static final String KEY_SPORT_TYPE = "sport_type";
    
    public static final String BROADCAST_NOTIFY_SPORT_TYPE_CHANGED = "com.bj4.yhh.coachboard.sporttype_changed";

    public SettingManager(Context context) {
        mContext = context.getApplicationContext();
        mPref = mContext.getSharedPreferences(SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
        // init value
        mCurrentSportType = mPref.getInt(KEY_SPORT_TYPE, SPORT_TYPE_BASKETBALL);
    }

    public void setSportType(int type) {
        if (type <= SPORT_TYPE_TOTAL_ITEMS) {
            mCurrentSportType = type;
            mPref.edit().putInt(KEY_SPORT_TYPE, type).apply();
        } else {
            // ignore wrong
        }
    }

    public int getSportType() {
        return mCurrentSportType;
    }
}
