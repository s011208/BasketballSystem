
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

    private static final String FILE_NAME_PREFIX_BASKETBALL = "BASKET_";

    private static final String FILE_NAME_PREFIX_BASEBALL = "BASEB_";

    private static final String FILE_NAME_PREFIX_SOCCER = "SOCCER_";

    private static final String FILE_NAME_PREFIX_FOOTBALL = "FOOT_";

    private static final String FILE_NAME_PREFIX_TENNIS = "TENNIS_";

    private static final String FILE_NAME_PREFIX_VOLLEYBALL = "VOLLEY_";

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

    public String generateFileName() {
        final String UUID = java.util.UUID.randomUUID().toString();
        return getPrefix() + UUID;
    }

    public String getPrefix() {
        String prefix = "";
        switch (mCurrentSportType) {
            case SPORT_TYPE_BASKETBALL:
                prefix = FILE_NAME_PREFIX_BASKETBALL;
                break;
            case SPORT_TYPE_SOCCER:
                prefix = FILE_NAME_PREFIX_SOCCER;
                break;
            case SPORT_TYPE_BASEBALL:
                prefix = FILE_NAME_PREFIX_BASEBALL;
                break;
            case SPORT_TYPE_FOOTBALL:
                prefix = FILE_NAME_PREFIX_FOOTBALL;
                break;
            case SPORT_TYPE_TENNIS:
                prefix = FILE_NAME_PREFIX_TENNIS;
                break;
            case SPORT_TYPE_VOLLEYBALL:
                prefix = FILE_NAME_PREFIX_VOLLEYBALL;
                break;
        }
        return prefix;
    }
}
