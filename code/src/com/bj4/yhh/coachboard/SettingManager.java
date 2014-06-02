
package com.bj4.yhh.coachboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

public class SettingManager {

    public static final int SPORT_TYPE_BASKETBALL = 0;

    public static final int SPORT_TYPE_SOCCER = 1;

    public static final int SPORT_TYPE_FOOTBALL = 2;

    public static final int SPORT_TYPE_TENNIS = 3;

    public static final int SPORT_TYPE_BASEBALL = 4;

    public static final int SPORT_TYPE_VOLLEYBALL = 5;

    public static final int SPORT_TYPE_HANDBALL = 6;

    public static final int SPORT_TYPE_HOCKEY = 7;

    public static final int SPORT_TYPE_TCHOUK = 8;

    private static final int SPORT_TYPE_TOTAL_ITEMS = SPORT_TYPE_TCHOUK;

    private static final String FILE_NAME_PREFIX_BASKETBALL = "BASKET_";

    private static final String FILE_NAME_PREFIX_BASEBALL = "BASEB_";

    private static final String FILE_NAME_PREFIX_SOCCER = "SOCCER_";

    private static final String FILE_NAME_PREFIX_FOOTBALL = "FOOT_";

    private static final String FILE_NAME_PREFIX_TENNIS = "TENNIS_";

    private static final String FILE_NAME_PREFIX_VOLLEYBALL = "VOLLEY_";

    private static final String FILE_NAME_PREFIX_HANDBALL = "HANDBALL_";

    private static final String FILE_NAME_PREFIX_HOCKEY = "HOCKEY_";

    private static final String FILE_NAME_PREFIX_TCHOUK = "TCHOUK_";

    private int mCurrentSportType = -1;

    private Context mContext;

    private SharedPreferences mPref;

    private boolean mShowAds = true;

    private int mCurrentPaintColor = Color.WHITE;

    public static final int BACK_KEY_FUNCTION_SYSTEM_DEFAULT = 0;

    public static final int BACK_KEY_FUNCTION_DISABLE = 1;

    public static final int BACK_KEY_FUNCTION_UNDO = 2;

    private int mBackKeyFunction = BACK_KEY_FUNCTION_DISABLE;

    private static final String SHAREDPREFERENCES_KEY = "settings";

    private static final String KEY_SPORT_TYPE = "sport_type";

    private static final String KEY_SHOW_ADS = "show_ads";

    private static final String KEY_ENABLE_BACKPRESS = "enable_backpress";

    private static final String KEY_PAINT_COLOR = "paint_color";

    public SettingManager(Context context) {
        mContext = context.getApplicationContext();
        mPref = mContext.getSharedPreferences(SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE);
        // init value
        mCurrentSportType = mPref.getInt(KEY_SPORT_TYPE, SPORT_TYPE_BASKETBALL);
        mShowAds = mPref.getBoolean(KEY_SHOW_ADS, true);
        mBackKeyFunction = mPref.getInt(KEY_ENABLE_BACKPRESS, BACK_KEY_FUNCTION_DISABLE);
        mCurrentPaintColor = mPref.getInt(KEY_PAINT_COLOR, Color.WHITE);
    }

    public void setPaintColor(int color) {
        mCurrentPaintColor = color;
        mPref.edit().putInt(KEY_PAINT_COLOR, color).apply();
    }

    public int getPaintColor() {
        return mCurrentPaintColor;
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
            case SPORT_TYPE_HANDBALL:
                prefix = FILE_NAME_PREFIX_HANDBALL;
                break;
            case SPORT_TYPE_HOCKEY:
                prefix = FILE_NAME_PREFIX_HOCKEY;
                break;
            case SPORT_TYPE_TCHOUK:
                prefix = FILE_NAME_PREFIX_TCHOUK;
                break;
        }
        return prefix;
    }

    public int getBackpressFunction() {
        return mBackKeyFunction;
    }

    public void setBackKeyFunction(int function) {
        mBackKeyFunction = function;
        mPref.edit().putInt(KEY_ENABLE_BACKPRESS, function).apply();
    }

    public boolean isShowAds() {
        return mShowAds;
    }

    public void setShowAds(boolean show) {
        mShowAds = show;
        mPref.edit().putBoolean(KEY_SHOW_ADS, show).apply();
    }
}
